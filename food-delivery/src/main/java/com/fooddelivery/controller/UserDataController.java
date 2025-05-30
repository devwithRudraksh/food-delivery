package com.fooddelivery.controller;

import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserDataController {
            private  final OrderService orderService;
    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory(@PathVariable Long userId) {
        List<OrderHistoryResponse> history = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(history);
    }

}
