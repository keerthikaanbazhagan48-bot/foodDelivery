package com.example.food.foodController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.dto.FoodItemRequest;
import com.example.food.dto.RestaurantRequest;
import com.example.food.food.model.Role;
import com.example.food.food.model.foodItem;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.resturant;
import com.example.food.food.model.user;
import com.example.food.service.DeliveryService;
import com.example.food.service.FoodItemService;
import com.example.food.service.OrderService;
import com.example.food.service.RestaurantService;
import com.example.food.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class adminController {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private FoodItemService foodItemService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private UserService userService;

	@PostMapping("/addresturant")
	public ResponseEntity<resturant> get1(@RequestBody resturant r) {
		resturant saved = restaurantService.saveRestaurant(r);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@PostMapping("/addresturant-dto")
	public ResponseEntity<resturant> addRestaurantDto(@Valid @RequestBody RestaurantRequest request) {
		resturant saved = restaurantService.addRestaurant(request);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@PostMapping("/addfood")
	public ResponseEntity<foodItem> get2(@RequestBody foodItem f) {
		foodItem saved = foodItemService.saveFoodItem(f);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@PostMapping("/addfood-dto")
	public ResponseEntity<foodItem> addFoodDto(@Valid @RequestBody FoodItemRequest request) {
		foodItem saved = foodItemService.addFoodItem(request);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@PutMapping("/assign/{orderId}/{partnerId}")
	public ResponseEntity<foodOrder> get3(@PathVariable int orderId, @PathVariable int partnerId) {
		foodOrder assignedOrder = deliveryService.assignDeliveryPartner(orderId, partnerId);
		return ResponseEntity.ok(assignedOrder);
	}

	@GetMapping("/restaurants")
	public ResponseEntity<List<resturant>> getAllRestaurants() {
		return ResponseEntity.ok(restaurantService.getAllRestaurants());
	}

	@GetMapping("/foods")
	public ResponseEntity<List<foodItem>> getAllFoods() {
		return ResponseEntity.ok(foodItemService.getAllFoodItems());
	}

	@GetMapping("/orders")
	public ResponseEntity<List<foodOrder>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	@GetMapping("/delivery-partners")
	public ResponseEntity<List<user>> getDeliveryPartners() {
		return ResponseEntity.ok(userService.getUsersByRole(Role.DELIVERY_PARTNER));
	}
}
