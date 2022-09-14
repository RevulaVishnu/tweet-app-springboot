package com.cognizant.tweetservice.rabbitmq;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ListenerService {


    private String tokenValue;

    private boolean authorizationStatus;
    public static final Logger logger = LoggerFactory.getLogger(ListenerService.class);

    @RabbitListener(queues = "token")
    public void getToken(String message) {
        setTokenValue(message);
        logger.info("From Queue : {}", message);
    }

    @RabbitListener(queues = "authStatus")
    public void getAuthStatus(boolean status) {
        setAuthorizationStatus(status);
        logger.info("From Queue : {}", status);
    }


//    @RabbitListener(queues = "helloPojo")
//    public void getPojo(Notification message) {
//        logger.info("From Queue : {}", message);
//    }
}
