package org.myproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    @NotNull(message = "购物车项ID列表不能为空")
    private List<Long> cartItemIds;

    private String remark;
}