package com.example.food.service;

import java.util.List;

import com.example.food.dto.FoodItemRequest;
import com.example.food.food.model.foodItem;

public interface FoodItemService {

	foodItem addFoodItem(FoodItemRequest request);

	foodItem saveFoodItem(foodItem f);

	foodItem getFoodItemById(int id);

	List<foodItem> getAllFoodItems();

	List<foodItem> getFoodItemsByRestaurantId(int restaurantId);
}
