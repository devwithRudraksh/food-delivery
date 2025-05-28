package com.fooddelivery.service;

import com.fooddelivery.dto.request.RestaurantRequest;
import com.fooddelivery.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {
    RestaurantResponse createRestaurant(RestaurantRequest request);
    List<RestaurantResponse> getAllRestaurants();
}
