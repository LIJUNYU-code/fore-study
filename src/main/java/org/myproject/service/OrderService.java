package org.myproject.service;

import org.myproject.dto.OrderCreateDTO;
import org.myproject.entity.Cart;
import org.myproject.vo.OrderVO;

import java.util.List;

public interface OrderService {

    // TODO: 创建订单(从购物车结算)
    // - 查询选中的购物车项
    // - 校验商品库存是否充足
    // - 扣减商品库存
    // - 生成订单编号(雪花算法)
    // - 计算订单总金额
    // - 创建订单主表和订单项
    // - 清除已结算的购物车项
    // - 返回订单信息
    OrderVO createOrder(Long userId, OrderCreateDTO dto);

    // TODO: 根据订单ID查询订单详情(包含订单项)
    OrderVO getOrderById(Long id);

    // TODO: 查询用户的订单列表(分页)
    List<OrderVO> listUserOrders(Long userId, Integer status, Integer page, Integer size);

    // TODO: 取消订单
    // - 校验订单状态是否为待支付
    // - 更新订单状态为已取消
    // - 恢复商品库存
    void cancelOrder(Long userId, Long orderId);

    // TODO: 支付订单(模拟)
    // - 校验订单状态是否为待支付
    // - 更新订单状态为已支付
    // - 记录支付时间
    void payOrder(Long userId, Long orderId);
}