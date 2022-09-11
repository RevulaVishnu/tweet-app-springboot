
package com.cognizant.tweetservice.configuration;

import com.cognizant.tweetservice.util.Constants;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j

@Generated

@Service
public class KafkaConsumerConfig {

	@KafkaListener(topics = "message", groupId = Constants.GROUP_ID)
	public void consume(String message) {
		System.out.println("message received" + message);
		log.info(String.format("Message received -> %s", message));
	}
}
