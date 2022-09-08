package com.cognizant.tweetservice.service;

import com.cognizant.tweetservice.configuration.KafkaProducerConfig;
import com.cognizant.tweetservice.exception.TweetAppException;
import com.cognizant.tweetservice.model.Tweet;
import com.cognizant.tweetservice.model.TweetRequest;
import com.cognizant.tweetservice.repo.TweetRepo;
import com.cognizant.tweetservice.util.Constants;
import com.cognizant.tweetservice.util.RequestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class TweetService {

    @Autowired
    TweetRepo tweetRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    private KafkaProducerConfig producer;

    public ResponseEntity<RequestResponse<String>> postTweet(String userName, TweetRequest tweetRequest) {
        log.info(Constants.IN_REQUEST_LOG, "postTweet", tweetRequest);
        long count = tweetRepository.count();
        log.info("total tweets " + count);
        tweetRequest.setTweetId((int) count + 1);
        Tweet tweet = new Tweet(tweetRequest.getTweetId(), userName, tweetRequest.getTweet(),
                new Date(System.currentTimeMillis()), null, null);
        tweetRepository.save(tweet);
        log.info(Constants.EXITING_RESPONSE_LOG, "postTweet", tweet);
        return ResponseEntity.ok(new RequestResponse<String>(HttpStatus.OK.value(), HttpStatus.OK, "Saved"));
    }

    public ResponseEntity<RequestResponse<List<Tweet>>> getAllTweet() {
        log.info(Constants.IN_REQUEST_LOG, "getAllTweet", "getting All Tweets");
        List<Tweet> findAll = tweetRepository.findAll();
        if (findAll.isEmpty())
            throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                    Constants.NO_TWEETS_FOUND);
        log.info(Constants.EXITING_RESPONSE_LOG, "getAllTweet", findAll);
        return ResponseEntity.ok(new RequestResponse<List<Tweet>>(HttpStatus.OK.value(), HttpStatus.OK, findAll));
    }

    public ResponseEntity<RequestResponse<List<Tweet>>> getTweetsByUser(String username) {
        log.info(Constants.IN_REQUEST_LOG, "getAllTweet", "getting All Tweets");
        List<Tweet> findAll = tweetRepository.findByUserName(username);
        if (findAll.isEmpty())
            throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                    Constants.NO_TWEETS_FOUND);
        log.info(Constants.EXITING_RESPONSE_LOG, "getAllTweet", findAll);
        return ResponseEntity.ok(new RequestResponse<List<Tweet>>(HttpStatus.OK.value(), HttpStatus.OK, findAll));
    }

    public ResponseEntity<RequestResponse<String>> updateTweet(String userName, int tweetId, TweetRequest tweetRequest) {
        log.info(Constants.IN_REQUEST_LOG, "updateTweet", tweetRequest);
        tweetAndUserValidation(userName, tweetId);
        Tweet tweet = new Tweet(tweetId, userName, tweetRequest.getTweet(), new Date(System.currentTimeMillis()), null, null);
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));
        Update update = new Update();
        update.set(Constants.TWEET, tweet.getTweet());
        tweet = mongoOperations.findAndModify(query, update, Tweet.class);
        if (tweet == null)
            throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error While Updating Tweet");
        producer.sendMessage("Updated Tweet :: " + tweet.toString().concat(" by ::" + userName));
        log.info(Constants.EXITING_RESPONSE_LOG, "updateTweet", tweet);
        return ResponseEntity
                .ok(new RequestResponse<String>(HttpStatus.OK.value(), HttpStatus.OK, Constants.TWEET_UPDATED));
    }

    public ResponseEntity<RequestResponse<String>> deleteTweet(String userName, int tweetId) {
        log.info(Constants.IN_REQUEST_LOG, "deleteTweet", tweetId);
        tweetAndUserValidation(userName, tweetId);
        tweetRepository.deleteById((long) tweetId);
        log.info(Constants.EXITING_RESPONSE_LOG, "deleteTweet", Constants.TWEET_DELETED);
        return ResponseEntity
                .ok(new RequestResponse<String>(HttpStatus.OK.value(), HttpStatus.OK, Constants.TWEET_DELETED));
    }

    public ResponseEntity<RequestResponse<String>> likeTweet(String userName, int tweetId) {
        log.info(Constants.IN_REQUEST_LOG, "likeTweet", tweetId);
        tweetAndUserValidation(userName, tweetId);
        Optional<Tweet> tweetRes = tweetRepository.findById((long) tweetId);
        Tweet tweet = tweetRes.get();
        Map<String, Integer> OldlikesMap = tweet.getLikes();
        Map<String, Integer> updatedLikesMap = new HashMap<>();
        if (OldlikesMap != null)
            updatedLikesMap.putAll(OldlikesMap);
        updatedLikesMap.put(userName, 1);
        tweet.setLikes(updatedLikesMap);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TWEET_ID).is(tweetId));
        Update update = new Update();
        update.set(Constants.LIKES, tweet.getLikes());
        log.info(Constants.EXITING_RESPONSE_LOG, "likeTweet", tweet.getLikes());
        tweet = mongoOperations.findAndModify(query, update, Tweet.class);
        if (tweet == null)
            throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error While Liking");
        return ResponseEntity.ok(new RequestResponse<String>(HttpStatus.OK.value(), HttpStatus.OK, Constants.LIKED_TWEET));
    }

    public ResponseEntity<RequestResponse<String>> replyTweet(String userName, int tweetId, String reply) {
        log.info(Constants.IN_REQUEST_LOG, "replyTweet", tweetId + " " + reply);
        tweetAndUserValidation(userName, tweetId);
        Optional<Tweet> findById = tweetRepository.findById((long) tweetId);
        Tweet tweet = findById.get();
        Map<String, List<String>> newReplyList = new HashMap<>();
        Map<String, List<String>> oldReplies = tweet.getReplies();
        if (oldReplies == null) {
            newReplyList.put(userName, Collections.singletonList(reply));
        } else {
            if (oldReplies.containsKey(userName)) {
                List<String> list = new ArrayList<>(oldReplies.get(userName));
                list.add(reply);
                newReplyList.putAll(oldReplies);
                newReplyList.put(userName, list);
            } else {
                newReplyList.putAll(oldReplies);
                newReplyList.put(userName, Collections.singletonList(reply));
            }
        }
        tweet.setReplies(newReplyList);
        tweet.setReplies(newReplyList);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TWEET_ID).is(tweetId));
        Update update = new Update();
        update.set(Constants.REPLIES, newReplyList);

        tweet = mongoOperations.findAndModify(query, update, Tweet.class);
        log.info(Constants.EXITING_RESPONSE_LOG, "replyTweet", tweet);
        if (tweet == null)
            throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error While replying");
        return ResponseEntity
                .ok(new RequestResponse<String>(HttpStatus.OK.value(), HttpStatus.OK, Constants.REPLIED_TO_TWEET));
    }

    private void tweetAndUserValidation(String userName, int tweetId) {
        log.info(Constants.IN_REQUEST_LOG, "tweetAndUserValidation :: Validating User", userName);
        Optional<Tweet> findById = tweetRepository.findById((long) tweetId);
        log.info(Constants.IN_REQUEST_LOG, "tweetAndUserValidation :: Validating Tweet", tweetId);
        if (findById.isEmpty())
            throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                    Constants.NO_TWEETS_FOUND);
        log.info(Constants.EXITING_RESPONSE_LOG, "tweetAndUserValidation", "User And Tweet Validated");
    }

}

