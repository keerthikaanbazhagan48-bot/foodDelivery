package com.example.food.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FoodItemRequest {

	@NotBlank(message = "Food name cannot be blank")
	private String name;

	@Min(value = 1, message = "Price must be greater than 0")
	private int price;

	@NotNull(message = "Restaurant ID is required")
	private Integer restaurantId;

	public FoodItemRequest() {
	}

	public FoodItemRequest(String name, int price, Integer restaurantId) {
		this.name = name;
		this.price = price;
		this.restaurantId = restaurantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Integer getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
}
