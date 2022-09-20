package com.cognizant.tweetservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client to connect with authorization microservice for token validation
 *
 */
@FeignClient(name = "auth-service", url = "auth-service:8083")
//@FeignClient(name = "auth-service", url = "tweet-LoadB-1RVB8HGCA28D3-cdb2a7c7804b5dd7.elb.us-east-1.amazonaws.com:8084")
public interface AuthorisationClient {
	
	/**
	 * method to validate jwt token
	 * @param token .
	 * @return true only if token is valid else false
	 * 
	 */
	@GetMapping("api/v1.0/validate")
	boolean validate(@RequestHeader(name = "Authorization") String token);

}
