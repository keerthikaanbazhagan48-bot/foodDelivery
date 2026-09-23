package com.example.food.dto;

import com.example.food.food.model.Role;

public class AuthResponse {

	private int id;
	private String name;
	private String emailId;
	private Role role;
	private String message;

	public AuthResponse() {
	}

	public AuthResponse(int id, String name, String emailId, Role role, String message) {
		this.id = id;
		this.name = name;
		this.emailId = emailId;
		this.role = role;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
