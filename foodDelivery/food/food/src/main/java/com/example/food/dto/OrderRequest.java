package com.example.food.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class OrderRequest {

	@NotEmpty(message = "Order must contain at least one food item ID")
	private List<Integer> foodIds;

	public OrderRequest() {
	}

	public OrderRequest(List<Integer> foodIds) {
		this.foodIds = foodIds;
	}

	public List<Integer> getFoodIds() {
		return foodIds;
	}

	public void setFoodIds(List<Integer> foodIds) {
		this.foodIds = foodIds;
	}
}
