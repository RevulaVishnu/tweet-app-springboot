package com.cts.authorization.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
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
    @Query("{ _id : ?0")
    Optional<User> findById(String emailId);

}
