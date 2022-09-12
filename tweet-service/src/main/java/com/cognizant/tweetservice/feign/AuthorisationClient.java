package com.cognizant.tweetservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client to connect with authorization microservice for token validation
 *
 */
@FeignClient(name = "auth-server", url = "${client.post.baseUrl}")
public interface AuthorisationClient {
	
	/**
	 * method to validate jwt token
	 * @param token .
	 * @return true only if token is valid else false
	 * 
	 */
	@GetMapping("/validate")
	boolean validate(@RequestHeader(name = "Authorization") String token);

}
