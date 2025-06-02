package com.fooddelivery.service;
import com.fooddelivery.dto.request.CartItemRequest;
import com.fooddelivery.dto.response.CartItemResponse;
import com.fooddelivery.dto.response.OrderItemResponse;

import java.util.List;

public interface RedisCartService {
    //void addToCart(Long userId, Long restaurantId, CartItemRequest request);
    void addMultipleItemsToCart(Long userId, List<CartItemRequest> requests);

    List<CartItemResponse> getCart(Long userId);
    void clearCart(Long userId);
    String getRestaurantIdFromCart(Long userId);


}
