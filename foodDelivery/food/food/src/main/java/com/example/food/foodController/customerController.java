package com.example.food.foodController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.dto.OrderRequest;
import com.example.food.food.model.foodItem;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.resturant;
import com.example.food.service.FoodItemService;
import com.example.food.service.OrderService;
import com.example.food.service.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class customerController {

	@Autowired
	private FoodItemService foodItemService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private RestaurantService restaurantService;

	@GetMapping("/showfoods")
	public List<foodItem> get4() {
		return foodItemService.getAllFoodItems();
	}

	@PostMapping("/order/{customerId}")
	public ResponseEntity<foodOrder> get5(@PathVariable int customerId, @RequestBody List<Integer> foodIds) {
		foodOrder createdOrder = orderService.createOrder(customerId, foodIds);
		return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
	}

	@PostMapping("/order-dto/{customerId}")
	public ResponseEntity<foodOrder> placeOrderDto(@PathVariable int customerId, @Valid @RequestBody OrderRequest request) {
		foodOrder createdOrder = orderService.createOrder(customerId, request);
		return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
	}

	@GetMapping("/restaurants")
	public ResponseEntity<List<resturant>> getOpenRestaurants() {
		return ResponseEntity.ok(restaurantService.getOpenRestaurants());
	}

	@GetMapping("/foods/restaurant/{restaurantId}")
	public ResponseEntity<List<foodItem>> getFoodsByRestaurant(@PathVariable int restaurantId) {
		return ResponseEntity.ok(foodItemService.getFoodItemsByRestaurantId(restaurantId));
	}

	@GetMapping("/orders/{customerId}")
	public ResponseEntity<List<foodOrder>> getCustomerOrders(@PathVariable int customerId) {
		return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
	}

	@GetMapping("/order/status/{orderId}")
	public ResponseEntity<foodOrder> getOrderStatus(@PathVariable int orderId) {
		return ResponseEntity.ok(orderService.getOrderById(orderId));
	}
}
