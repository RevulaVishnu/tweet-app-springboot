package com.cts.authorization.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.cts.authorization.model.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.authorization.model.UserData;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class to test all the repository functionality
 *
 */
@SpringBootTest
@Slf4j
class UserRepoTests {

	@Autowired
	private UserRepo userRepo;
	private UserData validUser;
	private UserRequest validUserRequest;
	private UserRequest inValidUserRequest;
	private UserRequest inValidPasswordRequest;
	@BeforeEach
	public void createUser(){
		validUser = new UserData(
				"vishnu@gmail.com",
				"vishnu",
				"vishnu",
				"r",
				"male",
				"14-9-1999",
				"9440833421",
				false
		);
		validUserRequest = new UserRequest(
				"vishnu@gmail.com",
				"vishnu"
		);
		inValidUserRequest = new UserRequest(
				"vishu@gmail.com",
				"vishnu"
		);

		inValidPasswordRequest= new UserRequest(
				"vishnu@gmail.com",
				"vishu"
		);
	}
	@Test
	@DisplayName("This method is responsible to test findById() method when user exists in database")
	void testFindUserById_userExists() {
		log.info("START - testFindUserById_userExists()");
		Optional<UserData> userOptional = userRepo.findByEmailIdName(validUserRequest.getUsername());
		assertTrue(userOptional.isPresent());
		assertEquals(validUserRequest.getUsername(), userOptional.get().getEmail());
		log.info("END - testFindUserById_userExists()");
	}

	@Test
	@DisplayName("This method is responsible to test findById() method when user doesn not exists in database")
	void testFindUserById_userDoesNotExists() {
		log.info("START - testFindUserById_userDoesNotExists()");
//		final String id = "adminDoesNotExist";
		Optional<UserData> userOptional = userRepo.findByEmailIdName(inValidUserRequest.getUsername());
		assertTrue(userOptional.isEmpty());
		log.info("END - testFindUserById_userDoesNotExists()");
	}
}
