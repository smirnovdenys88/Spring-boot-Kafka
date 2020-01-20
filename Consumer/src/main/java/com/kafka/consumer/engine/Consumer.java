package com.kafka.consumer.engine;

import com.kafka.consumer.models.User;
import com.kafka.consumer.models.UserData;
import com.kafka.consumer.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @KafkaListener(topics = "users2", groupId = "Group_consumer_1")
    public void consume(User user) throws IOException {
        ModelMapper mapper = new ModelMapper();
        UserData userData = mapper.map(user, UserData.class);
        userRepository.save(userData);

        logger.info(String.format("#### -> Consumed message user: %s , groupId: %s", user , groupId));
    }

//    @KafkaListener(topicPartitions = { @TopicPartition(topic = "users2", partitions = { "1" })},
//            groupId = "group_one",
//            containerFactory = "kafkaListenerContainerFactory",
//            autoStartup = "true")
//    public void consumeFromCoreTopicPartitionZERO(@Payload User containers){
//        logger.info("\n/******* Consume TEST-TOPIC Partition ---->>>>>>    ONE ********/\n"+containers);
//    }
}
