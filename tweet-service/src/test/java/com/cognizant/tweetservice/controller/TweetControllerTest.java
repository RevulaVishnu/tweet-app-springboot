package com.cognizant.tweetservice.controller;

import com.cognizant.tweetservice.model.Tweet;
import com.cognizant.tweetservice.model.TweetRequest;
import com.cognizant.tweetservice.repo.TweetRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @Mock
    TweetRepo tweetRepository;

    @Mock
    MongoOperations mongoperation;

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
    void postTweet() throws Exception {
        String json = mapper.writeValueAsString(validTweetReq);
        log.info("Input data {}", json);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjcwMzk2NzcsInN1YiI6ImFkbWluMSIsImV4cCI6MTY1ODU3NTY3N30.trkCUngtLG8C1W6obvcGvQhCK1J9qg2Hsbcn8GJB95Y";
        log.info("Token: {}", token);

        MvcResult result = mockMvc.perform(post("/api/v1.0/add"+username).contentType(MediaType.APPLICATION_JSON).header("Authorization",token)
                        .characterEncoding("UTF-8").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        assertNotNull(contentAsString);
        // match the token from the response body
        assertTrue(contentAsString.contains(token));

    }

    @Test
    void getAllTweet() {
    }

    @Test
    void getAllUserTweet() {
    }

    @Test
    void updateTweet() {
    }

    @Test
    void deleteTweet() {
    }

    @Test
    void likeTweet() {
    }

    @Test
    void replyTweet() {
    }

    @Test
    void statusCheck() {
    }
}