
package com.cognizant.tweetservice.configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j

//@Generated

//@Service
public class KafkaConsumerConfig {

//	@KafkaListener(topics = "message", groupId = Constants.GROUP_ID)
	public void consume(String message) {
		System.out.println("message received" + message);
		log.info(String.format("Message received -> %s", message));
	}
}
