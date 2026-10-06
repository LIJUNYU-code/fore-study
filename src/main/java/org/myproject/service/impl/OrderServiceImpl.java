package org.myproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.myproject.dto.OrderCreateDTO;
import org.myproject.entity.Cart;
import org.myproject.entity.Order;
import org.myproject.entity.OrderItem;
import org.myproject.entity.Product;
import org.myproject.mapper.CartMapper;
import org.myproject.mapper.OrderItemMapper;
import org.myproject.mapper.OrderMapper;
import org.myproject.service.OrderService;
import org.myproject.service.ProductService;
import org.myproject.utils.SnowflakeIdUtil;
import org.myproject.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(Long userId, OrderCreateDTO dto) {

        // 1. 根据cartItemIds查询购物车项, 校验都属于当前用户
        List<Cart> cartItems = cartMapper.selectBatchIds(dto.getCartItemIds());

        // 2. 根据productId查询商品信息, 校验商品状态和库存
        List<Product> products = productService.getProductsByCartItems(cartItems);

        // 3. 扣减商品库存(调用productService.reduceStock)
        cartItems.forEach(cart -> productService.reduceStock(cart.getProductId(), cart.getQuantity()));
        // 4. 生成订单编号(使用SnowflakeIdUtil或时间戳+随机数)
        Long orderId = SnowflakeIdUtil.nextId();
        // 5. 计算订单总金额 = SUM(商品单价 * 数量)
        double totalPrice = cartItems.stream().mapToDouble(cart -> products.get(products.indexOf(cart)).getPrice().doubleValue() * cart.getQuantity()).sum();
        // 6. 创建Order对象并插入数据库
        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .totalAmount(BigDecimal.valueOf(totalPrice))
                .status(0)
                .remark(dto.getRemark())
                .build();
        // 7. 创建OrderItem列表并批量插入
        List<OrderItem> orderItems = cartItems.stream()
                .map(cart -> OrderItem.builder()
                        .orderId(orderId)
                        .productId(cart.getProductId())
                        .price(products.get(products.indexOf(cart)).getPrice())
                        .quantity(cart.getQuantity())
                        .build())
                .toList();
        orderItemMapper.insertBatch(orderItems);
        // 8. 删除已结算的购物车项
        cartMapper.deleteBatchIds(cartItems.stream().map(Cart::getId).toList());
        // 9. 组装OrderVO返回
        return OrderVO.builder()
                .id(orderId)
                .userId(userId)
                .totalAmount(BigDecimal.valueOf(totalPrice))
                .status(0)
                .remark(dto.getRemark())
                .createTime(LocalDateTime.now())
                .build();

    }

    @Override
    public OrderVO getOrderById(Long id) {

        // 1. 查询订单主表
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        // 2. 查询订单项列表
        List<OrderItem> orderItems = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
        // 3. 组装OrderVO
        return OrderVO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .remark(order.getRemark())
                .createTime(order.getCreateTime())
                .orderItems(orderItems.stream()
                        .map(item -> OrderVO.OrderItemVO.builder()
                                .id(item.getId())
                                .productId(item.getProductId())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public List<OrderVO> listUserOrders(Long userId, Integer status, Integer page, Integer size) {

        Page<Order> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = orderMapper.selectPage(pageParam, wrapper);
        List<Order> orders = orderPage.getRecords();
        List<OrderVO> orderVOList = orders.stream()
                .map(order -> getOrderById(order.getId()))
                .toList();
        return orderVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long userId, Long orderId) {

        // 1. 查询订单, 校验是否属于当前用户
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (order.getUserId() != userId) {
            throw new IllegalArgumentException("订单不属于当前用户");
        }

        // 2. 校验订单状态是否为待支付(0)
        if (order.getStatus() != 0) {
            throw new IllegalArgumentException("订单状态不是待支付");
        }
        // 3. 更新订单状态为已取消(4)
        order.setStatus(4);
        // 4. 查询订单项, 恢复商品库存
        List<OrderItem> orderItems = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        orderItems.forEach(item -> {
            productService.increaseSales(item.getProductId(), item.getQuantity());
        });
        // 5. 更新订单主表
        orderMapper.updateById(order);
    }

    @Override
    public void payOrder(Long userId, Long orderId) {
        // 1. 查询订单, 校验是否属于当前用户
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (order.getUserId() != userId) {
            throw new IllegalArgumentException("订单不属于当前用户");
        }

        // 2. 校验订单状态是否为待支付(0)
        if (order.getStatus() != 0) {
            throw new IllegalArgumentException("订单状态不是待支付");
        }

        // 3. 更新订单状态为已支付(1)
        order.setStatus(1);
        // 4. 设置支付时间为当前时间
        order.setPayTime(LocalDateTime.now());
        // 5. 更新订单主表
        orderMapper.updateById(order);
    }
}