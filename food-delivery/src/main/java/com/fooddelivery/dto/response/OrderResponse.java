package com.fooddelivery.dto.response;

import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderTime;
}

