package org.myproject.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_seckill_activity")
public class SeckillActivity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private BigDecimal seckillPrice;

    private Integer totalStock;

    private Integer availableStock;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer limitPerUser;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}