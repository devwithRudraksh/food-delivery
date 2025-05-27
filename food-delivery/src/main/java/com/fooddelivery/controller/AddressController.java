package com.fooddelivery.controller;

import com.fooddelivery.dto.AddressRequest;
import com.fooddelivery.dto.AddressResponse;
import com.fooddelivery.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;
    public AddressController(AddressService addressService){
        this.addressService=addressService;
        log.info("✅ AddressController initialized");
    }
    @PostMapping("/{userId}/address")
    public ResponseEntity<AddressResponse> addAddress(
            @PathVariable Long userId,
            @RequestBody AddressRequest request
    ) {

        System.out.println("🔥 CONTROLLER METHOD HIT");
        log.info("Received request to add address for user: {}", userId);
        AddressResponse response = addressService.addAddress(userId, request);
        return ResponseEntity.ok(response);
    }
}
