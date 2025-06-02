package com.fooddelivery.repository.redis;

import com.fooddelivery.dto.response.CartItemResponse;

import java.util.List;

public interface RedisCartItemRepository {
    List<CartItemResponse> findCartItem(String userIdKey);
}
