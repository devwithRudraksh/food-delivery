package com.fooddelivery.repository.redis;

import com.fooddelivery.dto.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisCartItemRepositoryImpl implements RedisCartItemRepository {

    private final RedisTemplate<String, List<CartItemResponse>> redisTemplate;

    @Override
    public List<CartItemResponse> findCartItem(String userIdKey) {
        return List.of();
    }
}
