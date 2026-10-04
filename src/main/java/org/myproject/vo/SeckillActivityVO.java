package org.myproject.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SeckillActivityVO {

    private Long id;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal originalPrice;

    private BigDecimal seckillPrice;

    private Integer totalStock;

    private Integer availableStock;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer limitPerUser;
}