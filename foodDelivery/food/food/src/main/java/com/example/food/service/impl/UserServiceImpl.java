package com.example.food.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.dto.AuthResponse;
import com.example.food.dto.LoginRequest;
import com.example.food.dto.RegisterRequest;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.exception.UserAlreadyExistsException;
import com.example.food.food.model.Role;
import com.example.food.food.model.user;
import com.example.food.foodRepo.userRepo;
import com.example.food.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private userRepo userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired(required = false)
	private AuthenticationManager authenticationManager;

	@Override
	public user register(RegisterRequest request) {
		if (userRepository.existsByEmailId(request.getEmailId())) {
			throw new UserAlreadyExistsException("User with email " + request.getEmailId() + " already exists");
		}

		user u = new user();
		u.setName(request.getName());
		u.setEmailId(request.getEmailId());
		u.setPassword(passwordEncoder.encode(request.getPassword()));
		u.setRole(request.getRole());
		u.setIsAvailable(true);

		return userRepository.save(u);
	}

	@Override
	public user registerUser(user u) {
		if (userRepository.existsByEmailId(u.getEmailId())) {
			throw new UserAlreadyExistsException("User with email " + u.getEmailId() + " already exists");
		}
		if (u.getPassword() != null && !u.getPassword().isBlank()) {
			u.setPassword(passwordEncoder.encode(u.getPassword()));
		}
		if (u.getRole() == null) {
			u.setRole(Role.CUSTOMER);
		}
		if (u.getIsAvailable() == null) {
			u.setIsAvailable(true);
		}
		return userRepository.save(u);
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		if (authenticationManager != null) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmailId(), request.getPassword())
			);
		}

		user u = userRepository.findByEmailId(request.getEmailId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmailId()));

		if (authenticationManager == null) {
			if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
				throw new org.springframework.security.authentication.BadCredentialsException("Invalid password");
			}
		}

		return new AuthResponse(
				u.getId(),
				u.getName(),
				u.getEmailId(),
				u.getRole(),
				"Login successful"
		);
	}

	@Override
	@Transactional(readOnly = true)
	public user getUserById(int id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public user getUserByEmail(String email) {
		return userRepository.findByEmailId(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
	}

	@Override
	@Transactional(readOnly = true)
	public List<user> getUsersByRole(Role role) {
		return userRepository.findByRole(role);
	}

	@Override
	@Transactional(readOnly = true)
	public List<user> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public user updateAvailability(int userId, boolean isAvailable) {
		user u = getUserById(userId);
		u.setIsAvailable(isAvailable);
		return userRepository.save(u);
	}
}
