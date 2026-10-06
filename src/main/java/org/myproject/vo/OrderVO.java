package org.myproject.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemVO {

        private Long id;

        private Long productId;

        private String productName;

        private String productImage;

        private BigDecimal price;

        private Integer quantity;
    }
}