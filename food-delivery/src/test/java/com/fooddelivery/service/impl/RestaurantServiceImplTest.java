package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.RestaurantRequest;
import com.fooddelivery.dto.response.RestaurantResponse;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    void testCreateRestaurant() {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("Testaurant");
        request.setAddress("Test Street");

        Restaurant saved = Restaurant.builder()
                .id(1L)
                .name("Testaurant")
                .address("Test Street")
                .build();

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);

        RestaurantResponse response = restaurantService.createRestaurant(request);

        assertEquals("Testaurant", response.getName());
        assertEquals("Test Street", response.getAddress());
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetAllRestaurants() {
        List<Restaurant> mockList = List.of(
                new Restaurant(1L, "A", "Addr A", new ArrayList<>()),
                new Restaurant(2L, "B", "Addr B", new ArrayList<>())
        );

        when(restaurantRepository.findAll()).thenReturn(mockList);

        List<RestaurantResponse> responses = restaurantService.getAllRestaurants();

        assertEquals(2, responses.size());
        assertEquals("A", responses.get(0).getName());
    }
}

