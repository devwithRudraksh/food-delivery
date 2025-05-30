package com.fooddelivery.service;

import com.fooddelivery.entity.Wallet;

public interface WalletService {
    Wallet getWalletByUserId(Long userId);
    void deductAmount(Long userId, double amount);
    void refundAmount(Long userId, double amount);
}
