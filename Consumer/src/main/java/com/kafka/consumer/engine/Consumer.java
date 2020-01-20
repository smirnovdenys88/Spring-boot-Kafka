package com.kafka.consumer.engine;

import com.kafka.consumer.models.User;
import com.kafka.consumer.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private UserRepository userRepository;

    @KafkaListener(topics = "my_topic", containerFactory = "kafkaListenerContainerFactory")
    public void consumeString(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message: %s ", message));
    }

    @KafkaListener(topics = "user", containerFactory = "kafkaListenerContainerFactoryUser")
    public void consumeUser(User user) throws IOException {
        logger.info(String.format("#### -> Consumed User: %s ", user));
    }
}
