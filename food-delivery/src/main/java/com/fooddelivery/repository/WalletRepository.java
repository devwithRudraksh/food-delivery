package com.fooddelivery.repository;

import com.fooddelivery.entity.Wallet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @EntityGraph(attributePaths = {"user"})
    Optional<Wallet> findById(Long userId);
}
