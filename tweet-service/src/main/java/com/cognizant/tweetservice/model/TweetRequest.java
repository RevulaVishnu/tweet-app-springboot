package com.cognizant.tweetservice.model;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TweetRequest {
    private int tweetId;
    @NotBlank(message = "Tweet cannot be Null")
    private String tweet;
}