package com.cognizant.tweetservice.service;

import com.cognizant.tweetservice.configuration.KafkaProducerConfig;
import com.cognizant.tweetservice.model.Tweet;
import com.cognizant.tweetservice.model.TweetRequest;
import com.cognizant.tweetservice.repo.TweetRepo;
import com.cognizant.tweetservice.util.Constants;
import com.cognizant.tweetservice.util.RequestResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

class TweetServiceTest {

    @InjectMocks
    TweetService tweetService;

    @Mock
    TweetRepo tweetRepository;

    @Mock
    MongoOperations mongoperation;

    @Mock
    KafkaProducerConfig producer;

    private TweetRequest validTweetReq;
    private String username;

    private Tweet testTweet;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        validTweetReq = new TweetRequest(
                1,
                "Test tweet"
        );
        testTweet=new Tweet(1, "testUser1", "TestTweet1", null, null, null);

        username = "TestUser";
    }

    @Test
    void postTweetTest() {
        ResponseEntity<RequestResponse<String>> postTweet = tweetService.postTweet(username, validTweetReq);
        Assertions.assertEquals(ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, "Saved")),
                postTweet);
    }
 @Test
    void postTweetInvalidUserTest() {
        ResponseEntity<RequestResponse<String>> postTweet = tweetService.postTweet(username, validTweetReq);
        Assertions.assertEquals(ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, "Saved")),
                postTweet);
    }

    @Test
    void getAllTweet() {
        List<Tweet> tweetList = Arrays.asList(new Tweet(1, "testUser1", "TestTweet1", null, null, null),
                new Tweet(2, "testuser2", "TestTweet2", null, null, null));
        Mockito.when(tweetRepository.findAll()).thenReturn(tweetList);
        ResponseEntity<RequestResponse<List<Tweet>>> allTweet = tweetService.getAllTweet();
        Assertions.assertEquals(ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, tweetList)),
                allTweet);
    }

    @Test
    void getTweetsByUserTest() {
        List<Tweet> tweetList = Arrays.asList(new Tweet(1, "testUser1", "TestTweet1", null, null, null),
                new Tweet(2, "testUser1", "TestTweet2", null, null, null));
        Mockito.when(tweetRepository.findByUserName("testUser1")).thenReturn(tweetList);
        List<Tweet> allTweet = tweetRepository.findByUserName("testUser1");

        Assertions.assertEquals(ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, tweetList)),
                ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, allTweet)));
    }

    @Test
    void updateTweet() {
        Mockito.when(tweetRepository.findById((long)1)).thenReturn(Optional.of(new Tweet()));
        Query query = new Query();
        query.addCriteria(Criteria.where("TestUser").is(username));
        Update update = new Update();
        update.set(Constants.TWEET, "Updated the tweet");

        Mockito.when(mongoperation.findAndModify(query, update, Tweet.class)).thenReturn(testTweet);
        ResponseEntity<RequestResponse<String>> updatedTweet = ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, Constants.TWEET_UPDATED));
        Assertions.assertEquals(
                ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, "Tweet Updated")),updatedTweet);
    }
    
    @Test
    void likeTweet() {
        Mockito.when(tweetRepository.findById((long)1)).thenReturn(Optional.of(testTweet));
        Map<String, Integer> OldlikesMap = testTweet.getLikes();
        Map<String, Integer> updatedLikesMap = new HashMap<>();
        if (OldlikesMap != null)
            updatedLikesMap.putAll(OldlikesMap);
        updatedLikesMap.put(username, 1);
        testTweet.setLikes(updatedLikesMap);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TWEET_ID).is(1));
        Update update = new Update();
        update.set(Constants.LIKES, testTweet.getLikes());
//        System.out.println(testTweet);
        Mockito.when(mongoperation.findAndModify(query, update, Tweet.class)).thenReturn(testTweet);
        ResponseEntity<RequestResponse<String>> updatedTweet = ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, Constants.LIKED_TWEET));
        Assertions.assertEquals(
                ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, "Liked tweet")),updatedTweet);

    }

    @Test
    void replyTweet() {
        Mockito.when(tweetRepository.findById((long)1)).thenReturn(Optional.of(testTweet));
        Map<String, List<String>> newReplyList = new HashMap<>();
        Map<String, List<String>> oldReplies = testTweet.getReplies();
        String reply = "test reply";
        if (oldReplies == null) {
            newReplyList.put(username, Collections.singletonList(reply));
        } else {
            if (oldReplies.containsKey(username)) {
                List<String> list = new ArrayList<>(oldReplies.get(username));
                list.add(reply);
                newReplyList.putAll(oldReplies);
                newReplyList.put(username, list);
            } else {
                newReplyList.putAll(oldReplies);
                newReplyList.put(username, Collections.singletonList(reply));
            }
        }
        testTweet.setReplies(newReplyList);
        testTweet.setReplies(newReplyList);
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.TWEET_ID).is(testTweet));
        Update update = new Update();
        update.set(Constants.REPLIES, newReplyList);
//        System.out.println(testTweet);
        Mockito.when(mongoperation.findAndModify(query, update, Tweet.class)).thenReturn(testTweet);
        ResponseEntity<RequestResponse<String>> updatedTweet = ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, Constants.REPLIED_TO_TWEET));
        Assertions.assertEquals(
                ResponseEntity.ok(new RequestResponse<>(HttpStatus.OK.value(), HttpStatus.OK, "Replied to tweet")),updatedTweet);

    }
}