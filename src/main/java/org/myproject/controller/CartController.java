package org.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.myproject.entity.Cart;
import org.myproject.service.CartService;
import org.myproject.vo.CartVO;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResultVO<Cart> addToCart(@RequestHeader("X-User-Id") Long userId,
                                    @RequestParam Long productId,
                                    @RequestParam(defaultValue = "1") Integer quantity) {
        Cart cart = cartService.addToCart(userId, productId, quantity);
        return ResultVO.success(cart);
    }

    @GetMapping("/list")
    public ResultVO<List<CartVO>> listCart(@RequestHeader("X-User-Id") Long userId) {
        List<CartVO> cartList = cartService.listCart(userId);
        return ResultVO.success(cartList);
    }

    @PutMapping("/{id}/quantity")
    public ResultVO<Void> updateQuantity(@PathVariable Long id,
                                         @RequestParam Integer quantity) {
        cartService.updateQuantity(id, quantity);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }

    @PutMapping("/{id}/selected")
    public ResultVO<Void> toggleSelected(@PathVariable Long id) {
        cartService.toggleSelected(id);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }

    @PutMapping("/select-all")
    public ResultVO<Void> selectAll(@RequestHeader("X-User-Id") Long userId,
                                    @RequestParam Boolean selected) {
        cartService.selectAll(userId, selected);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> deleteCartItem(@PathVariable Long id) {
        
        cartService.deleteCartItem(id);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }

    @DeleteMapping("/clear")
    public ResultVO<Void> clearCart(@RequestHeader("X-User-Id") Long userId) {
        cartService.clearCart(userId);
        return ResultVO.success();
    }
}