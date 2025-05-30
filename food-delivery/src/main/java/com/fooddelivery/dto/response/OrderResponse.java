package com.fooddelivery.dto.response;

import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderTime;
    private List<OrderItemResponse> items; //added later for retrieving orderitems
}

