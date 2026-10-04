package org.myproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeckillExecuteDTO {

    @NotNull(message = "秒杀活动ID不能为空")
    private Long seckillActivityId;
}