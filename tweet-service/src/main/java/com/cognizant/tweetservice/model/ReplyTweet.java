package com.cognizant.tweetservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReplyTweet {
    @Id
    private int tweetId;
    private String userName;
    @NotBlank(message = "Tweet cannot be empty")
    private String tweet;
    @CreatedDate
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date created;
}
