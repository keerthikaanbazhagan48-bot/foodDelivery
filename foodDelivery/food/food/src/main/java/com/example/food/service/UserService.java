package com.example.food.service;

import java.util.List;

import com.example.food.dto.AuthResponse;
import com.example.food.dto.LoginRequest;
import com.example.food.dto.RegisterRequest;
import com.example.food.food.model.Role;
import com.example.food.food.model.user;

public interface UserService {

	user register(RegisterRequest request);

	user registerUser(user u);

	AuthResponse login(LoginRequest request);

	user getUserById(int id);

	user getUserByEmail(String email);

	List<user> getUsersByRole(Role role);

	List<user> getAllUsers();

	user updateAvailability(int userId, boolean isAvailable);
}
