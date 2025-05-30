package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Wallet;
import com.fooddelivery.exception.InsufficientBalanceException;
import com.fooddelivery.repository.WalletRepository;
import com.fooddelivery.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet getWalletByUserId(Long userId) {

        return walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public void deductAmount(Long userId, double amount) {
        Wallet wallet = getWalletByUserId(userId);
        if (wallet.getBalance() < amount) {
            throw new InsufficientBalanceException("Not enough balance");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
        log.info("Deducted ₹{} from user {}'s wallet. New balance: ₹{}", amount, userId, wallet.getBalance());
    }

    @Override
    public void refundAmount(Long userId, double amount) {
        Wallet wallet = getWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
        log.info("Refunded ₹{} to user {}'s wallet. New balance: ₹{}", amount, userId, wallet.getBalance());
    }
}
