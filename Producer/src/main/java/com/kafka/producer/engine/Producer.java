package com.kafka.producer.engine;


import com.kafka.producer.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "my_topic";
    private static final Random RANDOM = new Random();

    @Autowired
    private KafkaTemplate<String, User> userKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String messages) throws InterruptedException {
        logger.info(String.format("#### -> Producing message -> %s", messages));
        this.kafkaTemplate.send(TOPIC, messages);
    }

    public void sendUser(User user) {
        logger.info(String.format("#### -> Producing User -> %s", user));
        this.userKafkaTemplate.send("user",user);
    }
}
