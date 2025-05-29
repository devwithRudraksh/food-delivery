package com.fooddelivery.controller;

import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        log.info("Received order request: {}", request);

        OrderResponse response = orderService.placeOrder(request);

        log.info("Order placed successfully with ID: {}", response.getOrderId());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/pay")
    public ResponseEntity<PaymentResponse> payForOrder(@PathVariable Long id) {
        PaymentResponse response = orderService.processPayment(id);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusRequest request) {

        OrderResponse response = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory(@PathVariable Long userId) {
        List<OrderHistoryResponse> history = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(history);
    }


}
