package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private Long menuItemId;
    private String itemName;
    private double price;
    private int quantity;
    private Long restaurantId;
}
