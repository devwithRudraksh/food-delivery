package com.fooddelivery.controller;
import com.fooddelivery.dto.request.MenuItemRequest;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@Slf4j
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> addMenuItem(
            @PathVariable Long restaurantId,
            @RequestBody MenuItemRequest request) {

        log.info("POST request to add menu item to restaurant ID {}", restaurantId);
        MenuItemResponse response = menuItemService.addMenuItem(restaurantId, request);
        return ResponseEntity.status(201).body(response);  // 201 Created
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getMenuItems(
            @PathVariable Long restaurantId) {

        log.info("GET request for menu items of restaurant ID {}", restaurantId);
        List<MenuItemResponse> items = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(items);
    }
}

