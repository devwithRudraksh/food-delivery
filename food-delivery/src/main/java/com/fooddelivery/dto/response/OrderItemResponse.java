package com.fooddelivery.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private String itemName;
    private Long menuItemId;
    private Integer quantity;
    private Double price;
}
