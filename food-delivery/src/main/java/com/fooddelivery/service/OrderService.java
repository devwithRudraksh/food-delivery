package com.fooddelivery.service;

import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.kafka.event.OrderPayload;

import java.util.List;

public interface OrderService {
    void placeOrderAfterPayment(OrderPayload payload);
    OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request);
    List<OrderHistoryResponse> getOrdersByUserId(Long userId);



}
