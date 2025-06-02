package com.fooddelivery.controller;

import com.fooddelivery.dto.request.CartItemRequest;
import com.fooddelivery.dto.response.CartItemResponse;
import com.fooddelivery.service.RedisCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final RedisCartService redisCartService;

    /**
     * ➕ Add item to cart
     * RESTful path: /api/cart/{userId}/restaurant/{restaurantId}/add
     */
    @PostMapping("/{userId}/restaurant/{restaurantId}/add")
    public ResponseEntity<String> addMultipleItemsToCart(
            @PathVariable Long userId,
            @PathVariable("restaurantId") Long restaurantId,
            @RequestBody List<CartItemRequest> requests
    ) {
        redisCartService.addMultipleItemsToCart(userId, requests);
        return ResponseEntity.ok("Items added to cart");
    }



    /**
     * 👀 View cart
     */
    @GetMapping("/{userId}/view")
    public ResponseEntity<List<CartItemResponse>> viewCart(@PathVariable Long userId) {
        return ResponseEntity.ok(redisCartService.getCart(userId));
    }

    /**
     * 🧹 Clear cart
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        redisCartService.clearCart(userId);
        return ResponseEntity.ok("🧹 Cart cleared successfully");
    }
}
