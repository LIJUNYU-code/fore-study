package org.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.myproject.service.SeckillService;
import org.myproject.vo.ResultVO;
import org.myproject.vo.SeckillActivityVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    @GetMapping("/list")
    public ResultVO<List<SeckillActivityVO>> listSeckillActivities() {
        // TODO: 调用seckillService.listSeckillActivities()
        // 返回 ResultVO.success(activityVOList)
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/{id}")
    public ResultVO<SeckillActivityVO> getSeckillActivityById(@PathVariable Long id) {
        // TODO: 调用seckillService.getSeckillActivityById(id)
        // 返回 ResultVO.success(activityVO)
        throw new UnsupportedOperationException("TODO");
    }

    @PostMapping("/execute")
    public ResultVO<Long> executeSeckill(@RequestHeader("X-User-Id") Long userId,
                                         @RequestParam Long seckillActivityId) {
        // TODO: 调用seckillService.executeSeckill(userId, seckillActivityId)
        // 返回 ResultVO.success(resultId)
        // resultId含义: -1=排队中, >0=订单ID(秒杀成功), 0=秒杀失败
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/result")
    public ResultVO<Long> getSeckillResult(@RequestHeader("X-User-Id") Long userId,
                                           @RequestParam Long seckillActivityId) {
        // TODO: 调用seckillService.getSeckillResult(userId, seckillActivityId)
        // 返回 ResultVO.success(result)
        // result含义: 0=排队中, >0=订单ID(秒杀成功), -1=秒杀失败
        throw new UnsupportedOperationException("TODO");
    }
}