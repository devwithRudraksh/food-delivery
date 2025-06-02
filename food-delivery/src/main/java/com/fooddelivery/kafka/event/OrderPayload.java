package com.fooddelivery.kafka.event;
import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.response.OrderItemResponse;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderPayload {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private String restaurantName;
    private List<OrderItemResponse> items;
    private Double totalAmount;
    private String status;
}