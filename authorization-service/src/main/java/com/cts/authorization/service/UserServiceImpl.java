package com.cts.authorization.service;

import java.util.Optional;

import com.cts.authorization.exception.TweetAppException;
import com.cts.authorization.util.Constants;
import com.cts.authorization.util.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.authorization.model.UserData;
import com.cts.authorization.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl  {

	@Value("${userDetails.errorMessage}")
	private String USER_DOES_NOT_EXISTS_MESSAGE;

	@Autowired
	private UserRepo userRepo;

	/**
	 * Gets the user based on the user-name if it exists in the database
	 * 
	 * @param username
	 * @return Optional<User> Object
	 */
	public UserData findByUsername(String username) {
		log.info(Constants.IN_REQUEST_LOG, "login", username);
		Optional<UserData> isValid = userRepo.findById(username);
		System.out.println(isValid.toString());
		String userValid = isValid.isPresent() ? Constants.LOGIN_SUCCESS : Constants.LOGIN_FAILED;
		if (isValid.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userValid);
		log.info(Constants.EXITING_RESPONSE_LOG, "login", userValid);

		return new UserData(
				isValid.get().getEmail(),
				isValid.get().getPassword(),
				isValid.get().getFirstName(),
				isValid.get().getLastName(),
				isValid.get().getGender(),
				isValid.get().getDob(),
				isValid.get().getMobileNumber(),
				isValid.get().isOnline()
				);
	}
//	public Optional<User> findByUsername(String username) {
//		return userRepository.findById(username);
//	}

	/**
	 * Loads user from the database if exists and compares with the given username.
	 *
	 * @param user
	 * @return UserDetails
	 */

	public ResponseEntity<Envelope<String>> register(UserData user) {
		log.info(Constants.IN_REQUEST_LOG, "register", user.toString());
		Optional<UserData> isValid = userRepo.findByEmailIdName(user.getEmail());
		String userRegister = isValid.isPresent()
				? Constants.USER_NAME_ALREADY_EXIST
				: Constants.USER_NAME_REGISTERED_SUCCESSFULLY;
		if (isValid.isPresent())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userRegister);
		else
			userRepo.save(user);
		log.info(Constants.EXITING_RESPONSE_LOG, "register", userRegister);
		return ResponseEntity.ok(new Envelope<>(HttpStatus.OK.value(), HttpStatus.OK, user.getEmail() + " " + userRegister));
	}



}
