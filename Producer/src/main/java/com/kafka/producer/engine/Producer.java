package com.kafka.producer.engine;


import com.kafka.producer.models.User;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "my_topic";
    private static final Random RANDOM = new Random();
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Autowired
    private KafkaTemplate<Integer, User> userKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private Random randomAge = new Random();

    private static List<String> nameUserList = new ArrayList<>();


    static {
        nameUserList.add("Liam");
        nameUserList.add("Noah");
        nameUserList.add("William");
        nameUserList.add("James");
        nameUserList.add("Oliver");
        nameUserList.add("Benjamin");
        nameUserList.add("Elijah");
        nameUserList.add("Lucas");
    }

    public void sendMessage(String message) throws InterruptedException {
        logger.info(String.format("#### -> Producing message -> %s", message));

        List<ListenableFuture<SendResult<String, String>>> future = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 10; i++) {
            future.add(this.kafkaTemplate.send(TOPIC, message + i));
        }

        Iterator<ListenableFuture<SendResult<String, String>>> iterator = future.iterator();
        while (iterator.hasNext()) {
            iterator.next().addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    logger.info("Callback onFailure=["
                            + message + "] due to : " + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    logger.info("Callback onSuccess=[" + message + "] with offset=[" + stringStringSendResult.getRecordMetadata().offset() + "]");
                }
            });

        }

    }

    public void sendUser(User user) {
        logger.info(String.format("#### -> Producing User -> %s", user));
        this.userKafkaTemplate.send("user", 1888, user);
    }

    @Scheduled(fixedRate = 1000)
    public void sendUserScheduled() {
        Collections.shuffle(nameUserList);
        int random = 2;

        User user = new User();
        user.setName(nameUserList.get(random));
        user.setAge(randomAge.nextInt(80));

        logger.info(String.format("#### -> Producing User -> %s", user));
        logger.info(String.format("Topic: %s", userKafkaTemplate.partitionsFor("user").toString()));

        this.userKafkaTemplate.send("user", 1888, user);
    }

//    @Scheduled(fixedRate = 1000)
    public void sendMessageSchedule() throws InterruptedException {
        Collections.shuffle(nameUserList);
        int random = 2;

        String message = "Hello " + nameUserList.get(random);
//        logger.info(String.format("#### -> Producing message -> %s", message));

        List<ListenableFuture<SendResult<String, String>>> future = new CopyOnWriteArrayList<>();

        atomicInteger.incrementAndGet();
        future.add(this.kafkaTemplate.send(TOPIC, String.valueOf(atomicInteger.get()), message));


        Iterator<ListenableFuture<SendResult<String, String>>> iterator = future.iterator();
        while (iterator.hasNext()) {
            iterator.next().addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    logger.info("Callback onFailure=["
                            + message + "] due to : " + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    logger.info(String.format(
                            "\ntopic: %s" +
                                    "\npartition: %s" +
                                    "\nhasOffset: %s" +
                                    "\noffset: %s" +
                                    "\ntimestamp: %s" +
                                    "\nchecksum: %s" +
                                    "\n------------------------------------------------------------------------------------------",
                            stringStringSendResult.getRecordMetadata().topic(),
                            stringStringSendResult.getRecordMetadata().partition(),
                            stringStringSendResult.getRecordMetadata().hasOffset(),
                            stringStringSendResult.getRecordMetadata().offset(),
                            stringStringSendResult.getRecordMetadata().timestamp(),
                            stringStringSendResult.getRecordMetadata().checksum()));
                }
            });

        }

    }
}
