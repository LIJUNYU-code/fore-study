package org.myproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.myproject.entity.Cart;
import org.myproject.entity.Product;
import org.myproject.mapper.CartMapper;
import org.myproject.mapper.ProductMapper;
import org.myproject.service.CartService;
import org.myproject.vo.CartVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Override
    public Cart addToCart(Long userId, Long productId, Integer quantity) {

        // 1. 查询该用户是否已有该商品的购物车记录
        Cart existingCart = cartMapper.selectByUserIdAndProductId(userId, productId);

        // 2. 如果存在, 更新数量 = 原数量 + quantity
        if (existingCart != null) {
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            cartMapper.updateById(existingCart);
            return existingCart;
        }

        // 3. 如果不存在, 新建购物车记录
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setProductId(productId);
        newCart.setQuantity(quantity);
        newCart.setSelected(0);
        cartMapper.insert(newCart);
        return newCart;
    }

    @Override
    public List<CartVO> listCart(Long userId) {

        // 1. 根据userId查询购物车列表
        List<Cart> cartList = cartMapper.selectList(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));

        // 2. 转换为CartVO，关联查询商品信息
        return cartList.stream().map(cart -> {
            Product product = productMapper.selectById(cart.getProductId());

            return CartVO.builder()
                    .id(cart.getId())
                    .userId(cart.getUserId())
                    .productId(cart.getProductId())
                    .productName(product != null ? product.getName() : "未知商品")
                    .price(product != null ? product.getPrice() : java.math.BigDecimal.ZERO)
                    .productImage(product != null ? product.getImage() : "")
                    .quantity(cart.getQuantity())
                    .selected(cart.getSelected())
                    .createTime(cart.getCreateTime())
                    .updateTime(cart.getUpdateTime())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void updateQuantity(Long id, Integer quantity) {
        // 1. 校验购物车项是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new IllegalArgumentException("购物车项不存在");
        }

        // 2. 如果quantity <= 0, 删除该购物车项
        if (quantity <= 0) {
            cartMapper.deleteById(id);
            return;
        }

        // 3. 否则更新数量
        cart.setQuantity(quantity);
        cartMapper.updateById(cart);
    }

    @Override
    public void toggleSelected(Long id) {
        // 1. 查询当前选中状态
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new IllegalArgumentException("购物车项不存在");
        }

        // 2. 取反: 0→1 或 1→0
        cart.setSelected(cart.getSelected() == 0 ? 1 : 0);

        // 3. 更新
        cartMapper.updateById(cart);
    }

    @Override
    public void selectAll(Long userId, Boolean selected) {

        Cart updateCart = new Cart();
        updateCart.setSelected(selected ? 1 : 0);

        cartMapper.update(updateCart, new LambdaUpdateWrapper<Cart>()
                .eq(Cart::getUserId, userId));
    }

    @Override
    public void deleteCartItem(Long id) {

        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new IllegalArgumentException("购物车项不存在");
        }

        cartMapper.deleteById(id);
    }

    @Override
    public void clearCart(Long userId) {

        cartMapper.delete(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId));
    }
}