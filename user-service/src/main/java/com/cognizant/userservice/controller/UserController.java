package com.cognizant.userservice.controller;
//
import com.cognizant.userservice.exception.InvalidTokenException;
import com.cognizant.userservice.exception.TweetAppException;
import com.cognizant.userservice.feign.AuthorisationClient;
import com.cognizant.userservice.model.UserDetails;
import com.cognizant.userservice.service.UserService;
import com.cognizant.userservice.util.Envelope;
import io.micrometer.core.annotation.Timed;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.cognizant.userservice.util.Constants.ROOT_URL;

@RequestMapping(value = ROOT_URL)
@RestController
@Slf4j
@Generated
//@CrossOrigin(origins = "${client.url}")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AuthorisationClient authorisationClient;

	@GetMapping(value = "/hello")
	public String helloUser(){
		return "Hello from user controller";
	}

	@PostMapping(value = "/register")
	@Timed(value = "registerUser.time", description = "Time taken to return registerUser")
	public ResponseEntity<Envelope<String>> registerUser(@RequestHeader(name = "Authorization") String token,@RequestBody @Valid UserDetails user)
	{
		if (!authorisationClient.validate(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}

		System.out.println(user.toString());
		log.info("Registration for user {} {}", user.getFirstName(), user.getLastName());
		return userService.register(user);
	}

/*
	@GetMapping(value = "/login")
	@Timed(value = "loginUser.time", description = "Time taken to return login")
	public ResponseEntity<Envelope<String>> login( @RequestParam("emailId") String emailId, @RequestParam("password") String password ) throws TweetAppException
	{
		log.info("Login for user {} {}", emailId, password);
		return userService.login(emailId, password);
	}
*/

	@GetMapping(value = "/forgot")
	@Timed(value = "forgotPassword.time", description = "Time taken to return forgotPassword")
	public ResponseEntity<Envelope<String>> forgotPassword(@RequestParam("userName") String userName, @RequestParam("newPassword") String password) {
		log.info("forgot password for user {}", userName);
		return userService.forgotPassword(userName, password);
	}

	@GetMapping(value = "/users")
	@Timed(value = "users.time", description = "Time taken to return users")
	public ResponseEntity<Envelope<List<UserDetails>>> users() {
		log.info("Get All Users");
		return userService.getAllusers();
	}

	@GetMapping(value = "/users/search")
	@Timed(value = "searchUserName.time", description = "Time taken to return searchUserName")
	public ResponseEntity<Envelope<UserDetails>> searchUserName(@RequestParam("userName") String userName) {
		log.info("Search UserName {}", userName);
		return userService.username(userName);
	}
}
