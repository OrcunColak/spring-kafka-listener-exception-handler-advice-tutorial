package com.colak.springtutorial.consumer;

import com.colak.springtutorial.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    public static final String TOPIC_NAME = "orders";

    @KafkaListener(topics = TOPIC_NAME)
    public void listen(@Payload Order order) throws MyKafkaException {
        throw new MyKafkaException("MyKafkaException is thrown");
    }

}
