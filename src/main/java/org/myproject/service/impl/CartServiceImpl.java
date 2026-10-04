package org.myproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.myproject.entity.Cart;
import org.myproject.mapper.CartMapper;
import org.myproject.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    @Override
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // TODO: 实现添加商品到购物车
        // 1. 查询该用户是否已有该商品的购物车记录
        //    SELECT * FROM t_cart WHERE user_id = #{userId} AND product_id = #{productId}
        // 2. 如果存在, 更新数量 = 原数量 + quantity
        // 3. 如果不存在, 新建购物车记录
        throw new UnsupportedOperationException("TODO: 请实现添加商品到购物车");
    }

    @Override
    public List<Cart> listCart(Long userId) {
        // TODO: 实现查询购物车列表
        // 1. 根据userId查询购物车列表
        // 2. (可选) 关联查询商品信息
        throw new UnsupportedOperationException("TODO: 请实现查询购物车列表");
    }

    @Override
    public void updateQuantity(Long id, Integer quantity) {
        // TODO: 实现更新购物车数量
        // 1. 校验购物车项是否存在
        // 2. 如果quantity <= 0, 删除该购物车项
        // 3. 否则更新数量
        throw new UnsupportedOperationException("TODO: 请实现更新购物车数量");
    }

    @Override
    public void toggleSelected(Long id) {
        // TODO: 实现切换选中状态
        // 1. 查询当前选中状态
        // 2. 取反: 0→1 或 1→0
        // 3. 更新
        throw new UnsupportedOperationException("TODO: 请实现切换选中状态");
    }

    @Override
    public void selectAll(Long userId, Boolean selected) {
        // TODO: 实现全选/全不选
        // UPDATE t_cart SET selected = #{selected} WHERE user_id = #{userId}
        throw new UnsupportedOperationException("TODO: 请实现全选/全不选");
    }

    @Override
    public void deleteCartItem(Long id) {
        // TODO: 实现删除购物车项
        // cartMapper.deleteById(id)
        throw new UnsupportedOperationException("TODO: 请实现删除购物车项");
    }

    @Override
    public void clearCart(Long userId) {
        // TODO: 实现清空购物车
        // DELETE FROM t_cart WHERE user_id = #{userId}
        throw new UnsupportedOperationException("TODO: 请实现清空购物车");
    }
}