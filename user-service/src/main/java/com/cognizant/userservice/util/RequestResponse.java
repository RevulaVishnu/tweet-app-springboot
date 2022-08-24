package com.cognizant.userservice.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse<T> {
	public int statusCode;
	public HttpStatus httpStatus;
	public T data;
}
