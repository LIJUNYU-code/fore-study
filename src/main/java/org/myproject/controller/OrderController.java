package org.myproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myproject.dto.OrderCreateDTO;
import org.myproject.service.OrderService;
import org.myproject.vo.OrderVO;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResultVO<OrderVO> createOrder(@RequestHeader("X-User-Id") Long userId,
                                         @Valid @RequestBody OrderCreateDTO dto) {
        // TODO: 从请求头获取userId, 调用orderService.createOrder(userId, dto)
        // 注意: 实际项目中userId应从JWT Token中解析, 这里简化处理
        // 返回 ResultVO.success(orderVO)
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/{id}")
    public ResultVO<OrderVO> getOrderById(@PathVariable Long id) {
        // TODO: 调用orderService.getOrderById(id)
        // 返回 ResultVO.success(orderVO)
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/list")
    public ResultVO<List<OrderVO>> listUserOrders(@RequestHeader("X-User-Id") Long userId,
                                                   @RequestParam(required = false) Integer status,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        // TODO: 调用orderService.listUserOrders(userId, status, page, size)
        // 返回 ResultVO.success(orderVOList)
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/{id}/cancel")
    public ResultVO<Void> cancelOrder(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long id) {
        // TODO: 调用orderService.cancelOrder(userId, id)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/{id}/pay")
    public ResultVO<Void> payOrder(@RequestHeader("X-User-Id") Long userId,
                                   @PathVariable Long id) {
        // TODO: 调用orderService.payOrder(userId, id)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }
}