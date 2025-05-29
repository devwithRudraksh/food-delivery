package com.fooddelivery.entity;

import com.fooddelivery.enums.DeliveryStatus;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // From which restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // Status of order: PLACED, PREPARING, DELIVERED, CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;



    // Time when order was placed
    private LocalDateTime orderTime;

    // Total amount for the order
    private Double totalAmount;

    // All items inside the order (1 order has many items)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
