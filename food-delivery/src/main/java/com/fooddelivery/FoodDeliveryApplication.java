package com.fooddelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodDeliveryApplication {

	public static void main(String[] args) {

		SpringApplication.run(FoodDeliveryApplication.class, args);
		System.out.println("✅ Food Delivery App has started successfully on port 8080 🍕");

	}
}
