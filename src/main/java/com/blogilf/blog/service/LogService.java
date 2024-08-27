package com.blogilf.blog.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    
    private final RabbitTemplate rabbitTemplate;

    public LogService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendLogMessage(String message) {
        rabbitTemplate.convertAndSend("mainExchange", "logRoutingKey", message);
        System.out.println("Log message sent: " + message);
    }

    @RabbitListener(queues = "replyQueue")
    public void listenForReplies(String responseMessage) {
        System.out.println("Received response: " + responseMessage);
    }
}
