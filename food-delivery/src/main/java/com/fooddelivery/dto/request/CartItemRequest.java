package com.fooddelivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private Long menuItemId;
    private String itemName;
    private double price;
    private int quantity;
    private Long restaurantId;
}
