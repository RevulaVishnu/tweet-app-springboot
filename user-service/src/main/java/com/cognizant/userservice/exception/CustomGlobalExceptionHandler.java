package com.cognizant.userservice.exception;

import java.util.List;
import java.util.stream.Collectors;

import com.cognizant.userservice.util.RequestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Generated
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	// error handle for @Valid

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		RequestResponse<String> requestResponse = new RequestResponse<>();
		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		requestResponse.setData(errors.get(0));
		requestResponse.setStatusCode(status.value());
		requestResponse.setHttpStatus(status);
		return new ResponseEntity<>(requestResponse, headers, status);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RequestResponse<String>> TweetAppException(TweetAppException exception) {

		RequestResponse<String> env = new RequestResponse<>(exception.getStatusCode(), exception.getStatus(), exception.getData());
		log.debug("TweetAppException StatusCode{} Message{}", exception.getStatus(), exception.getData());
		return new ResponseEntity<>(env, HttpStatus.valueOf(exception.getStatusCode()));

	}

}