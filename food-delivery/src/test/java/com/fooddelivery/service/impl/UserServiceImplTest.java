package com.fooddelivery.service.impl;
import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.dto.UserSignupRequest;
import com.fooddelivery.entity.User;
import com.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserSignupRequest request = new UserSignupRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("123456");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> userService.registerUser(request));
    }

    @Test
    public void testLoginUser_Success() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("123456")
                .build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("123456");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        var response = userService.loginUser(loginRequest);
        assertEquals("john@example.com", response.getEmail());
    }
}
