package com.cognizant.tweetservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "Tweet cannot be empty")
    private String tweet;
    @CreatedDate
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date created;
    private Map<String, Integer> likes;
    private Map<String, List<String>> replies;
}