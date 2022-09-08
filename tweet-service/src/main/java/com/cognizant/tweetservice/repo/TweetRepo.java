package com.cognizant.tweetservice.repo;

import java.util.List;
import java.util.Optional;

import com.cognizant.tweetservice.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepo extends MongoRepository<Tweet, Long> {
	@Query("{ userName : ?0}")
	List<Tweet> findByUserName(String userName);

}
