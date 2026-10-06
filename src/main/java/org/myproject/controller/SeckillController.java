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
        List<SeckillActivityVO> activityVOList = seckillService.listSeckillActivities();
        return ResultVO.success(activityVOList);
    }

    @GetMapping("/{id}")
    public ResultVO<SeckillActivityVO> getSeckillActivityById(@PathVariable Long id) {
        SeckillActivityVO activityVO = seckillService.getSeckillActivityById(id);
        return ResultVO.success(activityVO);
    }

    @PostMapping("/execute")
    public ResultVO<Long> executeSeckill(@RequestHeader("X-User-Id") Long userId,
                                         @RequestParam Long seckillActivityId) {
        Long result = seckillService.executeSeckill(userId, seckillActivityId);
        return ResultVO.success(result);
    }

    @GetMapping("/result")
    public ResultVO<Long> getSeckillResult(@RequestHeader("X-User-Id") Long userId,
                                           @RequestParam Long seckillActivityId) {
        Long result = seckillService.getSeckillResult(userId, seckillActivityId);
        return ResultVO.success(result);
    }
}