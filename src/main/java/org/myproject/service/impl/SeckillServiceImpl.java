package org.myproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.myproject.entity.Product;
import org.myproject.entity.SeckillActivity;
import org.myproject.mapper.ProductMapper;
import org.myproject.mapper.SeckillActivityMapper;
import org.myproject.mapper.SeckillOrderMapper;
import org.myproject.service.SeckillService;
import org.myproject.vo.SeckillActivityVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeckillServiceImpl implements SeckillService {

    private final SeckillActivityMapper seckillActivityMapper;
    private final SeckillOrderMapper seckillOrderMapper;
    private final ProductMapper productMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final org.myproject.mapper.OrderMapper orderMapper;

    private static final String REDIS_STOCK_KEY = "seckill:stock:";
    private static final String REDIS_ORDER_KEY = "seckill:order:";
    private static final String REDIS_RESULT_KEY = "seckill:result:";
    private static final String REDIS_ACTIVITY_KEY = "seckill:activity:";
    private static final String REDIS_COUNT_KEY = "seckill:count:";

    private static final long RESULT_QUEUE = -1L;
    private static final long RESULT_FAIL = 0L;

    /**
     * 查询所有秒杀活动
     * @return 秒杀活动列表
     */
    @Override
    public List<SeckillActivityVO> listSeckillActivities() {

        LambdaQueryWrapper<SeckillActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillActivity::getDeleted, 0)
               .orderByDesc(SeckillActivity::getCreateTime);
        List<SeckillActivity> activityList = seckillActivityMapper.selectList(wrapper);

        List<SeckillActivityVO> voList = activityList.stream().map(activity -> {
            Product product = productMapper.selectById(activity.getProductId());

            return SeckillActivityVO.builder()
                    .id(activity.getId())
                    .productId(activity.getProductId())
                    .productName(product != null ? product.getName() : "未知商品")
                    .productImage(product != null ? product.getImage() : null)
                    .originalPrice(product != null ? product.getOriginalPrice() : null)
                    .seckillPrice(activity.getSeckillPrice())
                    .totalStock(activity.getTotalStock())
                    .availableStock(activity.getAvailableStock())
                    .startTime(activity.getStartTime())
                    .endTime(activity.getEndTime())
                    .status(activity.getStatus())
                    .limitPerUser(activity.getLimitPerUser())
                    .build();
        }).collect(Collectors.toList());

        return voList;
    }

    @Override
    public SeckillActivityVO getSeckillActivityById(Long id) {

        SeckillActivity activity = seckillActivityMapper.selectById(id);
        if (activity == null) {
            throw new IllegalArgumentException("秒杀活动不存在");
        }

        Product product = productMapper.selectById(activity.getProductId());

        return SeckillActivityVO.builder()
                .id(activity.getId())
                .productId(activity.getProductId())
                .productName(product != null ? product.getName() : "未知商品")
                .productImage(product != null ? product.getImage() : null)
                .originalPrice(product != null ? product.getOriginalPrice() : null)
                .seckillPrice(activity.getSeckillPrice())
                .totalStock(activity.getTotalStock())
                .availableStock(activity.getAvailableStock())
                .startTime(activity.getStartTime())
                .endTime(activity.getEndTime())
                .status(activity.getStatus())
                .limitPerUser(activity.getLimitPerUser())
                .build();
    }



    /**
     * 执行秒杀
     * @param userId 用户ID
     * @param seckillActivityId 秒杀活动ID
     * @return -1L=排队中, 0=秒杀失败, >0=订单ID(秒杀成功)
     */
    @Override
    public Long executeSeckill(Long userId, Long seckillActivityId) {

        // ====== 第一阶段: 接口层校验(快速失败) ======

        // 1. 从数据库查询活动信息
        SeckillActivity activity = seckillActivityMapper.selectById(seckillActivityId);
        if (activity == null) {
            throw new IllegalArgumentException("秒杀活动不存在");
        }

        // 2. 校验活动是否在进行中
        if (activity.getStatus() != 1) {
            throw new IllegalArgumentException("秒杀活动未开始或已结束");
        }

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (now.isBefore(activity.getStartTime())) {
            throw new IllegalArgumentException("秒杀活动还未开始");
        }
        if (now.isAfter(activity.getEndTime())) {
            throw new IllegalArgumentException("秒杀活动已结束");
        }

        // 3. 校验用户是否已参与秒杀(查询数据库)
        Long orderCount = seckillOrderMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<org.myproject.entity.SeckillOrder>()
                .eq(org.myproject.entity.SeckillOrder::getSeckillActivityId, seckillActivityId)
                .eq(org.myproject.entity.SeckillOrder::getUserId, userId)
        );
        if (orderCount > 0) {
            throw new IllegalArgumentException("您已参与过此秒杀活动");
        }

        // 4. 校验限购数量
        Integer limitPerUser = activity.getLimitPerUser();
        if (limitPerUser != null && limitPerUser > 0) {
            if (orderCount >= limitPerUser) {
                throw new IllegalArgumentException("您已超过限购数量");
            }
        }

        // ====== 第二阶段: Redis预减库存 ======

        // 5. 尝试预减库存
        String stockKey = REDIS_STOCK_KEY + seckillActivityId;
        Long stock = stringRedisTemplate.opsForValue().decrement(stockKey);

        if (stock == null || stock < 0) {
            // 库存不足，恢复库存
            if (stock != null) {
                stringRedisTemplate.opsForValue().increment(stockKey);
            }
            // 设置秒杀失败结果
            stringRedisTemplate.opsForValue().set(
                REDIS_RESULT_KEY + seckillActivityId + ":" + userId,
                String.valueOf(RESULT_FAIL),
                1, TimeUnit.HOURS
            );
            return RESULT_FAIL;
        }

        // ====== 第三阶段: 同步处理订单 ======

        try {
            // 直接处理订单（简化版本，生产环境可使用消息队列）
            processSeckillOrder(userId, seckillActivityId);
            return RESULT_QUEUE;
        } catch (Exception e) {
            // 处理失败，恢复库存
            stringRedisTemplate.opsForValue().increment(stockKey);
            // 设置秒杀失败结果
            stringRedisTemplate.opsForValue().set(
                REDIS_RESULT_KEY + seckillActivityId + ":" + userId,
                String.valueOf(RESULT_FAIL),
                1, TimeUnit.HOURS
            );
            throw new RuntimeException("秒杀失败：" + e.getMessage());
        }
    }

    @Override
    public Long getSeckillResult(Long userId, Long seckillActivityId) {
        // 查询Redis中的秒杀结果
        String result = stringRedisTemplate.opsForValue().get(
            REDIS_RESULT_KEY + seckillActivityId + ":" + userId
        );

        if (result == null) {
            return RESULT_QUEUE;
        }

        return Long.parseLong(result);
    }

    @Override
    public void processSeckillOrder(Long userId, Long seckillActivityId) {
        // 1. 再次从Redis检查库存
        String stockKey = REDIS_STOCK_KEY + seckillActivityId;
        String stockStr = stringRedisTemplate.opsForValue().get(stockKey);
        long stock = stockStr != null ? Long.parseLong(stockStr) : -1;

        if (stock < 0) {
            // 库存不足
            stringRedisTemplate.opsForValue().set(
                REDIS_RESULT_KEY + seckillActivityId + ":" + userId,
                String.valueOf(RESULT_FAIL),
                1, TimeUnit.HOURS
            );
            throw new RuntimeException("库存不足");
        }

        // 2. 使用乐观锁扣减数据库库存
        SeckillActivity activity = new SeckillActivity();
        activity.setId(seckillActivityId);
        activity.setAvailableStock(-1);

        int updated = seckillActivityMapper.decreaseStock(seckillActivityId);
        if (updated == 0) {
            // 扣减失败，库存不足
            stringRedisTemplate.opsForValue().increment(stockKey);
            stringRedisTemplate.opsForValue().set(
                REDIS_RESULT_KEY + seckillActivityId + ":" + userId,
                String.valueOf(RESULT_FAIL),
                1, TimeUnit.HOURS
            );
            throw new RuntimeException("库存不足，秒杀失败");
        }

        // 3. 创建订单
        Long orderId = createSeckillOrder(userId, seckillActivityId);

        // 4. 创建秒杀订单记录
        org.myproject.entity.SeckillOrder seckillOrder = new org.myproject.entity.SeckillOrder();
        seckillOrder.setSeckillActivityId(seckillActivityId);
        seckillOrder.setUserId(userId);
        seckillOrder.setOrderId(orderId);
        seckillOrderMapper.insert(seckillOrder);

        // 5. 设置Redis结果为订单ID(秒杀成功)
        stringRedisTemplate.opsForValue().set(
            REDIS_RESULT_KEY + seckillActivityId + ":" + userId,
            String.valueOf(orderId),
            1, TimeUnit.HOURS
        );

        // 6. 设置Redis已购标记
        stringRedisTemplate.opsForValue().set(
            REDIS_ORDER_KEY + seckillActivityId + ":" + userId,
            "1",
            24, TimeUnit.HOURS
        );

        // 7. 递增Redis已购计数
        stringRedisTemplate.opsForValue().increment(
            REDIS_COUNT_KEY + seckillActivityId + ":" + userId
        );
    }

    private Long createSeckillOrder(Long userId, Long seckillActivityId) {
        // 查询秒杀活动信息
        SeckillActivity activity = seckillActivityMapper.selectById(seckillActivityId);
        if (activity == null) {
            throw new IllegalArgumentException("秒杀活动不存在");
        }

        // 查询商品信息
        Product product = productMapper.selectById(activity.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        // 创建订单实体
        org.myproject.entity.Order order = new org.myproject.entity.Order();
        order.setUserId(userId);
        order.setOrderNo("SK" + System.currentTimeMillis() + userId);
        order.setTotalAmount(activity.getSeckillPrice());
        order.setPayAmount(activity.getSeckillPrice());
        order.setStatus(0);
        order.setRemark("秒杀订单");

        orderMapper.insert(order);

        return order.getId();
    }

    @Override
    public void refreshActivityStatus() {
        // 查询所有未结束的秒杀活动
        LambdaQueryWrapper<SeckillActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(SeckillActivity::getStatus, 2);
        List<SeckillActivity> activityList = seckillActivityMapper.selectList(wrapper);

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        for (SeckillActivity activity : activityList) {
            Integer newStatus;

            if (now.isBefore(activity.getStartTime())) {
                newStatus = 0; // 未开始
            } else if (now.isAfter(activity.getEndTime())) {
                newStatus = 2; // 已结束
            } else {
                newStatus = 1; // 进行中
            }

            // 如果状态发生变化，更新数据库
            if (!newStatus.equals(activity.getStatus())) {
                activity.setStatus(newStatus);
                seckillActivityMapper.updateById(activity);
            }

            // 将库存预热到Redis
            String stockKey = REDIS_STOCK_KEY + activity.getId();
            String currentStock = stringRedisTemplate.opsForValue().get(stockKey);
            if (currentStock == null) {
                stringRedisTemplate.opsForValue().set(
                    stockKey,
                    String.valueOf(activity.getAvailableStock() != null ? activity.getAvailableStock() : 0)
                );
            }
        }
    }
}