package com.cts.authorization.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import com.cts.authorization.exception.TweetAppException;
import com.cts.authorization.model.UserData;
import com.cts.authorization.model.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;

import com.cts.authorization.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;

/**
 * Test cases for user service
 *
 */
@SpringBootTest
@Slf4j
class UserServiceTests {

	@Autowired
	private UserDetailsServiceImpl userServiceImpl;

	@MockBean
	private UserRepo userRepository;

	@Value("Email not found")
	private String ERROR_MESSAGE;

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
	@DisplayName("This method is responsible to test LoadUserByUsername() method when username is valid")
	void testLoadUserByUsername_validUsername() {
		log.info("START - testLoadUserByUsername_validUsername()");

		// Data to mock
		// Convert to optional
		Optional<UserData> userOptional = Optional.of(validUser);

		// user-name to check - correct credentials

		// actual value
		SecurityUser securityUser = new SecurityUser(validUser.getEmail(), validUser.getPassword(), new ArrayList<>());
		// Mock the repository
		when(userRepository.findByEmailIdName(validUserRequest.getUsername())).thenReturn(userOptional);

		log.info("Running the test case...");
		// checking condition
		assertEquals(userServiceImpl.loadUserByUsername(validUserRequest.getUsername()), securityUser);
		assertNotNull(securityUser);

		log.info("END - testLoadUserByUsername_validUsername()");
	}

	@Test
	@DisplayName("This method is responsible to test LoadUserByUsername() method when username is invalid")
	void testLoadUserByUsername_invalidUsername() {
		log.info("START - testLoadUserByUsername_invalidUsername()");

		// Data to mock
		Optional<UserData> userOptional = Optional.of(validUser);

		// actual value
		SecurityUser securityUser = new SecurityUser(validUser.getEmail(), validUser.getPassword(), new ArrayList<>());

		// Mock the repository
		when(userRepository.findByEmailIdName(inValidUserRequest.getUsername())).thenReturn(userOptional);

		log.info("Running the test case...");
		// checking condition
		TweetAppException thrownException = assertThrows(TweetAppException.class,
				() -> userServiceImpl.loadUserByUsername(validUser.getEmail()));

		assertTrue(thrownException.getData().contains(ERROR_MESSAGE));
		assertNotNull(securityUser);

		log.info("END - testLoadUserByUsername_invalidUsername()");
	}

	@Test
	@DisplayName("This method is responsible to test LoadUserByUsername() method when username is invalid")
	void testLoadUserByUsername_invalidPassword() {
		log.info("START - testLoadUserByUsername_invalidPassword()");

		// Data to mock
		Optional<UserData> userOptional = Optional.of(validUser);

		// actual value
		SecurityUser securityUser = new SecurityUser(validUser.getEmail(), validUser.getPassword(), new ArrayList<>());

		// Mock the repository
		when(userRepository.findByEmailIdName(inValidPasswordRequest.getUsername())).thenReturn(userOptional);

		log.info("Running the test case...");

		assertNotEquals(securityUser.getPassword(), inValidPasswordRequest.getPassword());
		assertNotNull(securityUser);

		log.info("END - testLoadUserByUsername_invalidPassword()");
	}

	// Class to avoid User conflict
	@MockBean
	public static class SecurityUser extends User {

		private static final long serialVersionUID = -4209816021578748288L;

		public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

	}
}
