package com.fooddelivery.service;

import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.entity.Wallet;

import java.util.List;

public interface WalletService {

    Wallet getWalletByUserId(Long userId);

    void deductAmount(Long userId, double amount);

    void refundAmount(Long userId, double amount);

    PaymentResponse confirmWalletFromCart(Long userId);

}
