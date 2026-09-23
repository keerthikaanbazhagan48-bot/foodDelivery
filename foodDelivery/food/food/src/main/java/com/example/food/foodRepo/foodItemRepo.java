package com.example.food.foodRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.food.food.model.foodItem;

@Repository
public interface foodItemRepo extends JpaRepository<foodItem, Integer> {

	List<foodItem> findByRestId(int restId);
}
