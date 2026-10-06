package org.myproject.service;

import org.myproject.entity.Cart;
import org.myproject.vo.CartVO;

import java.util.List;

public interface CartService {


    /**
     * 添加商品到购物车
     * - 如果该用户已有该商品的购物车记录, 则增加数量
     * - 如果没有, 则新建购物车记录
     * @return 购物车项对象（包含ID）
     */
    Cart addToCart(Long userId, Long productId, Integer quantity);

    /**
     * 查询用户的购物车列表
     * - 关联查询商品信息(名称、价格、图片、库存)
     * @return 购物车VO列表（包含商品信息）
     */
    List<CartVO> listCart(Long userId);

    /**
     * 更新购物车商品数量
     * - 如果quantity <= 0, 删除该购物车项
     * - 否则更新数量
     */
    void updateQuantity(Long id, Integer quantity);

    /**
     * 切换购物车项选中状态
     */
    void toggleSelected(Long id);

    /**
     * 全选/全不选
     */
    void selectAll(Long userId, Boolean selected);


    /**
     * 删除购物车项
     */
    void deleteCartItem(Long id);

    /**
     * 清空用户购物车
     */
    void clearCart(Long userId);
}