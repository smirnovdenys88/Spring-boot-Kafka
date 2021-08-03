package com.kafka.consumer.engine;

import com.kafka.consumer.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class Consumer {

    @KafkaListener(topics = "${spring.kafka.topic.user_topic}", containerFactory = "kafkaListenerContainerFactoryUser")
    public void consumeUser(@Payload User user,
                            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
        log.info("\nPLAY_LOAD: {} " +
                "\nRECEIVED_MESSAGE_KEY: {} " +
                "\nRECEIVED_PARTITION_ID: {} " +
                "\nRECEIVED_TOPIC: {} " +
                "\nRECEIVED_TIMESTAMP: {} ", user.toString(), key, partition, topic, ts);
    }
}
