package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Wallet;
import com.fooddelivery.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    WalletServiceImpl walletService;

    @Test
    void getWalletByUserId_Success() {
        Wallet wallet = Wallet.builder().userId(1L)
                        .build();

        Mockito.when(walletRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(wallet));

        assertEquals(1L, walletService.getWalletByUserId(1L).getUserId());
    }

}