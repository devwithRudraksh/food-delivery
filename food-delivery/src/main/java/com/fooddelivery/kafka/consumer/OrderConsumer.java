package com.fooddelivery.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.kafka.event.OrderEvent;
import com.fooddelivery.kafka.event.OrderPayload;
import com.fooddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-placed", groupId = "order")
    public void consume(String message) throws JsonProcessingException {
        OrderPayload payload = objectMapper.readValue(message, OrderPayload.class);

        log.info("📦 Received order with ID: {}", payload.getOrderId());
        // handle delivery etc.
        orderService.placeOrderAfterPayment(payload);

    }

}
