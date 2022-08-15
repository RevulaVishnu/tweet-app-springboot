package com.cts.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for user credentials
 *
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
	@Id
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String gender;
	private String dob;
	private String mobileNumber;
	private boolean online;
	private String role;
}
