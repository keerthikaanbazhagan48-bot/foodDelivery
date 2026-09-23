package com.example.food.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.dto.RestaurantRequest;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.food.model.resturant;
import com.example.food.foodRepo.resturantRepo;
import com.example.food.service.RestaurantService;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private resturantRepo restaurantRepository;

	@Override
	public resturant addRestaurant(RestaurantRequest request) {
		resturant r = new resturant();
		r.setName(request.getName());
		r.setLocation(request.getLocation());
		r.setIsOpen(request.getIsOpen() != null ? request.getIsOpen() : true);
		return restaurantRepository.save(r);
	}

	@Override
	public resturant saveRestaurant(resturant r) {
		if (r.getIsOpen() == null) {
			r.setIsOpen(true);
		}
		return restaurantRepository.save(r);
	}

	@Override
	@Transactional(readOnly = true)
	public resturant getRestaurantById(int id) {
		return restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<resturant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<resturant> getOpenRestaurants() {
		return restaurantRepository.findByIsOpen(true);
	}

	@Override
	@Transactional(readOnly = true)
	public List<resturant> searchByLocation(String location) {
		return restaurantRepository.findByLocationContainingIgnoreCase(location);
	}

	@Override
	public resturant updateStatus(int id, boolean isOpen) {
		resturant r = getRestaurantById(id);
		r.setIsOpen(isOpen);
		return restaurantRepository.save(r);
	}
}
