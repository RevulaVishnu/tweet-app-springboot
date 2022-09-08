package com.cts.authorization.controller;

import javax.validation.Valid;

import com.cts.authorization.model.UserResponseData;
import com.cts.authorization.service.UserDetailsServiceImpl;
import com.cts.authorization.service.UserServiceImpl;
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
@CrossOrigin(origins = "http://localhost:3000")
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

	@Value("Invalid Credentials")
	private String BAD_CREDENTIALS_MESSAGE;

	/**
	 * @return token on successful login else throws exception handled by
	 * GlobalExceptionHandler
	 * @URL: <a href="http://localhost:8081//api/v1.0/login">...</a>
	 * <p>
	 * //	 * @param userRequest {username, password}
	 */
	@PostMapping("/login")
	//	public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password ) {
	public Envelope<? extends Object> login(@RequestBody @Valid UserRequest userRequest) {
		boolean isAuthenticated = false;
		System.out.println("In login controller meth");
		log.info("START - login()");
		try {
//			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
			if (authenticate.isAuthenticated()) {
				isAuthenticated=true;
				log.info("Valid User detected - logged in");
			}
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(BAD_CREDENTIALS_MESSAGE);
		} catch (Exception e){
			e.printStackTrace();
		}
		if(isAuthenticated){
//			String token = jwtUtil.generateToken(username);
			String token = jwtUtil.generateToken(userRequest.getUsername());
			UserResponseData userResponseData = new UserResponseData((userService.findByUsername(userRequest.getUsername())),token);
//			String token = jwtUtil.generateToken(UserDetailsUtils.extractUsername(userRequest.getUsername()));
			System.out.println(token);
			log.info("END - login()");
			return new Envelope<UserResponseData>(HttpStatus.OK.value(), HttpStatus.OK,userResponseData);
		}else{
			return new Envelope<String>(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,"Invalid Credentials");
		}
	}

	/**
	 * Checks if the token is a valid administrator token
	 *
	 * @URL: <a href="http://localhost:8081/validate">...</a>
	 *
	 * @Header: [Authorization] = JWT Token
	 *
	 * @RequestHeader token
	 */
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateJWT(@RequestHeader(name = "Authorization") String token) {
		log.info("START - validateJWT()");

		// throws custom exception and response if token is invalid
		jwtUtil.isTokenExpiredOrInvalidFormat(token);

		UserDetails user = userDetailsService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
//		System.out.println(user.toString());
		if (!user.getUsername().equals("")) {
			log.info("END - validateJWT()");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}

		return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

	}

	/**
	 * @URL: <a href="http://localhost:8084/api/v1.0/statusCheck">...</a>
	 * @return "OK" if the server and controller is up and running
	 */
	@GetMapping(value = "/statusCheck")
	public String statusCheck() {
		log.info("OK");
		return "OK";
	}
}
