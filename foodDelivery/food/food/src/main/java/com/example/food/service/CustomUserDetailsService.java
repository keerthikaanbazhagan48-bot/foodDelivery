package com.example.food.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.food.food.model.user;
import com.example.food.foodRepo.userRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private userRepo userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		user u = userRepository.findByEmailId(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		String roleName = u.getRole() != null ? u.getRole().name() : "CUSTOMER";
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

		return new User(
				u.getEmailId(),
				u.getPassword(),
				Collections.singletonList(authority)
		);
	}
}
