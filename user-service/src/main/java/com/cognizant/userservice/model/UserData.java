package com.cognizant.userservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "user-details")
public class UserData {
    @Id
    @Field(name = "email")
    private String email;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String dob;
    private String mobileNumber;
    private boolean online;
}