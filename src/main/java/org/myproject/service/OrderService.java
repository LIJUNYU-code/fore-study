package org.myproject.service;

import org.myproject.dto.OrderCreateDTO;
import org.myproject.vo.OrderVO;

import java.util.List;

public interface OrderService {


    /**
     * 创建订单(从购物车结算)
     * @param userId 用户ID
     * @param dto 订单创建DTO
     * @return 订单VO
     */
    OrderVO createOrder(Long userId, OrderCreateDTO dto);

    /**
     * 根据订单ID查询订单详情(包含订单项)
     * @param id 订单ID
     * @return 订单VO
     */
    OrderVO getOrderById(Long id);

    /**
     * 查询用户的订单列表(分页)
     * @param userId 用户ID
     * @param status 订单状态(可选)
     * @param page 页码(默认1)
     * @param size 每页数量(
     *            * @return     * @return 订单VO列表
     */
       List<OrderVO> listUserOrders(Long userId, Integer status, Integer page, Integer size);

       /**
        * 取消订单
        * @param userId 用户ID
        * @param orderId 订单ID
        */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 支付订单(模拟)
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void payOrder(Long userId, Long orderId);
}