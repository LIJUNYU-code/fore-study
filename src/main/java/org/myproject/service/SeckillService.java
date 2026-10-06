package org.myproject.service;

import org.myproject.vo.SeckillActivityVO;

import java.util.List;

public interface SeckillService {


   /**
    * 查询秒杀活动列表
    * - 查询所有秒杀活动
    * - 转换为VO(包含商品名称、图片、原价)
    */
    List<SeckillActivityVO> listSeckillActivities();


    SeckillActivityVO getSeckillActivityById(Long id);


    // 步骤1: 校验活动是否存在
    // 步骤2: 校验活动是否在进行中(时间+状态)
    // 步骤3: 校验用户是否已参与(防止重复秒杀)
    // 步骤4: 校验用户是否超过限购数量
    // 步骤5: 使用Redis预减库存(DECR), 判断库存是否充足
    // 步骤6: 发送秒杀请求到消息队列(异步处理, 降低数据库压力)
    // 步骤7: 返回排队中状态
    // 注意: 实际的库存扣减和订单创建在消费者中完成
    Long executeSeckill(Long userId, Long seckillActivityId);


    // - 在Redis中查询秒杀结果
    // - 返回: 排队中 / 秒杀成功(订单ID) / 秒杀失败
    Long getSeckillResult(Long userId, Long seckillActivityId);


    // - 再次校验库存(Redis)
    // - 扣减数据库库存(使用乐观锁 UPDATE ... WHERE available_stock > 0)
    // - 创建订单
    // - 创建秒杀订单记录
    // - 将结果写入Redis
    void processSeckillOrder(Long userId, Long seckillActivityId);

    // - 扫描所有活动
    // - 根据当前时间更新活动状态(未开始→进行中→已结束)
    // - 活动开始时将库存预热到Redis
    void refreshActivityStatus();
}