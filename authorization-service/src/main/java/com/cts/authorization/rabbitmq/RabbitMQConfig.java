package com.cts.authorization.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${mq.token}")
    private String mqKey;

    @Value("${mq.authStatus}")
    private String authStatus;

    public static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    Queue tokenQueue() {
        return new Queue(mqKey);
    }

    @Bean
    Queue authStatusQueue() {
        return new Queue(authStatus);
    }

//    @Bean
//    Queue helloPojoQueue() {
//        return new Queue("helloPojo");
//    }
//
//    @Bean
//    MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

}
