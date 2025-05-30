package com.fooddelivery.service.impl;

import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.User;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import com.fooddelivery.exception.InsufficientBalanceException;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = Order.builder()
                .id(1L)
                .user(User.builder().id(1L).build())
                .status(OrderStatus.PLACED)
                .paymentStatus(PaymentStatus.PENDING)
                .totalAmount(300.0)
                .build();
    }

    @Test
    void processPayment_success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // No exception means wallet deduction successful
        doNothing().when(walletService).deductAmount(1L, 300.0);

        PaymentResponse response = orderService.processPayment(1L);

        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        verify(walletService).deductAmount(1L, 300.0);
        verify(orderRepository).save(order);
    }

    @Test
    void processPayment_insufficientBalance() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        doThrow(new InsufficientBalanceException("Insufficient")).when(walletService).deductAmount(1L, 300.0);

        PaymentResponse response = orderService.processPayment(1L);

        assertEquals(OrderStatus.CANCELLED, response.getStatus());
        assertTrue(response.getMessage().toLowerCase().contains("failed"));
        verify(orderRepository).save(order);
    }
}
