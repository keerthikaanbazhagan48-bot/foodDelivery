package com.example.food.foodRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.food.food.model.resturant;

@Repository
public interface resturantRepo extends JpaRepository<resturant, Integer> {

	List<resturant> findByLocationContainingIgnoreCase(String location);

	List<resturant> findByIsOpen(Boolean isOpen);
}
