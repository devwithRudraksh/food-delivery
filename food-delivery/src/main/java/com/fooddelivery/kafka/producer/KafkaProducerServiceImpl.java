package com.fooddelivery.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendEvent(String topic, String key, String payload) {
        kafkaTemplate.send(topic, key, payload);
        log.info("✅ Kafka event sent: topic={}, key={}, payload={}", topic, key, payload);
    }
}

