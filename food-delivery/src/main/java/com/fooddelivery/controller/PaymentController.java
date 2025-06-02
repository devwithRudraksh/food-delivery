package com.fooddelivery.controller;

import com.fooddelivery.dto.request.WalletPaymentRequest;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final WalletService walletService;


    @PostMapping("/wallet/confirm/{userId}")
    public ResponseEntity<PaymentResponse> confirmWalletPayment(@PathVariable Long userId) {
        PaymentResponse response = walletService.confirmWalletFromCart(userId);
        log.info("✅ Payment response: {}", response);
        return ResponseEntity.ok(response);
    }

}
