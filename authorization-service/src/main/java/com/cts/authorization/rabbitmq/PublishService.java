package com.cts.authorization.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class PublishService {

    @Value("${mq.token}")
    private String mqKey;

    @Value("${mq.authStatus}")
    private String authStatus;

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public PublishService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


//    @Scheduled(fixedDelay = 1000)
    public void pushMessage(String message) {
        //        String messageString = "Hello Rabbit @ " + LocalTime.now().format(DateTimeFormatter.ISO_TIME);
        rabbitTemplate.convertAndSend(mqKey, message);
    }

    public void pushMessage(boolean message) {
        //        String messageString = "Hello Rabbit @ " + LocalTime.now().format(DateTimeFormatter.ISO_TIME);
        rabbitTemplate.convertAndSend(authStatus, message);
    }

//    @Scheduled(fixedDelay = 1000)
//    public void publishNotification() {
//        rabbitTemplate.convertAndSend("helloPojo", new Notification("Hello Rabbit", LocalTime.now().format(DateTimeFormatter.ISO_TIME)));
//    }
}
