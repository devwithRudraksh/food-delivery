package com.fooddelivery.dto.response;

import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponse {
    private Long orderId;
    private Double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private PaymentStatus paymentStatus;
    private String restaurantName;
}

