package com.fooddelivery.dto.request;



import com.fooddelivery.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusRequest {
    private OrderStatus status;
}