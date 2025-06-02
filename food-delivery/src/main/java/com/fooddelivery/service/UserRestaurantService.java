package com.fooddelivery.service;

import com.fooddelivery.dto.response.UserRestaurantResponse;

import java.util.List;

public interface UserRestaurantService {
   List<UserRestaurantResponse> getRestaurants();
}
