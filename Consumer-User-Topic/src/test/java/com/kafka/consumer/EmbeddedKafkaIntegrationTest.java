package com.kafka.consumer;

import com.kafka.consumer.engine.Consumer;
import com.kafka.consumer.engine.Producer;
import com.kafka.consumer.models.User;
import com.kafka.consumer.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private Consumer consumer;

    @Autowired
    private Producer producer;

    @Value("${spring.kafka.topic.user_topic}")
    private String userTopic;

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingToSimpleProducer_thenMessageReceived() throws InterruptedException {
        User user = TestUtil.nextObject(User.class);

        producer.sendUser(userTopic, user);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertEquals(consumer.getLatch().getCount(), Matchers.equalTo(0L));
        assertEquals(consumer.getPayload().getName(), user.getName());
        assertEquals(consumer.getPayload().getAge(), user.getAge());
    }
}
