package com.kafka.producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.address}")
    private String bootstrapAddress;

    @Value("${spring.kafka.topic.test_topic}")
    private String testTopic;

    @Value("${spring.kafka.topic.user_topic}")
    private String userTopic;

    //automatically add topics to the broker
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    //use NewTopic
    @Bean
    public NewTopic topic1() {
        return new NewTopic(testTopic, 2, (short) 1);
    }

    //use TopicBuilder
    @Bean
    public NewTopic topicUser() {
        return TopicBuilder.name(userTopic) // name topic
//                .partitions(2)  // ?
//                .replicas(1)   // Replicas are nothing but backups of a partition. Replicas never read or write data. They are used to
                // prevent data loss
                .build();
    }
}

