package org.myproject.service;

import org.myproject.entity.Cart;

import java.util.List;

public interface CartService {

    // TODO: 添加商品到购物车
    // - 如果该用户已有该商品的购物车记录, 则增加数量
    // - 如果没有, 则新建购物车记录
    void addToCart(Long userId, Long productId, Integer quantity);

    // TODO: 查询用户的购物车列表
    // - 关联查询商品信息(名称、价格、图片、库存)
    List<Cart> listCart(Long userId);

    // TODO: 更新购物车商品数量
    void updateQuantity(Long id, Integer quantity);

    // TODO: 切换购物车项选中状态
    void toggleSelected(Long id);

    // TODO: 全选/全不选
    void selectAll(Long userId, Boolean selected);

    // TODO: 删除购物车项
    void deleteCartItem(Long id);

    // TODO: 清空用户购物车
    void clearCart(Long userId);
}