package com.cognizant.tweetservice.controller;

import com.cognizant.tweetservice.model.UserData;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.cognizant.tweetservice.util.Constants.ROOT_URL;

@RequestMapping(value = ROOT_URL)
@RestController
@Slf4j
@Generated
//@CrossOrigin(origins = "${client.url}")
public class TweetController {
    /**
     * @URL: <a href="http://localhost:8084/statusCheck">...</a>
     * @return "OK" if the server and controller is up and running
     */
    @GetMapping(value = "/statusCheck")
    public String statusCheck() {
        log.info("OK");
        return "OK";
    }
}
