package com.kafka.consumer.engine;

import com.kafka.consumer.models.User;
import com.kafka.consumer.models.UserData;
import com.kafka.consumer.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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

//    @KafkaListener(topics = "user", containerFactory = "kafkaListenerContainerFactoryUser")
//    public void consumeUser(User user) throws IOException {
//        logger.info(String.format("Topic: %s Consumed User: %s ", user));
//
//        ModelMapper mapper = new ModelMapper();
//        UserData userData = mapper.map(user, UserData.class);
//
//        userRepository.save(userData);
//    }

    @KafkaListener(topics = "user", containerFactory = "kafkaListenerContainerFactoryUser")
    public void consumeUser(@Payload User user,
                            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) throws IOException {
        logger.info(String.format("\nPLAY_LOAD: %s " +
                "\nRECEIVED_MESSAGE_KEY: %s " +
                "\nRECEIVED_PARTITION_ID: %s " +
                "\nRECEIVED_TOPIC: %s " +
                "\nRECEIVED_TIMESTAMP: %s ", user, key, partition, topic, ts));

    }


}
