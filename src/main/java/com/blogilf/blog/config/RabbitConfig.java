package com.blogilf.blog.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private final String mainExchange = "mainExchange";
    private final String replyQueue = "replyQueue";
    private final String replyRoutingKey = "replyRoutingKey";

    @Bean
    public DirectExchange logsExchange() {
        return new DirectExchange(mainExchange);
    }

    @Bean
    public Queue replyQueue() {
        return new Queue(replyQueue, true);
    }

    @Bean
    public Binding replyBinding() {
        return BindingBuilder.bind(replyQueue()).to(logsExchange()).with(replyRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
