package com.fooddelivery.service.impl;

import com.fooddelivery.dto.request.OrderItemRequest;
import com.fooddelivery.dto.request.OrderStatusRequest;
import com.fooddelivery.dto.response.OrderHistoryResponse;
import com.fooddelivery.dto.response.OrderItemResponse;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.kafka.event.OrderPayload;
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
    public void placeOrderAfterPayment(OrderPayload payload) {
        log.info("Placing order after payment for user ID: {}", payload.getUserId());

        User user = userRepository.findById(payload.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(payload.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = payload.getTotalAmount();

        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .status(OrderStatus.PLACED)
                .paymentStatus(PaymentStatus.PAID)
                .orderTime(LocalDateTime.now())
                .totalAmount(totalAmount)
                .build();

        for (OrderItemResponse itemResponse : payload.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemResponse.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            double itemTotal = menuItem.getPrice() * itemResponse.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .menuItem(menuItem)
                    .quantity(itemResponse.getQuantity())
                    .price(itemTotal)
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        orderRepository.save(order);
        log.info("✅ Order created successfully after payment. Order ID: {}", order.getId());
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
    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        log.info("Updating order {} from status {} to {}", orderId, order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .orderTime(order.getOrderTime())
                .build();
    }
}