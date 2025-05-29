package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.MenuItemRequest;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.MenuCategory;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public MenuItemResponse addMenuItem(Long restaurantId, MenuItemRequest request) {
        log.info("Adding menu item to restaurant ID: {}", restaurantId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        MenuItem item = MenuItem.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .restaurant(restaurant)
                .build();

        MenuItem saved = menuItemRepository.save(item);
        log.info("Saved menu item: {}", saved);

        return MenuItemResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .price(saved.getPrice())
                .category(saved.getCategory())
                .build();
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {
        log.info("Fetching menu items for restaurant ID: {}", restaurantId);

        return menuItemRepository.findAll().stream()
                .filter(i -> i.getRestaurant().getId().equals(restaurantId))
                .map(item -> MenuItemResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .category(item.getCategory())
                        .build())
                .collect(Collectors.toList());
    }
}
