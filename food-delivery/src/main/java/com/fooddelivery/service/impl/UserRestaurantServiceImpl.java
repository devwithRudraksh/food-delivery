package com.fooddelivery.service.impl;

import com.fooddelivery.dto.response.RestaurantResponse;
import com.fooddelivery.dto.response.UserResponse;
import com.fooddelivery.dto.response.UserRestaurantResponse;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.UserRestaurantService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Data
@Builder
@RequiredArgsConstructor
@Service
public class UserRestaurantServiceImpl implements UserRestaurantService {
    private final RestaurantRepository restaurantRepository;
    @Override
    public List<UserRestaurantResponse> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream().map(restaurant ->
                UserRestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .address(restaurant.getAddress()).build()

        ).toList();
    }

}
