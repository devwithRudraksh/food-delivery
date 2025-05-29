package com.fooddelivery.service.impl;
import com.fooddelivery.dto.request.AddressRequest;
import com.fooddelivery.dto.request.MenuItemRequest;
import com.fooddelivery.dto.response.AddressResponse;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.entity.Address;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.MenuCategory;
import com.fooddelivery.repository.AddressRepository;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class MenuItemServiceImplTest {

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Test
    void testAddMenuItem() {
        Long restaurantId = 1L;

        MenuItemRequest request = new MenuItemRequest();
        request.setName("Pasta");
        request.setPrice(250.0);
        request.setCategory(MenuCategory.MAIN_COURSE);

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Testaurant")
                .build();

        MenuItem savedItem = MenuItem.builder()
                .id(1L)
                .name("Pasta")
                .price(250.0)
                .category(MenuCategory.MAIN_COURSE)
                .restaurant(restaurant)
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedItem);

        MenuItemResponse response = menuItemService.addMenuItem(restaurantId, request);

        assertEquals("Pasta", response.getName());
        assertEquals(250.0, response.getPrice());
        assertEquals(MenuCategory.MAIN_COURSE, response.getCategory());
        assertEquals(1L, response.getId());
    }
}

