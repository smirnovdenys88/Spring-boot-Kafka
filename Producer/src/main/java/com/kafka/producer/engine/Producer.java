package com.kafka.producer.engine;


import com.kafka.producer.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Value("${spring.kafka.topic.user_topic}")
    private String userTopic;

    private final KafkaTemplate<Integer, User> userKafkaTemplate;
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

    public Producer(KafkaTemplate<Integer, User> userKafkaTemplate) {
        this.userKafkaTemplate = userKafkaTemplate;
    }

    @Scheduled(fixedRate = 3000)
    public void sendUserScheduled() {
        Collections.shuffle(nameUserList);
        int random = 2;

        User user = new User();
        user.setName(nameUserList.get(random));
        user.setAge(randomAge.nextInt(80));

        logger.info(String.format("#### -> Producing User -> %s", user));
        logger.info(String.format("Topic: %s", userKafkaTemplate.partitionsFor(userTopic)));

        this.userKafkaTemplate.send(userTopic, 1888, user);
    }
}
