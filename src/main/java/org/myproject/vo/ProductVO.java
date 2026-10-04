package org.myproject.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {

    private Long id;

    private String name;

    private Long categoryId;

    private String categoryName;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;

    private Integer sales;

    private String description;

    private String image;

    private Integer status;
}