package com.fooddelivery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    private Long userId;

    private double balance;

    @OneToOne
    @MapsId
    private User user;
}
