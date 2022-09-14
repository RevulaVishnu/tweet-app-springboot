package com.cts.authorization.rabbitmq;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ListenerService {

    @Value("${mq.token}")
    private String mqKey;

    private String tokenValue;
    public static final Logger logger = LoggerFactory.getLogger(ListenerService.class);


    @RabbitListener(queues = "token")
    public void getToken(String message) {
        setTokenValue(message);
        logger.info("From Queue : {}", message);
    }

//    @RabbitListener(queues = "helloPojo")
//    public void getPojo(Notification message) {
//        logger.info("From Queue : {}", message);
//    }
}
