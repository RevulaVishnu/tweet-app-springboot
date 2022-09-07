
package com.cognizant.tweetservice.configuration;

import com.cognizant.tweetservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service

@Slf4j

@Configuration
public class KafkaProducerConfig {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
		log.info(String.format("Message sent-> %s", message));
		this.kafkaTemplate.send(Constants.TOPIC_NAME, Constants.TOPIC_NAME, message);
	}

	@Bean
	public NewTopic createTopic() {
		return new NewTopic(Constants.TOPIC_NAME, 3, (short) 1);
	}

}
