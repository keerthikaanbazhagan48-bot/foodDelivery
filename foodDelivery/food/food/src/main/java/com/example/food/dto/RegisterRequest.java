package com.example.food.dto;

import com.example.food.food.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = "Name cannot be blank")
	private String name;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Invalid email format")
	private String emailId;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 4, message = "Password must be at least 4 characters")
	private String password;

	@NotNull(message = "Role is required")
	private Role role;

	public RegisterRequest() {
	}

	public RegisterRequest(String name, String emailId, String password, Role role) {
		this.name = name;
		this.emailId = emailId;
		this.password = password;
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
