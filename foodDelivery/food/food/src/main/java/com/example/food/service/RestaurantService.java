package com.example.food.service;

import java.util.List;

import com.example.food.dto.RestaurantRequest;
import com.example.food.food.model.resturant;

public interface RestaurantService {

	resturant addRestaurant(RestaurantRequest request);

	resturant saveRestaurant(resturant r);

	resturant getRestaurantById(int id);

	List<resturant> getAllRestaurants();

	List<resturant> getOpenRestaurants();

	List<resturant> searchByLocation(String location);

	resturant updateStatus(int id, boolean isOpen);
}
