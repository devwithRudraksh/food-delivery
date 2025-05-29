package com.fooddelivery.service;

import com.fooddelivery.dto.request.DeliveryStatusUpdateRequest;
import com.fooddelivery.dto.response.DeliveryResponse;

public interface DeliveryService {
    DeliveryResponse updateDeliveryStatus(Long orderId, DeliveryStatusUpdateRequest request);
}
