package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        log.info("Placing order for user ID: {}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .status(OrderStatus.PLACED)
                .paymentStatus(PaymentStatus.PENDING)  //
                .orderTime(LocalDateTime.now())
                .build();

        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            double itemTotal = menuItem.getPrice() * itemRequest.getQuantity();
            totalAmount += itemTotal;

            OrderItem orderItem = OrderItem.builder()
                    .menuItem(menuItem)
                    .quantity(itemRequest.getQuantity())
                    .price(itemTotal)
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .orderStatus(savedOrder.getStatus())
                .paymentStatus(savedOrder.getPaymentStatus())  // <---- NEW
                .orderTime(savedOrder.getOrderTime())
                .build();
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        log.info("Updating status of order {} from {} to {}", order.getId(), order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getStatus())
                .paymentStatus(order.getPaymentStatus())  // <---- NEW
                .orderTime(order.getOrderTime())
                .build();
    }

    @Override
    public PaymentResponse processPayment(Long orderId) {
        log.info("Received request to process payment for order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        log.info("Fetched order with current status: {}", order.getStatus());

        if (order.getStatus() != OrderStatus.PLACED) {
            return PaymentResponse.builder()
                    .orderId(order.getId())
                    .status(order.getStatus())
                    .message("Payment not allowed in current status.")
                    .build();
        }

        boolean success = Math.random() > 0.2;
        log.info("Simulated payment result: {}", success ? "SUCCESS" : "FAILED");

        OrderStatus newOrderStatus = success ? OrderStatus.CONFIRMED : OrderStatus.CANCELLED;
        PaymentStatus newPaymentStatus = success ? PaymentStatus.PAID : PaymentStatus.FAILED;

        order.setStatus(newOrderStatus);
        order.setPaymentStatus(newPaymentStatus);

        orderRepository.save(order);
        log.info("Updated order status to: {} and payment status to: {}", newOrderStatus, newPaymentStatus);

        return PaymentResponse.builder()
                .orderId(order.getId())
                .status(newOrderStatus)
                .message(success ? "Payment Successful" : "Payment Failed")
                .build();
    }

    @Override
    public List<OrderHistoryResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(order -> OrderHistoryResponse.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .paymentStatus(order.getPaymentStatus())
                        .orderTime(order.getOrderTime())
                        .restaurantName(order.getRestaurant() != null ? order.getRestaurant().getName() : null) // FIXED
                        .build())
                .toList();
    }
}
