package com.example.food.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Invalid email format")
	private String emailId;

	@NotBlank(message = "Password cannot be blank")
	private String password;

	public LoginRequest() {
	}

	public LoginRequest(String emailId, String password) {
		this.emailId = emailId;
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
