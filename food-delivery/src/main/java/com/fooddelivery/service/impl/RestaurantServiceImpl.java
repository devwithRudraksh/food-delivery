package com.fooddelivery.service.impl;

import com.fooddelivery.dto.RestaurantRequest;
import com.fooddelivery.dto.RestaurantResponse;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import com.fooddelivery.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        log.info("Creating restaurant: {}", request.getName());

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Saved restaurant: {}", saved);

        return RestaurantResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .address(saved.getAddress())
                .build();
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants() {
        log.info("Fetching all restaurants");

        return restaurantRepository.findAll().stream().map(r ->
                RestaurantResponse.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .address(r.getAddress())
                        .build()
        ).collect(Collectors.toList());
    }
}
