package com.cognizant.userservice.repo;

import java.util.Optional;

import com.cognizant.userservice.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends MongoRepository<UserData, Long> {
	@Query("{ emailId : ?0,password: ?1 }")
	Optional<UserData> findByemailIdAndPassword(String emailId, String password);

	@Query("{ email : ?0}")
	Optional<UserData> findByEmailIdName(String userName);

}
