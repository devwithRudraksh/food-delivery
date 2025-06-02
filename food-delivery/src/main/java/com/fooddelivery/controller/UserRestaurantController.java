package com.fooddelivery.controller;

import com.fooddelivery.dto.response.UserRestaurantResponse;
import com.fooddelivery.service.UserRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/restaurants")
public class UserRestaurantController {
    private final UserRestaurantService userRestaurantService;

    @GetMapping
    public ResponseEntity<List<UserRestaurantResponse>> getRestaurants() {
        List<UserRestaurantResponse> list = userRestaurantService.getRestaurants();
        return ResponseEntity.ok(list);
    }

}
