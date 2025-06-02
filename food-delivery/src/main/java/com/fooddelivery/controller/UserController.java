package com.fooddelivery.controller;

import com.fooddelivery.dto.request.LoginRequest;
import com.fooddelivery.dto.request.UserSignupRequest;
import com.fooddelivery.dto.response.UserResponse;
import com.fooddelivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")

public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserSignupRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);

    }
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

}
