package com.cognizant.userservice.service;

import java.util.List;
import java.util.Optional;

import com.cognizant.userservice.configuration.KafkaProducerConfig;
import com.cognizant.userservice.exception.TweetAppException;
import com.cognizant.userservice.model.UserData;
import com.cognizant.userservice.repo.UserRepo;
import com.cognizant.userservice.util.RequestResponse;
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

//	@Autowired
	private KafkaProducerConfig producer;

	public ResponseEntity<RequestResponse<String>> register(UserData user) {
		log.info(Constants.IN_REQUEST_LOG, "register", user.toString());
		Optional<UserData> isValid = userRepository.findByEmailIdName(user.getEmail());
		String userRegister = isValid.isPresent()
				? Constants.USER_NAME_ALREADY_EXIST
				: Constants.USER_NAME_REGISTERED_SUCCESSFULLY;
		if (isValid.isPresent())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userRegister);
		else
			userRepository.save(user);
		log.info(Constants.EXITING_RESPONSE_LOG, "register", userRegister);
		return ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, user.getEmail() + " " + userRegister));
	}

	public ResponseEntity<RequestResponse<String>> forgotPassword(String userName, String password) {
		log.info(Constants.IN_REQUEST_LOG, "forgotPassword", userName.concat(" " + password));
		Optional<UserData> findByEmailIdName = userRepository.findByEmailIdName(userName);
		if (findByEmailIdName.isEmpty()) {
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
					Constants.USER_NAME_NOT_PRESENT);
		}
//		producer.sendMessage("Forgot Password for :: " + userName.concat(" " + password));
		Query query = new Query();
		query.addCriteria(Criteria.where(Constants.EMAIL_ID).is(userName));
		Update update = new Update();
		update.set(Constants.PASSWORD, password);
		UserData user = mongoOperations.findAndModify(query, update, UserData.class);

		if (user == null)
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					Constants.ERROR_WHILE_UPDATING_PASSWORD);
		log.info(Constants.EXITING_RESPONSE_LOG, "forgotPassword", user.toString());
		return ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, Constants.PASSWORD_UPDATED));
	}

	public ResponseEntity<RequestResponse<List<UserData>>> getAllusers() {
		log.info(Constants.IN_REQUEST_LOG, "getAllusers", "Getting All Users");
		List<UserData> findAll = userRepository.findAll();
		log.debug("allUsers from list {}", findAll);
		if (findAll.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
					Constants.NO_USERS_FOUND);
		log.info(Constants.EXITING_RESPONSE_LOG, "getAllusers", findAll);
		return ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, findAll));
	}

	public ResponseEntity searchBasedOnUserName(String userName) {
		log.info(Constants.IN_REQUEST_LOG, "username", userName);
		Optional<UserData> userPresent = userRepository.findByUserName(userName);
		log.info(Constants.IN_REQUEST_LOG, "UserData is:", userPresent.toString());
		log.debug("{}", userPresent);
		if (userPresent.isEmpty())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userPresent.toString());
		log.info(Constants.EXITING_RESPONSE_LOG, "username", userPresent.isPresent() ? "Present" : "Not Present");
		return ResponseEntity.ok(new RequestResponse(HttpStatus.OK.value(), HttpStatus.OK, userPresent));
	}
}

