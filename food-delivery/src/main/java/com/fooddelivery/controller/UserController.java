package com.fooddelivery.controller;

import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.dto.UserSignupRequest;
import com.fooddelivery.dto.UserResponse;
import com.fooddelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserSignupRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);

    }
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

}
