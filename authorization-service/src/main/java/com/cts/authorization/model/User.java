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
	private String username;
	private String password;
	private String role;
}
