package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.request.OrderRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.dto.response.OrderItemResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import com.fooddelivery.exception.InsufficientBalanceException;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.WalletService;
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
    private final WalletService walletService;

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
        List<OrderItemResponse> itemResponses = savedOrder.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .itemName(item.getMenuItem().getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .build())
                .toList();
        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .orderStatus(savedOrder.getStatus())
                .paymentStatus(savedOrder.getPaymentStatus())
                .orderTime(savedOrder.getOrderTime())
                .items(itemResponses)
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

        try {
            // ✅ Try deducting wallet
            walletService.deductAmount(order.getUser().getId(), order.getTotalAmount());
            order.setStatus(OrderStatus.CONFIRMED);
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);

            log.info("Payment successful, wallet deducted. Order ID: {}", order.getId());

            return PaymentResponse.builder()
                    .orderId(order.getId())
                    .status(order.getStatus())
                    .message("Payment successful via wallet")
                    .build();

        } catch (InsufficientBalanceException e) {
            // ❌ Handle failure gracefully
            order.setStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.FAILED);
            orderRepository.save(order);

            log.warn("Payment failed for order {} due to: {}", order.getId(), e.getMessage());

            return PaymentResponse.builder()
                    .orderId(order.getId())
                    .status(order.getStatus())
                    .message("Payment failed: " + e.getMessage())
                    .build();
        }
    }


    @Override
    public List<OrderHistoryResponse> getOrdersByUserId(Long userId) {
        log.info("orderHistory for user id {}",userId);
        List<Order> orders = orderRepository.findByUserIdOrderByOrderTimeDesc(userId);
          log.info("Orders found {} for user id :{}",orders.size(),userId);
        return orders.stream()
                .map(order -> {
                    // 🧾 Convert OrderItem -> OrderItemResponse
                    List<OrderItemResponse> itemResponses = order.getItems().stream()
                            .map(item -> OrderItemResponse.builder()
                                    .itemName(item.getMenuItem().getName())
                                    .quantity(item.getQuantity())
                                    .price(item.getPrice())
                                    .build())
                            .toList();
                    log.info("🧾 Order ID: {} | Items: {} | Total: {}",
                            order.getId(), itemResponses.size(), order.getTotalAmount());
                    return OrderHistoryResponse.builder()
                            .orderId(order.getId())
                            .totalAmount(order.getTotalAmount())
                            .status(order.getStatus())
                            .paymentStatus(order.getPaymentStatus())
                            .orderTime(order.getOrderTime())
                            .restaurantName(order.getRestaurant() != null ? order.getRestaurant().getName() : null)
                            .items(itemResponses) // ✅ NEW
                            .build();
                })
                .toList();
    }
}
