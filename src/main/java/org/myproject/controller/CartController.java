package org.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.myproject.entity.Cart;
import org.myproject.service.CartService;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResultVO<Void> addToCart(@RequestHeader("X-User-Id") Long userId,
                                    @RequestParam Long productId,
                                    @RequestParam(defaultValue = "1") Integer quantity) {
        // TODO: 调用cartService.addToCart(userId, productId, quantity)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/list")
    public ResultVO<List<Cart>> listCart(@RequestHeader("X-User-Id") Long userId) {
        // TODO: 调用cartService.listCart(userId)
        // 返回 ResultVO.success(cartList)
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/{id}/quantity")
    public ResultVO<Void> updateQuantity(@PathVariable Long id,
                                         @RequestParam Integer quantity) {
        // TODO: 调用cartService.updateQuantity(id, quantity)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/{id}/selected")
    public ResultVO<Void> toggleSelected(@PathVariable Long id) {
        // TODO: 调用cartService.toggleSelected(id)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/select-all")
    public ResultVO<Void> selectAll(@RequestHeader("X-User-Id") Long userId,
                                    @RequestParam Boolean selected) {
        // TODO: 调用cartService.selectAll(userId, selected)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> deleteCartItem(@PathVariable Long id) {
        // TODO: 调用cartService.deleteCartItem(id)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @DeleteMapping("/clear")
    public ResultVO<Void> clearCart(@RequestHeader("X-User-Id") Long userId) {
        // TODO: 调用cartService.clearCart(userId)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }
}