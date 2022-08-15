package com.cts.authorization.service;

import java.util.Collections;
import java.util.Optional;

import com.cts.authorization.exception.TweetAppException;
import com.cts.authorization.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.cts.authorization.model.User;
import com.cts.authorization.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

	@Value("${userDetails.errorMessage}")
	private String USER_DOES_NOT_EXISTS_MESSAGE;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Gets the user based on the user-name if it exists in the database
	 * 
	 * @param username
	 * @return Optional<User> Object
	 */
	public Optional<User> findByUsername(String username) {
		log.info(Constants.IN_REQUEST_LOG, "login", username);
		Optional<User> isValid = userRepository.findById(username);
		System.out.println(isValid.toString());
		String userValid = isValid.isPresent() ? Constants.LOGIN_SUCCESS : Constants.LOGIN_FAILED;
		if (isValid.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userValid);
		log.info(Constants.EXITING_RESPONSE_LOG, "login", userValid);
		return isValid;
	}
//	public Optional<User> findByUsername(String username) {
//		return userRepository.findById(username);
//	}

	/**
	 * Loads user from the database if exists and compares with the given username.
	 *
	 * @param username
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> userOptional = findByUsername(username);
		String userValid = userOptional.isPresent() ? Constants.LOGIN_SUCCESS : Constants.LOGIN_FAILED;
		if (userOptional.isEmpty()) {
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userValid);
		}
		else {
			log.info(Constants.EXITING_RESPONSE_LOG, "login", userValid);
//			log.info("Username: {} is valid", username);
			User user = userOptional.get();
			return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
			);
		}
	}

}
