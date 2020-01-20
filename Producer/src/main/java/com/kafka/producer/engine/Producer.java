package com.kafka.producer.engine;


import com.kafka.producer.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "my_topic";
    private static final Random RANDOM = new Random();

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void sendMessage(String messages) throws InterruptedException {
        logger.info(String.format("#### -> Producing message -> %s", messages));

        User user = new User("Name_" + UUID.randomUUID().toString(), RANDOM.nextInt());

        Message<User> message = MessageBuilder
                .withPayload(user)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(KafkaHeaders.PARTITION_ID, 1)
                .build();
        this.kafkaTemplate.send(message);
    }
}
