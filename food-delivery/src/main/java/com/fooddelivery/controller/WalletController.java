package com.fooddelivery.controller;

import com.fooddelivery.dto.response.WalletResponse;
import com.fooddelivery.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWalletBalance(@PathVariable Long userId) {
        double balance = walletService.getWalletByUserId(userId).getBalance();
        return ResponseEntity.ok(new WalletResponse(userId, balance));
    }
}
