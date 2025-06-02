package com.fooddelivery.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.dto.request.CartItemRequest;
import com.fooddelivery.dto.response.CartItemResponse;
import com.fooddelivery.dto.response.OrderItemResponse;
import com.fooddelivery.dto.response.PaymentResponse;
import com.fooddelivery.entity.*;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import com.fooddelivery.exception.InsufficientBalanceException;
import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.kafka.event.OrderPayload;
import com.fooddelivery.kafka.producer.KafkaProducerService;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.RedisCartService;
import com.fooddelivery.service.WalletService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final KafkaProducerService kafkaProducerService;
    private final RedisCartService redisCartService;

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
        log.info("💰 Deducted ₹{} from user {}. New balance: ₹{}", amount, userId, wallet.getBalance());
    }

    @Override
    public PaymentResponse confirmWalletFromCart(Long userId) {
        try {
            log.info("🧾 Starting wallet payment flow for user {}", userId);
            String cartKey = "cart:" + userId;

            // 1. Fetch cart items from Redis
//            List<Object> rawCartItems = redisTemplate.opsForList().range(cartKey, 0, -1);
//            List<CartItemResponse> items = new ArrayList<>();
//            ObjectMapper mapper = new ObjectMapper(); // make this a @Component bean ideally
//
//            for (Object obj : rawCartItems) {
//                String json = (String) obj; // Redis stores it as String
//                CartItemResponse item = mapper.readValue(json, CartItemResponse.class); // Deserialize
//                items.add(item);
//            }
            List<CartItemResponse> items = redisCartService.getCart(userId);

            if (items.isEmpty()) throw new RuntimeException("Cart is empty");

            // 2. Get restaurantId from Redis
            // ✅ Get restaurantId directly from first item
            Long restaurantId= items.stream()
                    .map(CartItemResponse::getRestaurantId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("❌ Invalid or missing restaurantId in Redis for user: \" + userId"));

            log.info("📦 Fetched restaurant ID from cart item: {}", restaurantId);

            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // 3. Calculate total and build order items
            double totalAmount = 0;
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItemResponse item : items) {
                MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                        .orElseThrow(() -> new RuntimeException("MenuItem not found: " + item.getMenuItemId()));
                double itemTotal = menuItem.getPrice() * item.getQuantity();
                totalAmount += itemTotal;
                log.info("🧾 Item: {} | Qty: {} | Price: {} | Total: {}", menuItem.getName(), item.getQuantity(), menuItem.getPrice(), itemTotal);

                orderItems.add(OrderItem.builder()
                        .menuItem(menuItem)
                        .quantity(item.getQuantity())
                        .price(menuItem.getPrice())
                        .build());
            }

            // 4. Deduct wallet
            deductAmount(userId, totalAmount);

            // 5. Create and save order
            Order order = Order.builder()
                    .user(User.builder().id(userId).build())
                    .restaurant(restaurant)
                    .status(OrderStatus.PLACED)
                    .paymentStatus(PaymentStatus.PAID)
                    .orderTime(LocalDateTime.now())
                    .totalAmount(totalAmount)
                    .build();

            Order savedOrder = orderRepository.save(order);
            log.info("📦 Order saved: ID={} | Total={}", savedOrder.getId(), savedOrder.getTotalAmount());

            for (OrderItem item : orderItems) {
                item.setOrder(savedOrder);
            }
            orderItemRepository.saveAll(orderItems);
//KafkaEvent
            OrderPayload payload = OrderPayload.builder()
                    .orderId(savedOrder.getId())
                    .userId(userId)
                    .restaurantId(restaurant.getId())
                    .restaurantName(restaurant.getName())
                    .items(orderItems.stream().map(item -> OrderItemResponse.builder()
                            .itemName(item.getMenuItem().getName())
                            .menuItemId(item.getMenuItem().getId())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .build()).toList())
                    .totalAmount(totalAmount)
                    .status("PLACED")
                    .build();

            kafkaProducerService.sendEvent("order-placed", String.valueOf(savedOrder.getId()), new ObjectMapper().writeValueAsString(payload));

//
//            OrderEvent event = new OrderEvent(savedOrder.getId(), userId, "PLACED");
//            kafkaProducerService.sendEvent("order-placed", String.valueOf(savedOrder.getId()), event.toString());
            log.info("📤 Kafka event published for Order ID {}", savedOrder.getId());

            // 7. Clear Redis cart
            redisCartService.clearCart(userId);
            log.info("🧹 Cleared Redis cart and restaurant keys for user {}", userId);

            // 8. Return response
            return PaymentResponse.builder()
                    .orderId(savedOrder.getId())
                    .userId(userId)
                    .restaurantName(restaurant.getName())
                    .totalAmount(totalAmount)
                    .paymentStatus("SUCCESS")
                    .message("✅ Order placed and wallet deducted")
                    .build();

        } catch (Exception e) {
            log.error("❌ Error placing order for user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error placing order: " + e.getMessage(), e);
        }
    }

    @Override
    public void refundAmount(Long userId, double amount) {
        Wallet wallet = getWalletByUserId(userId);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
        log.info("↩️ Refunded ₹{} to user {}. New balance: ₹{}", amount, userId, wallet.getBalance());
    }

}
