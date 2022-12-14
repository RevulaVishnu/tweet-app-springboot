package com.cognizant.tweetservice.exception;

import org.springframework.http.HttpStatus;

/**
 * This exception is thrown when user sends invalid token in api request
 *
 */
public class InvalidTokenException extends RuntimeException {
	private static final long serialVersionUID = 1558149957272449535L;
	private int statusCode;
	private HttpStatus status;
	private String data;
	public InvalidTokenException(String message) {
		this.statusCode=HttpStatus.UNAUTHORIZED.value();
		this.status=HttpStatus.UNAUTHORIZED;
		this.data=message;
	}

}
