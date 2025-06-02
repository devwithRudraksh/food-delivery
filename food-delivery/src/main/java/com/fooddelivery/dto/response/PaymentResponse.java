package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long orderId;
    private Long userId; // ✅ Add this field
    private String restaurantName;
    private double totalAmount;
    private String paymentStatus;
    private String message;
}

