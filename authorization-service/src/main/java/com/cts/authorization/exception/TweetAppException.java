package com.cts.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class TweetAppException extends RuntimeException {
	private static final long serialVersionUID = 1558149957272449535L;
	private int statusCode;
	private HttpStatus status;
	private String data;
}
