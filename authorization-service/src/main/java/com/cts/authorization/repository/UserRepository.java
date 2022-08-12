package com.cts.authorization.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.authorization.model.UserRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.authorization.model.User;

import java.util.Optional;

/**
 * Repository for User
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ emailId : ?0,password: ?1 }")
    Optional<User> findByemailId(String emailId);

}
