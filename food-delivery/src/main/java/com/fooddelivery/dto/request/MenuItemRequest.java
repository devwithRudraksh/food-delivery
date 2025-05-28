package com.fooddelivery.dto.request;

import com.fooddelivery.enums.MenuCategory;
import lombok.Data;

@Data
public class MenuItemRequest {
    private String name;
    private Double price;
    private MenuCategory category;
}
