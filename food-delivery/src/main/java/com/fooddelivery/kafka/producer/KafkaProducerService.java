package com.fooddelivery.kafka.producer;

public interface KafkaProducerService {
    void sendEvent(String topic, String key, String payload);

}
