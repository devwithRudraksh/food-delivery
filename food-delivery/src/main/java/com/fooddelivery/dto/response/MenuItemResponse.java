package com.fooddelivery.dto.response;

import com.fooddelivery.enums.MenuCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuItemResponse {
    private Long id;
    private String name;
    private Double price;
    private MenuCategory category;
}
