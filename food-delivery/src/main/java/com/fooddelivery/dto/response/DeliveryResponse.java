package com.fooddelivery.dto.response;

import com.fooddelivery.enums.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponse {
    private Long orderId;
    private DeliveryStatus status;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
}
