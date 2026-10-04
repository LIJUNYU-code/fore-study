package org.myproject.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_seckill_order")
public class SeckillOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long seckillActivityId;

    private Long userId;

    private Long orderId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}