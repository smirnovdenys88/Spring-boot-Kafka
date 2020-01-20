package com.kafka.producer.controller;

import com.kafka.producer.engine.Producer;
import com.kafka.producer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private final Producer producer;

    @Autowired
    KafkaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        CompletableFuture.runAsync(() -> {
            try {
                producer.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @PostMapping("/publish/user")
    public void sendMessageToKafkaTopic(@RequestBody User user) {
        producer.sendUser(user);
    }
}
