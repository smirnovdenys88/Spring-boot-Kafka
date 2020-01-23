package com.kafka.producer.engine;


import com.kafka.producer.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "my_topic";
    private static final Random RANDOM = new Random();

    @Autowired
    private KafkaTemplate<Integer, User> userKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) throws InterruptedException {
        logger.info(String.format("#### -> Producing message -> %s", message));

        List<ListenableFuture<SendResult<String, String>>> future = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 10; i++) {
            future.add(this.kafkaTemplate.send(TOPIC,message + i));
        }

        Iterator<ListenableFuture<SendResult<String, String>>> iterator = future.iterator();
        while (iterator.hasNext()){
            iterator.next().addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    logger.info("Callback onFailure=["
                            + message + "] due to : " + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    logger.info("Callback onSuccess=[" + message +"] with offset=[" + stringStringSendResult.getRecordMetadata().offset() + "]");
                }
            });

        }

    }

    public void sendUser(User user) {
        logger.info(String.format("#### -> Producing User -> %s", user));
        this.userKafkaTemplate.send("user", 1888, user);
    }
}
