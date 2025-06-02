package com.fooddelivery.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRestaurantResponse {
    private Long id;
    private String name;
    private String address;

}

