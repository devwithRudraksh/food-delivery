package com.fooddelivery.dto.request;

import com.fooddelivery.enums.DeliveryStatus;
import lombok.Data;

@Data
public class DeliveryStatusUpdateRequest {
    private DeliveryStatus status;
}
