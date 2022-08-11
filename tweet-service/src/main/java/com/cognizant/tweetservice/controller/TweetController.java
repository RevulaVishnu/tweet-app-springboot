package com.cognizant.tweetservice.controller;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cognizant.tweetservice.util.Constants.ROOT_URL;

@RequestMapping(value = ROOT_URL)
@RestController
@Slf4j
@Generated
//@CrossOrigin(origins = "${client.url}")
public class TweetController {

    @GetMapping(value = "/hello")
    public String helloTweet(){
        return "Hello from Tweet controller";
    }
}
