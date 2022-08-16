package com.cognizant.userservice;

import com.cognizant.userservice.model.UserData;
import com.cognizant.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

@SpringBootApplication
public class UserServiceApplication {

	@Autowired
	UserService userService;
	public static void main(String[] args) throws ParseException {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	public void init() throws ParseException {
		UserData userData = new UserData(
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
		userService.register(userData);
	}


}
