package com.cognizant.userservice.controller;
//
import com.cognizant.userservice.model.UserData;
import com.cognizant.userservice.service.UserService;
import com.cognizant.userservice.util.UserDetailsUtils;
import io.micrometer.core.annotation.Timed;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cognizant.userservice.util.Constants.CLIENT_URL;
import static com.cognizant.userservice.util.Constants.ROOT_URL;

@RequestMapping(value = ROOT_URL)
@RestController
@Slf4j
@Generated
@CrossOrigin(origins = CLIENT_URL)
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping(value = "/register")
	@Timed(value = "registerUser.time", description = "Time taken to return registerUser")
	public ResponseEntity registerUser(@RequestBody @Valid UserData user)
	{
		user.setPassword(UserDetailsUtils.encodePassword(user.getPassword()));
		user.setUserName(user.getFirstName()+" "+ user.getLastName());
		System.out.println(user);
		log.info("Registration for user {} {}", user.getFirstName(), user.getLastName());
		return userService.register(user);
	}

	@GetMapping(value = "/forgot")
	@Timed(value = "forgotPassword.time", description = "Time taken to return forgotPassword")
	public ResponseEntity forgotPassword(@RequestParam("userName") String userName, @RequestParam("newPassword") String password) {
		log.info("forgot password for user {}", userName);
		return userService.forgotPassword(userName, password);
	}

	@GetMapping(value = "/users/all")
	@Timed(value = "users.time", description = "Time taken to return users")
	public ResponseEntity users() {
		log.info("Get All Users");
		return userService.getAllusers();
	}

	@GetMapping(value = "/users/search")
	@Timed(value = "searchUserName.time", description = "Time taken to return searchUserName")
	public ResponseEntity searchUserName(@RequestParam("userName") String userName) {
		log.info("Search UserName {}", userName);
		return userService.searchBasedOnUserName(UserDetailsUtils.extractFirstName(userName));
	}
	/**
	 * @URL: <a href="http://localhost:8081/statusCheck">...</a>
	 * @return "OK" if the server and controller is up and running
	 */
	@GetMapping(value = "/statusCheck")
	public String statusCheck() {
		log.info("OK");
		return "OK";
	}
}
