package com.fooddelivery.service;

import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.dto.response.PaymentResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
    OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request);
    PaymentResponse processPayment(Long orderId);
    List<OrderHistoryResponse> getOrdersByUserId(Long userId);


}
