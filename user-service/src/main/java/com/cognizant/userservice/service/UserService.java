package com.cognizant.userservice.service;

import java.util.List;
import java.util.Optional;

import com.cognizant.userservice.configuration.KafkaProducerConfig;
import com.cognizant.userservice.exception.TweetAppException;
import com.cognizant.userservice.model.UserDetails;
import com.cognizant.userservice.repo.UserRepo;
import com.cognizant.userservice.util.Envelope;
import com.cognizant.userservice.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserService {

	@Autowired
	UserRepo userRepository;

	@Autowired
	MongoOperations mongoOperations;

	@Autowired
	private KafkaProducerConfig producer;

	public ResponseEntity<Envelope<String>> register(UserDetails user) {
		log.info(Constants.IN_REQUEST_LOG, "register", user.toString());
		Optional<UserDetails> isValid = userRepository.findByEmailIdName(user.getEmail());
		String userRegister = isValid.isPresent()
				? Constants.USER_NAME_ALREADY_EXIST
				: Constants.USER_NAME_REGISTERED_SUCCESSFULLY;
		if (isValid.isPresent())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userRegister);
		else
			userRepository.save(user);
		log.info(Constants.EXITING_RESPONSE_LOG, "register", userRegister);
		return ResponseEntity.ok(new Envelope<>(HttpStatus.OK.value(), HttpStatus.OK, user.getEmail() + " " + userRegister));
	}

/*
	public ResponseEntity<Envelope<String>> login(String userName, String password) throws TweetAppException {
		log.info(Constants.IN_REQUEST_LOG, "login", userName.concat(" " + password));
		Optional<UserDetails> isValid = userRepository.findByemailIdAndPassword(userName, password);
		String userValid = isValid.isPresent() ? Constants.LOGIN_SUCCESS : Constants.LOGIN_FAILED;
		if (isValid.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userValid);
		log.info(Constants.EXITING_RESPONSE_LOG, "login", userValid);
		return ResponseEntity.ok(new Envelope<String>(HttpStatus.OK.value(), HttpStatus.OK, userValid));
	}
*/

	public ResponseEntity<Envelope<String>> forgotPassword(String userName, String password) {
		log.info(Constants.IN_REQUEST_LOG, "forgotPassword", userName.concat(" " + password));
		Optional<UserDetails> findByEmailIdName = userRepository.findByEmailIdName(userName);
		if (findByEmailIdName.isEmpty()) {
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
					Constants.USER_NAME_NOT_PRESENT);
		}
		producer.sendMessage("Forgot Password for :: " + userName.concat(" " + password));
		Query query = new Query();
		query.addCriteria(Criteria.where(Constants.EMAIL_ID).is(userName));
		Update update = new Update();
		update.set(Constants.PASSWORD, password);
		UserDetails user = mongoOperations.findAndModify(query, update, UserDetails.class);

		if (user == null)
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					Constants.ERROR_WHILE_UPDATING_PASSWORD);
		log.info(Constants.EXITING_RESPONSE_LOG, "forgotPassword", user.toString());
		return ResponseEntity.ok(new Envelope<>(HttpStatus.OK.value(), HttpStatus.OK, Constants.PASSWORD_UPDATED));
	}

	public ResponseEntity<Envelope<List<UserDetails>>> getAllusers() {
		log.info(Constants.IN_REQUEST_LOG, "getAllusers", "Getting All Users");
		List<UserDetails> findAll = userRepository.findAll();
		log.debug("allUsers from list {}", findAll);
		if (findAll.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
					Constants.NO_USERS_FOUND);
		log.info(Constants.EXITING_RESPONSE_LOG, "getAllusers", findAll);
		return ResponseEntity.ok(new Envelope<>(HttpStatus.OK.value(), HttpStatus.OK, findAll));
	}

	public ResponseEntity<Envelope<UserDetails>> username(String userName) {
		log.info(Constants.IN_REQUEST_LOG, "username", userName);
		Optional<UserDetails> userPresent = userRepository.findByEmailIdName(userName);
		log.debug("{}", userPresent);
		if (userPresent.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userPresent.toString());
		log.info(Constants.EXITING_RESPONSE_LOG, "username", userPresent.isPresent() ? "Present" : "Not Present");
		return ResponseEntity.ok(new Envelope(HttpStatus.OK.value(), HttpStatus.OK, userPresent));
	}
}

