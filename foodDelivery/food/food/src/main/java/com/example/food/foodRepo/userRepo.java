package com.example.food.foodRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.food.food.model.Role;
import com.example.food.food.model.user;

@Repository
public interface userRepo extends JpaRepository<user, Integer> {

	Optional<user> findByEmailId(String emailId);

	boolean existsByEmailId(String emailId);

	List<user> findByRole(Role role);

	List<user> findByRoleAndIsAvailable(Role role, Boolean isAvailable);
}
