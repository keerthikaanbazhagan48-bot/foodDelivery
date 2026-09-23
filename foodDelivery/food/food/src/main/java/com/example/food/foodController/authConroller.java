package com.example.food.foodController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.dto.AuthResponse;
import com.example.food.dto.LoginRequest;
import com.example.food.dto.RegisterRequest;
import com.example.food.food.model.user;
import com.example.food.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class authConroller {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<user> register(@RequestBody user u) {
		user registeredUser = userService.registerUser(u);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PostMapping("/register-dto")
	public ResponseEntity<user> registerDto(@Valid @RequestBody RegisterRequest request) {
		user registeredUser = userService.register(request);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = userService.login(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<user> getUserById(@PathVariable int id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}
}
