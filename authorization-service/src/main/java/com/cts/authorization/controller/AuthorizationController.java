package com.cts.authorization.controller;

import javax.validation.Valid;

import com.cts.authorization.model.UserData;

import com.cts.authorization.service.UserServiceImpl;
import com.cts.authorization.util.EncodePassword;
import com.cts.authorization.util.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
//import io.micrometer.core.annotation.Timed;

import com.cts.authorization.exception.InvalidCredentialsException;
import com.cts.authorization.model.UserRequest;
import com.cts.authorization.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Authorization Controller to handle requests for logging in a valid user and
 * validating the JWT Token for other services.
 * 
 * 
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping(value = "/api/v1.0")
public class AuthorizationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${userDetails.badCredentialsMessage}")
	private String BAD_CREDENTIALS_MESSAGE;

	@Value("${userDetails.disabledAccountMessage}")
	private String DISABLED_ACCOUNT_MESSAGE;
	
	@Value("${userDetails.lockedAccountMessage}")
	private String LOCKED_ACCOUNT_MESSAGE;

	/**
	 * @URL: <a href="http://localhost:8081//api/v1.0/login">...</a>
	 *
	 * @Data: [Admin] { "username": "admin1", "password": "adminpass@1234" }
	 *
	 * @param userRequest {username, password}
	 * @return token on successful login else throws exception handled by
	 *         GlobalExceptionHandler
	 */
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid UserRequest userRequest) {
		System.out.println("In login controller meth");
		log.info("START - login()");
		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
			System.out.println(authenticate.isAuthenticated());
			if (authenticate.isAuthenticated()) {
				log.info("Valid User detected - logged in");
			}
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(BAD_CREDENTIALS_MESSAGE);
		} catch (DisabledException e) {
			throw new InvalidCredentialsException(DISABLED_ACCOUNT_MESSAGE);
		} catch (LockedException e) {
			throw new InvalidCredentialsException(LOCKED_ACCOUNT_MESSAGE);
		} catch (Exception e){
			e.printStackTrace();
		}

		String token = jwtUtil.generateToken(userRequest.getUsername());
		System.out.println(token);
		log.info("END - login()");
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@PostMapping(value = "/register")
//	@Timed(value = "registerUser.time", description = "Time taken to return registerUser")
	public ResponseEntity<Envelope<String>> registerUser(@RequestBody @Valid UserData user)
	{
		user.setPassword(EncodePassword.registerStudent(user.getPassword()));
//		if (!authorisationClient.validate(token)) {
//			throw new InvalidTokenException("You are not allowed to access this resource");
//		}

		System.out.println(user.toString());
		log.info("Registration for user {} {}", user.getFirstName(), user.getLastName());
		return userService.register(user);
	}


	/**
	 * Checks if the token is a valid administrator token
	 *
	 * @URL: <a href="http://localhost:8081/validate">...</a>
	 *
	 * @Header: [Authorization] = JWT Token
	 *
	 * @param token
	 */
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateJWT(@RequestHeader(name = "Authorization") String token) {
		log.info("START - validateJWT()");

		// throws custom exception and response if token is invalid
		jwtUtil.isTokenExpiredOrInvalidFormat(token);

		UserDetails user = userDetailsService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
		if (!user.getUsername().equals("")) {
			log.info("END - validateJWT()");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}

		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

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
