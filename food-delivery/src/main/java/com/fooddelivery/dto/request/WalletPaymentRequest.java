package com.fooddelivery.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class WalletPaymentRequest {
    private Long restaurantId;
    private List<OrderItemRequest> items;
}
