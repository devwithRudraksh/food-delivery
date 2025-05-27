package com.fooddelivery.service.impl;

import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.dto.UserSignupRequest;
import com.fooddelivery.dto.UserResponse;
import com.fooddelivery.entity.User;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(UserSignupRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }


        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // We'll hash this later
                .build();

        User saved = userRepository.save(user);
        log.info("User registered successfully: {}", saved.getEmail());
        return UserResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();

    }

    @Override
    public UserResponse loginUser(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())

                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Invalid password attempt for user: {}", request.getEmail());
            throw new RuntimeException("Invalid password");
        }
        log.info("Login successful for user: {}", user.getEmail());

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
