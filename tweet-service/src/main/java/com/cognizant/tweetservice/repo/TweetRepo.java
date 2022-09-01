package com.cognizant.tweetservice.repo;

import java.util.Optional;

import com.cognizant.tweetservice.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepo extends MongoRepository<Tweet, Long> {
	@Query("{ email : ?0}")
	Optional<Tweet> findByEmailIdName(String userName);

}
