package org.myproject.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {

    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private LocalDateTime payTime;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

    private List<OrderItemVO> orderItems;

    @Data
    public static class OrderItemVO {

        private Long id;

        private Long productId;

        private String productName;

        private String productImage;

        private BigDecimal price;

        private Integer quantity;
    }
}