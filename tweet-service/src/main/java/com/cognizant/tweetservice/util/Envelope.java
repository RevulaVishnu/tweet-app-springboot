package com.cognizant.tweetservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Envelope<T> {
	public int statusCode;
	public HttpStatus httpStatus;
	public T data;
}
