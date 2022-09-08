package com.cts.authorization.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;

import com.cts.authorization.model.UserData;
import com.cts.authorization.service.UserServiceImpl;
import com.cts.authorization.util.UserDetailsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cts.authorization.exception.InvalidTokenException;
import com.cts.authorization.model.UserRequest;
import com.cts.authorization.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for the authorization controller
 *
 */
@WebMvcTest
@Slf4j
class AuthorizationControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailsService userDetailsService;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserDetailsUtils userDetailsUtils;
	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtUtil jwtUtil;

	private static ObjectMapper mapper = new ObjectMapper();
	private static SecurityUser validSecureUser;
	private static SecurityUser invalidUser;

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

		// User object
		validSecureUser = new SecurityUser(validUser.getEmail(), validUser.getPassword(), new ArrayList<>());
		invalidUser = new SecurityUser(validUser.getEmail(), validUser.getPassword(), new ArrayList<>());
	}

	/*****************************************************************
	 * User Login Tests
	 * 
	 * @throws Exception
	 * 
	 *****************************************************************
	 */
	@Test
	@DisplayName("This method is responsible to test login() method with valid credentials")
	void testLogin_withValidCredentials() throws Exception {
		log.info("START - testLogin_withValidCredentials()");

		// Set the user request

		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
		log.info("Token: {}", token);

		when(authenticationManager.authenticate(ArgumentMatchers.any()))
				.thenReturn(new TestingAuthenticationToken(validUserRequest.getUsername(), validUserRequest.getPassword(), new ArrayList<>()));
		when(jwtUtil.generateToken(ArgumentMatchers.any())).thenReturn(token);

		String json = mapper.writeValueAsString(validUserRequest);
		log.info("Input data {}", json);

		MvcResult result = mockMvc.perform(post("/api/v1.0/login").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();

		assertNotNull(contentAsString);
		// match the token from the response body
		assertTrue(contentAsString.contains(token));

		log.info("END - testLogin_withValidCredentials()");
	}

	@Test
	@DisplayName("This method is responsible to test login() method with Global Input Errors")
	void testLogin_withInvalidCredentials() throws Exception {
		log.info("START - testLogin_withGlobalExceptions()");
//		UserRequest user = new UserRequest("1", "password1");

		final String errorMessage = "Invalid Credentials";
				
		String json = mapper.writeValueAsString(inValidPasswordRequest);
		log.info("Input data {}", json);

		MvcResult result = mockMvc.perform(post("/api/v1.0/login").contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		String contentAsString = result.getResponse().getContentAsString();
		// match the token from the response body
		assertTrue(contentAsString.contains(errorMessage));


		log.info("END - testLogin_withGlobalExceptions()");
	}

	@Test
	@DisplayName("Test method to check for status check")
	void testStatusCheck() throws Exception {
		log.info("START - testStatusCheck()");

		MvcResult result = mockMvc.perform(get("/api/v1.0/statusCheck"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		
		assertEquals("OK", contentAsString);
		assertNotNull(result);
		
		log.info("END - testStatusCheck()");
	}
	
	// Class to avoid User conflict
	public class SecurityUser extends User {

		private static final long serialVersionUID = -4209816021578748288L;

		public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

	}

}
