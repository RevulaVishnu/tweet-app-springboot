package com.cognizant.tweetservice.controller;

import com.cognizant.tweetservice.exception.InvalidTokenException;
import com.cognizant.tweetservice.feign.AuthorisationClient;
import com.cognizant.tweetservice.model.Tweet;
import com.cognizant.tweetservice.model.TweetRequest;
import com.cognizant.tweetservice.service.TweetService;
import com.cognizant.tweetservice.util.RequestResponse;
import io.micrometer.core.annotation.Timed;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import java.util.List;
import java.util.Optional;

import static com.cognizant.tweetservice.util.Constants.ROOT_URL;
import static com.cognizant.tweetservice.util.Constants.CLIENT_URL;

@RequestMapping(value = ROOT_URL)
@RestController
@Slf4j
@Generated
@CrossOrigin(origins = CLIENT_URL)
public class TweetController {

    @Autowired
    TweetService tweetService;

    @Autowired
    AuthorisationClient authorisationClient;

    @PostMapping("/add/{userName}")
//    @Timed(value = "postTweet.time", description = "Time taken to return postTweet")
    public ResponseEntity<RequestResponse<String>> postTweet(@RequestHeader(name = "Authorization") String token,
                                                      @RequestBody @Valid TweetRequest tweet,
                                                      @PathVariable(value = "userName") String userName) {
        if (!authorisationClient.validate(token)) {
            throw new InvalidTokenException("You are not allowed to access this resource");
        }
        log.info("In {} UserName {} ", "postTweet", userName);
        return tweetService.postTweet(userName,tweet);
    }

    @GetMapping("/all")
    @Timed(value = "all.time", description = "Time taken to return allTweet")
    public ResponseEntity<RequestResponse<List<Tweet>>> getAllTweet() {
        log.info("In {}", "getAllTweet");
        return tweetService.getAllTweet();
    }

    @GetMapping("/{userName}")
    @Timed(value = "getAllUserTweet.time", description = "Time taken to return getAllUserTweet")
    public ResponseEntity<RequestResponse<List<Tweet>>> getAllUserTweet(@RequestHeader(name = "Authorization") String token,
                                                                            @PathVariable String userName) {
        if (!authorisationClient.validate(token)) {
            throw new InvalidTokenException("You are not allowed to access this resource");
        }

        log.info("In {} UserName {} ", "getAllUserTweet", userName);
        return tweetService.getAllUserTweet(userName);
    }

    @PutMapping("/{userName}/update/{id}")
    @Timed(value = "updateTweet.time", description = "Time taken to return updateTweet")
    public ResponseEntity<RequestResponse<String>> updateTweet(@RequestHeader(name = "Authorization") String token,
                                                        @PathVariable("userName") String userName,
                                                        @PathVariable("id") int tweetId,
                                                        @RequestBody TweetRequest tweetRequest) {
        if (!authorisationClient.validate(token)) {
            throw new InvalidTokenException("You are not allowed to access this resource");
        }

        log.info("In {} UserName {} ", "updateTweet", userName);
        return tweetService.updateTweet(userName, tweetId, tweetRequest);
    }

    @DeleteMapping("/{userName}/delete/{id}")
    @Timed(value = "deleteTweet.time", description = "Time taken to return deleteTweet")
    public ResponseEntity<RequestResponse<String>> deleteTweet(@RequestHeader(name = "Authorization") String token,
                                                        @PathVariable("userName") String userName,
                                                        @PathVariable("id") int tweetId) {
        if (!authorisationClient.validate(token)) {
            throw new InvalidTokenException("You are not allowed to access this resource");
        }

        log.info("In {} UserName {} ", "deleteTweet", userName);
        return tweetService.deleteTweet(userName, tweetId);
    }

    @PutMapping("/{userName}/like/{id}")
    @Timed(value = "likeTweet.time", description = "Time taken to return likeTweet")
    public ResponseEntity<RequestResponse<String>> likeTweet(@RequestHeader(name = "Authorization") String token,
                                                      @PathVariable("userName") String userName,
                                                      @PathVariable("id") int tweetId) {
        if (!authorisationClient.validate(token)) {
            throw new InvalidTokenException("You are not allowed to access this resource");
        }

        log.info("In {} UserName {} ", "likeTweet", userName);
        return tweetService.likeTweet(userName, tweetId);
    }

    @PostMapping("/{userName}/reply/{id}")
    @Timed(value = "replyTweet.time", description = "Time taken to return replyTweet")
    public ResponseEntity<RequestResponse<String>> replyTweet(@RequestHeader(name = "Authorization") String token,
                                                       @PathVariable("userName") String userName,
                                                       @PathVariable("id") int tweetId,
                                                       @RequestBody TweetRequest reply) {
        if (!authorisationClient.validate(token)) {
            throw new InvalidTokenException("You are not allowed to access this resource");
        }

        log.info("In {} UserName {} ", "replyTweet", userName);
        return tweetService.replyTweet(userName, tweetId, reply.getTweet());
    }

    /**
     * @URL: <a href="http://localhost:8081/statusCheck">...</a>
     * @return "OK" if the server and controller is up and running
     */
    @GetMapping(value = "/statusCheck")
    public String statusCheck() {
        log.info("OK");
        return "OK";
    }
}
