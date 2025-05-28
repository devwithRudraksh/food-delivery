package com.fooddelivery.service;

import com.fooddelivery.dto.request.LoginRequest;
import com.fooddelivery.dto.response.UserResponse;
import com.fooddelivery.dto.request.UserSignupRequest;

public interface UserService {
    UserResponse registerUser(UserSignupRequest request);
    UserResponse loginUser(LoginRequest request);
}
