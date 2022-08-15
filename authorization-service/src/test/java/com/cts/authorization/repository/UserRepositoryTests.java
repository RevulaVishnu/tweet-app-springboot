package com.cts.authorization.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("This method is responsible to test findById() method when user exists in database")
	void testFindUserById_userExists() {
		log.info("START - testFindUserById_userExists()");
		final String username = "admin1";
		Optional<UserData> userOptional = userRepository.findById(username);
		assertTrue(userOptional.isPresent());
		assertEquals(username, userOptional.get().getEmail());
		log.info("END - testFindUserById_userExists()");
	}

	@Test
	@DisplayName("This method is responsible to test findById() method when user doesn not exists in database")
	void testFindUserById_userDoesNotExists() {
		log.info("START - testFindUserById_userDoesNotExists()");
		final String id = "adminDoesNotExist";
		Optional<UserData> userOptional = userRepository.findById(id);
		assertTrue(userOptional.isEmpty());
		log.info("END - testFindUserById_userDoesNotExists()");
	}
}
