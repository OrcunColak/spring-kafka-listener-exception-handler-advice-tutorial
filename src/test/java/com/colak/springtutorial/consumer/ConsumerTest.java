package com.colak.springtutorial.consumer;

import com.colak.springtutorial.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.TimeUnit;

@SpringBootTest(properties = {
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@EmbeddedKafka(partitions = 1, topics = { Consumer.TOPIC_NAME })
class ConsumerTest {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @Test
    void testInvalidMessage() throws InterruptedException {
        Order order = new Order();
        order.setName("order1");
        order.setPrice(10);
        kafkaTemplate.send(Consumer.TOPIC_NAME, order);

        TimeUnit.SECONDS.sleep(10);
    }

}