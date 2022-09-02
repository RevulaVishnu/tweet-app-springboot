package com.cognizant.userservice;

import com.cognizant.userservice.model.UserData;
import com.cognizant.userservice.repo.UserRepo;
import com.cognizant.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.text.ParseException;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = UserRepo.class)
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
