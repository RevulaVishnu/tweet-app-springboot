package com.cognizant.userservice.repo;

import java.util.Optional;

import com.cognizant.userservice.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends MongoRepository<UserDetails, Long> {
	@Query("{ emailId : ?0,password: ?1 }")
	Optional<UserDetails> findByemailIdAndPassword(String emailId, String password);

	@Query("{ emailId : ?0}")
	Optional<UserDetails> findByEmailIdName(String userName);

}
