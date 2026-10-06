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

        // 注意: 实际项目中userId应从JWT Token中解析, 这里简化处理
        OrderVO orderVO = orderService.createOrder(userId, dto);

        // 返回 ResultVO.success(orderVO);
        return ResultVO.success(orderVO);
    }

    @GetMapping("/{id}")
    public ResultVO<OrderVO> getOrderById(@PathVariable Long id) {
        // 返回 ResultVO.success(orderVO)
        OrderVO orderVO = orderService.getOrderById(id);
        return ResultVO.success(orderVO);
    }

    @GetMapping("/list")
    public ResultVO<List<OrderVO>> listUserOrders(@RequestHeader("X-User-Id") Long userId,
                                                   @RequestParam(required = false) Integer status,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size) {

        // 返回 ResultVO.success(orderVOList)
        List<OrderVO> orderVOList = orderService.listUserOrders(userId, status, page, size);
        return ResultVO.success(orderVOList);
    }

    @PutMapping("/{id}/cancel")
    public ResultVO<Void> cancelOrder(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long id) {
        orderService.cancelOrder(userId, id);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }

    @PutMapping("/{id}/pay")
    public ResultVO<Void> payOrder(@RequestHeader("X-User-Id") Long userId,
                                   @PathVariable Long id) {
        orderService.payOrder(userId, id);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }
}