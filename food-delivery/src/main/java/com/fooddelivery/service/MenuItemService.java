package com.fooddelivery.service;

import com.fooddelivery.dto.request.MenuItemRequest;
import com.fooddelivery.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    MenuItemResponse addMenuItem(Long restaurantId, MenuItemRequest request);
    List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId);
}
