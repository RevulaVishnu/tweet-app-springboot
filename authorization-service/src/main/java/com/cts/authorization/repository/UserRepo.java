package com.cts.authorization.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.authorization.model.UserData;

import java.util.Optional;

/**
 * Repository for User
 *
 */
@Repository
public interface UserRepo extends MongoRepository<UserData, Long> {
    @Query("{ email : ?0}")
    Optional<UserData> findById(String emailId);

    @Query("{ emailId : ?0,password: ?1 }")
    Optional<UserData> findByemailIdAndPassword(String emailId, String password);

    @Query("{ email : ?0}")
    Optional<UserData> findByEmailIdName(String userName);
}
