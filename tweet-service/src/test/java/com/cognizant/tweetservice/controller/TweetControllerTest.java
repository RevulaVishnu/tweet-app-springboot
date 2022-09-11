package com.cognizant.tweetservice.controller;

import com.cognizant.tweetservice.model.Tweet;
import com.cognizant.tweetservice.model.TweetRequest;
import com.cognizant.tweetservice.repo.TweetRepo;
import com.cognizant.tweetservice.util.Constants;
import com.cognizant.tweetservice.util.RequestResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class TweetControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private static ObjectMapper mapper = new ObjectMapper();
//
//    @Mock
//    TweetRepo tweetRepository;
//
//    @Mock
//    MongoOperations mongoperation;
//
//    private TweetRequest validTweetReq;
//    private String username;
//
//    private Tweet testTweet;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//        validTweetReq = new TweetRequest(
//                1,
//                "Test tweet"
//        );
//        testTweet=new Tweet(1, "testUser1", "TestTweet1", null, null, null);
//
//        username = "TestUser";
//    }
//
//    @Test
//    void postTweet() throws Exception {
//        String json = mapper.writeValueAsString(validTweetReq);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(post("/api/v1.0/add"+username).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
//                        .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        String contentAsString = result.getResponse().getContentAsString();
//
//        assertNotNull(contentAsString);
//        // match the token from the response body
//        assertTrue(contentAsString.contains("Saved"));
//    }
//
//    @Test
//    void getAllTweet() throws Exception {
//        String json = mapper.writeValueAsString(validTweetReq);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(get("/api/v1.0/all").contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        String contentAsString = result.getResponse().getContentAsString();
//
//        assertNotNull(contentAsString);
//        // match the token from the response body
//        assertFalse(contentAsString.contains("No Tweets Found"));
//    }
//
//    @Test
//    void getAllUserTweet() throws Exception{
//        String json = mapper.writeValueAsString(validTweetReq);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(get("/api/v1.0/"+username).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
//                .characterEncoding("UTF-8").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        int contentAsString = result.getResponse().getStatus();
//
//        // match the token from the response body
//        assertEquals(contentAsString, HttpStatus.INTERNAL_SERVER_ERROR.value());
//    }
//
//    @Test
//    void updateTweet() throws Exception{
//        TweetRequest update = new TweetRequest(1,"update to test tweet");
//        String json = mapper.writeValueAsString(update);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(put("/api/v1.0/"+username+"/update/"+update.getTweetId()).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
//                .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        String contentAsString = result.getResponse().getContentAsString();
//
//        // match the token from the response body
//        assertTrue(contentAsString.contains(Constants.TWEET_UPDATED));
//
//    }
//
//    @Test
//    void deleteTweet() throws Exception {
//        TweetRequest update = new TweetRequest(1,"update to test tweet");
//        String json = mapper.writeValueAsString(update);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(delete("/api/v1.0/"+username+"/delete/"+update.getTweetId()).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
//                .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        String contentAsString = result.getResponse().getContentAsString();
//
//        // match the token from the response body
//        assertTrue(contentAsString.contains(Constants.TWEET_DELETED));
//
//    }
//
//    @Test
//    void likeTweet() throws Exception{
//        String json = mapper.writeValueAsString(validTweetReq);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(get("/api/v1.0/"+username+"/like/"+validTweetReq.getTweetId()).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
//                .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        String contentAsString = result.getResponse().getContentAsString();
//
//        // match the token from the response body
//        assertTrue(contentAsString.contains(Constants.LIKED_TWEET));
//
//    }
//
//    @Test
//    void replyTweet() throws Exception{
//        TweetRequest replyTweet = new TweetRequest(1,"replyTweet to test tweet");
//        String json = mapper.writeValueAsString(replyTweet);
//        log.info("Input data {}", json);
//
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
//        log.info("Token: {}", token);
//
//        MvcResult result = mockMvc.perform(get("/api/v1.0/"+username+"/reply/"+replyTweet.getTweetId()).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
//                .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//                .andReturn();
//        String contentAsString = result.getResponse().getContentAsString();
//
//        // match the token from the response body
//        assertTrue(contentAsString.contains(Constants.REPLIED_TO_TWEET));
//    }
//
//    @Test
//    @DisplayName("Test method to check for status check")
//    void testStatusCheck() throws Exception {
//        log.info("START - testStatusCheck()");
//
//        MvcResult result = mockMvc.perform(get("/api/v1.0/statusCheck"))
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//
//        String contentAsString = result.getResponse().getContentAsString();
//
//        assertEquals("OK", contentAsString);
//        assertNotNull(result);
//
//        log.info("END - testStatusCheck()");
//    }
}