package com.fooddelivery.enums;



public enum OrderStatus {
    CREATED,       // (Optional if you use Outbox pattern)
    PLACED,        // Order received

    CONFIRMED,     // Accepted by restaurant
    PREPARING,

    CANCELLED
}


