package com.fooddelivery.service.impl;

import com.fooddelivery.entity.User;
import com.fooddelivery.entity.Wallet;
import com.fooddelivery.exception.InsufficientBalanceException;
import com.fooddelivery.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 🟢 Required to initialize mocks!
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository; // ✅ Add this!


    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void deductAmount_success() {
        Wallet wallet = Wallet.builder()
                .user(new User())
                .balance(500.0)
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        walletService.deductAmount(1L, 200.0);

        assertEquals(300.0, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void deductAmount_insufficientBalance() {
        Wallet wallet = Wallet.builder()
                .user(new User())
                .balance(100.0)
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientBalanceException.class, () ->
                walletService.deductAmount(1L, 200.0));
    }

    @Test
    void refundAmount_success() {
        Wallet wallet = Wallet.builder()
                .user(new User())
                .balance(300.0)
                .build();

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        walletService.refundAmount(1L, 100.0);

        assertEquals(400.0, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }
}
