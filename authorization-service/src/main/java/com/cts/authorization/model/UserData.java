package com.cts.authorization.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Model class for user credentials
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "user-details")
public class UserData {
	@Id
	@Field(name = "email")
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String gender;
	private String dob;
	private String mobileNumber;
	private boolean online;
}
