package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
