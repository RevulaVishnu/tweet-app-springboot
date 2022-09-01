package com.cognizant.tweetservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TweetRequest {
    @Id
    private int tweetId;
    @NotBlank(message = "Tweet cannot be Null")
    private String tweet;
    private Date created;
}