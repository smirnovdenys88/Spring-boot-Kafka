package com.kafka.consumer.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

//    @KafkaListener(topics = "users", groupId = "group_id")
//    public void consume(String message) throws IOException {
//        logger.info(String.format("#### -> Consumed message -> %s", message));
//    }

    @KafkaListener(topics = "users1", groupId = "group_id")
    public void consume1(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        try {
            Thread.sleep(500l);
            logger.info(String.format("#### -> Consumed message -> %s END", message));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
