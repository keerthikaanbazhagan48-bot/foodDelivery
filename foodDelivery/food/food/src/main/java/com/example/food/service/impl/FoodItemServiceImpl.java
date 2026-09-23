package com.example.food.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.dto.FoodItemRequest;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.food.model.foodItem;
import com.example.food.food.model.resturant;
import com.example.food.foodRepo.foodItemRepo;
import com.example.food.service.FoodItemService;
import com.example.food.service.RestaurantService;

@Service
@Transactional
public class FoodItemServiceImpl implements FoodItemService {

	@Autowired
	private foodItemRepo foodItemRepository;

	@Autowired
	private RestaurantService restaurantService;

	@Override
	public foodItem addFoodItem(FoodItemRequest request) {
		resturant restaurant = restaurantService.getRestaurantById(request.getRestaurantId());
		foodItem item = new foodItem();
		item.setName(request.getName());
		item.setPrice(request.getPrice());
		item.setRest(restaurant);
		return foodItemRepository.save(item);
	}

	@Override
	public foodItem saveFoodItem(foodItem f) {
		if (f.getRest() != null && f.getRest().getId() > 0) {
			resturant restaurant = restaurantService.getRestaurantById(f.getRest().getId());
			f.setRest(restaurant);
		}
		return foodItemRepository.save(f);
	}

	@Override
	@Transactional(readOnly = true)
	public foodItem getFoodItemById(int id) {
		return foodItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<foodItem> getAllFoodItems() {
		return foodItemRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<foodItem> getFoodItemsByRestaurantId(int restaurantId) {
		return foodItemRepository.findByRestId(restaurantId);
	}
}
