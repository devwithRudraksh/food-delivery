package com.fooddelivery.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.dto.request.CartItemRequest;
import com.fooddelivery.dto.response.CartItemResponse;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.service.RedisCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCartServiceImpl implements RedisCartService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MenuItemRepository menuItemRepository;



    @Override
    public void addMultipleItemsToCart(Long userId, List<CartItemRequest> requests) {
        String cartKey = getCartKey(userId);

        // 🧠 Check existing cart
        Map<Object, Object> existingItems = redisTemplate.opsForHash().entries(cartKey);

        Long existingRestaurantId = null;
        if (!existingItems.isEmpty()) {
            Object firstItemJson = existingItems.values().iterator().next();
            try {
                CartItemResponse firstItem = objectMapper.readValue(firstItemJson.toString(), CartItemResponse.class);
                existingRestaurantId = firstItem.getRestaurantId();
            } catch (JsonProcessingException e) {
                log.error("Error reading existing cart item for user {}: {}", userId, e.getMessage());
                throw new RuntimeException("Corrupted cart data");
            }
        }

        for (CartItemRequest request : requests) {
            // ✅ Enforce restaurant constraint
            if (existingRestaurantId != null && !existingRestaurantId.equals(request.getRestaurantId())) {
                throw new IllegalArgumentException("Cart contains items from another restaurant. Please clear the cart first.");
            }

            MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

            CartItemResponse item = CartItemResponse.builder()
                    .menuItemId(menuItem.getId())
                    .itemName(menuItem.getName())
                    .price(menuItem.getPrice())
                    .quantity(request.getQuantity())
                    .restaurantId(request.getRestaurantId())
                    .build();

            try {
                redisTemplate.opsForHash().put(cartKey,
                        String.valueOf(menuItem.getId()),
                        objectMapper.writeValueAsString(item));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to write cart item to Redis", e);
            }
        }
    }

    private String getCartKey(Long userId) {
        return "cart:user:" + userId;
    }
//    public void addToCart(Long userId, Long restaurantId, CartItemRequest request) {
//        log.info("🛒 Adding to cart: userId={}, item={}", userId, request);
//
//        String cartKey = "cart:" + userId;
//        String restaurantKey = cartKey + ":restaurantId";
//
//        String existingRestaurantId = redisTemplate.opsForValue().get(restaurantKey);
//        if (existingRestaurantId != null && !existingRestaurantId.equals(String.valueOf(restaurantId))) {
//            throw new RuntimeException("❌ You can only add items from one restaurant. Clear cart to add from another.");
//        }
//
//        if (existingRestaurantId == null) {
//            redisTemplate.opsForValue().set(restaurantKey, String.valueOf(restaurantId));
//        }
//
//        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
//                .orElseThrow(() -> new RuntimeException("Menu item not found"));
//
//        double price = menuItem.getPrice() * request.getQuantity();
//
//        CartItemResponse item = CartItemResponse.builder()
//                .itemName(menuItem.getName())
//                .quantity(request.getQuantity())
//                .price(price)
//                .menuItemId(request.getMenuItemId())
//                .build();
//
//        try {
//            redisTemplate.opsForHash().put(cartKey, String.valueOf(request.getMenuItemId()), objectMapper.writeValueAsString(item));
//            log.info("✅ Item added to cart for user {}: {}", userId, item);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to add item to cart", e);
//        }
//    }

    @Override
    public List<CartItemResponse> getCart(Long userId) {
        String cartKey = "cart:user:" + userId;  // ✅ FIXED key
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey);

        return entries.values().stream().map(val -> {
            try {
                return objectMapper.readValue(val.toString(), CartItemResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse cart item", e);
            }
        }).collect(Collectors.toList());
    }


    @Override
    public void clearCart(Long userId) {
        String cartKey = "cart:" + userId;
        redisTemplate.delete(cartKey);
        redisTemplate.delete(cartKey + ":restaurantId");
        log.info("🧹 Cleared cart for user {}", userId);
    }

    @Override
    public String getRestaurantIdFromCart(Long userId) {
        String restaurantKey = "cart:" + userId + ":restaurantId";
        return redisTemplate.opsForValue().get(restaurantKey);
    }
}