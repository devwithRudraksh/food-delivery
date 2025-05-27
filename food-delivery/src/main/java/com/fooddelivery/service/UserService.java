package com.fooddelivery.service;

import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.dto.UserResponse;
import com.fooddelivery.dto.UserSignupRequest;

public interface UserService {
    UserResponse registerUser(UserSignupRequest request);
    UserResponse loginUser(LoginRequest request);
}
