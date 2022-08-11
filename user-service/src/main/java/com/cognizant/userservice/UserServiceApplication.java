package com.cognizant.userservice;

import com.cognizant.userservice.controller.UserController;
import com.cognizant.userservice.model.UserDetails;
import com.cognizant.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class UserServiceApplication {

	@Autowired
	UserService userService;
	public static void main(String[] args) throws ParseException {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	public void init() throws ParseException {
		UserDetails userDetails = new UserDetails(
				"vishnu@gmail.com",
				"vishnu",
				"vishnu",
				"revula",
				"Male",
				("14-08-1999"),
//				new SimpleDateFormat("dd-MM-yyyy").parse("14-08-1999"),
				"94413121212",
				false
		);
		userService.register(userDetails);
	}


}
