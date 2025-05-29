package com.fooddelivery.service.impl;



import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import com.fooddelivery.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_success() {
        // Arrange
        Long userId = 1L;
        Long restaurantId = 2L;
        Long menuItemId = 3L;

        OrderRequest request = new OrderRequest();
        request.setUserId(userId);
        request.setRestaurantId(restaurantId);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(menuItemId);
        itemRequest.setQuantity(2);

        request.setItems(Collections.singletonList(itemRequest));

        User user = User.builder().id(userId).build();
        Restaurant restaurant = Restaurant.builder().id(restaurantId).build();
        MenuItem menuItem = MenuItem.builder().id(menuItemId).price(100.0).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(10L); // mock order ID
            return o;
        });

        // Act
        OrderResponse response = orderService.placeOrder(request);

        // Assert
        assertEquals(10L, response.getOrderId());
        assertEquals(200.0, response.getTotalAmount());
        assertEquals(OrderStatus.PLACED, response.getOrderStatus());
        assertEquals(PaymentStatus.PENDING, response.getPaymentStatus());
    }

    @Test
    void testProcessPayment_successPath() {
        // Arrange
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .status(OrderStatus.PLACED)
                .paymentStatus(PaymentStatus.PENDING)
                .orderTime(LocalDateTime.now())
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        PaymentResponse response = orderService.processPayment(orderId);

        // Assert
        assertNotNull(response);
        assertTrue(response.getStatus() == OrderStatus.CONFIRMED || response.getStatus() == OrderStatus.CANCELLED);
        assertTrue(response.getMessage().contains("Payment"));
    }

    @Test
    void testUpdateOrderStatus_success() {
        // Arrange
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .status(OrderStatus.PLACED)
                .paymentStatus(PaymentStatus.PAID)
                .totalAmount(500.0)
                .orderTime(LocalDateTime.now())
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderStatusRequest statusRequest = new OrderStatusRequest();
        statusRequest.setStatus(OrderStatus.CONFIRMED);

        // Act
        OrderResponse response = orderService.updateOrderStatus(orderId, statusRequest);

        // Assert
        assertEquals(OrderStatus.CONFIRMED, response.getOrderStatus());
        assertEquals(PaymentStatus.PAID, response.getPaymentStatus());
    }
}

