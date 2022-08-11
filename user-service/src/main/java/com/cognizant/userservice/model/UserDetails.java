package com.cognizant.userservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "user-details")
public class UserDetails {
    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String mobileNumber;
    private boolean online;
}