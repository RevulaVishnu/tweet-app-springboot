package com.cognizant.tweetservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "tweets")
public class Tweet {
    @Transient
    public static final String SEQUENCE_NAME = "tweet_sequence";
    @Id
    private int tweetId;
    private String userName;
    private String tweet;
    @CreatedDate
    private Date created;
    private Map<String, Integer> likes;
    private Map<String, List<String>> replies;
}