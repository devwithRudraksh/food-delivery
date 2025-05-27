package com.fooddelivery.dto;
import lombok.Data;
@Data
public class UserSignupRequest {
    private String name;
    private String email;
    private String password;
}
