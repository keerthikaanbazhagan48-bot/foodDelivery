package com.example.food;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.dto.AuthResponse;
import com.example.food.dto.FoodItemRequest;
import com.example.food.dto.LoginRequest;
import com.example.food.dto.RegisterRequest;
import com.example.food.dto.RestaurantRequest;
import com.example.food.exception.BadRequestException;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.exception.UserAlreadyExistsException;
import com.example.food.food.model.Role;
import com.example.food.food.model.foodItem;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;
import com.example.food.food.model.resturant;
import com.example.food.food.model.user;
import com.example.food.service.DeliveryService;
import com.example.food.service.FoodItemService;
import com.example.food.service.OrderService;
import com.example.food.service.RestaurantService;
import com.example.food.service.UserService;

@SpringBootTest
@Transactional
class FoodApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private FoodItemService foodItemService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
		assertNotNull(userService);
		assertNotNull(restaurantService);
		assertNotNull(foodItemService);
		assertNotNull(orderService);
		assertNotNull(deliveryService);
	}

	@Test
	void testUserRegistrationAndLogin() {
		RegisterRequest regRequest = new RegisterRequest("Alice", "alice@example.com", "secret123", Role.CUSTOMER);
		user registeredUser = userService.register(regRequest);

		assertNotNull(registeredUser);
		assertEquals("Alice", registeredUser.getName());
		assertEquals("alice@example.com", registeredUser.getEmailId());
		assertEquals(Role.CUSTOMER, registeredUser.getRole());
		assertTrue(passwordEncoder.matches("secret123", registeredUser.getPassword()));

		// Duplicate registration test
		assertThrows(UserAlreadyExistsException.class, () -> userService.register(regRequest));

		// Login test
		LoginRequest loginRequest = new LoginRequest("alice@example.com", "secret123");
		AuthResponse loginResponse = userService.login(loginRequest);
		assertNotNull(loginResponse);
		assertEquals("Alice", loginResponse.getName());
		assertEquals("Login successful", loginResponse.getMessage());
	}

	@Test
	void testRestaurantAndFoodItemFlow() {
		RestaurantRequest restRequest = new RestaurantRequest("Spice Garden", "Downtown", true);
		resturant restaurant = restaurantService.addRestaurant(restRequest);
		assertNotNull(restaurant);
		assertTrue(restaurant.getId() > 0);
		assertEquals("Spice Garden", restaurant.getName());

		FoodItemRequest foodRequest1 = new FoodItemRequest("Biryani", 250, restaurant.getId());
		foodItem item1 = foodItemService.addFoodItem(foodRequest1);

		FoodItemRequest foodRequest2 = new FoodItemRequest("Paneer Tikka", 180, restaurant.getId());
		foodItem item2 = foodItemService.addFoodItem(foodRequest2);

		List<foodItem> restaurantFoods = foodItemService.getFoodItemsByRestaurantId(restaurant.getId());
		assertEquals(2, restaurantFoods.size());
	}

	@Test
	void testOrderCreationCalculationAndHistory() {
		user customer = userService.register(new RegisterRequest("Bob", "bob@example.com", "pass123", Role.CUSTOMER));
		resturant restaurant = restaurantService.addRestaurant(new RestaurantRequest("Pizza House", "Midtown", true));
		foodItem pizza = foodItemService.addFoodItem(new FoodItemRequest("Margherita Pizza", 300, restaurant.getId()));
		foodItem garlicBread = foodItemService.addFoodItem(new FoodItemRequest("Garlic Bread", 120, restaurant.getId()));

		foodOrder order = orderService.createOrder(customer.getId(), Arrays.asList(pizza.getId(), garlicBread.getId()));

		assertNotNull(order);
		assertEquals(orderStatus.PLACED, order.getStatus());
		assertEquals(420, order.getTotalAmount()); // 300 + 120
		assertEquals(customer.getId(), order.getCustomer().getId());
		assertEquals(2, order.getItems().size());
		assertNotNull(order.getOrderTime());

		List<foodOrder> customerOrders = orderService.getOrdersByCustomerId(customer.getId());
		assertEquals(1, customerOrders.size());
		assertEquals(order.getId(), customerOrders.get(0).getId());
	}

	@Test
	void testDeliveryPartnerAssignmentAndConflictPrevention() {
		user customer = userService.register(new RegisterRequest("Carol", "carol@example.com", "pass123", Role.CUSTOMER));
		user partner = userService.register(new RegisterRequest("Dave Rider", "dave@example.com", "rider123", Role.DELIVERY_PARTNER));
		resturant restaurant = restaurantService.addRestaurant(new RestaurantRequest("Burger Hub", "Uptown", true));
		foodItem burger = foodItemService.addFoodItem(new FoodItemRequest("Cheese Burger", 150, restaurant.getId()));

		foodOrder order1 = orderService.createOrder(customer.getId(), List.of(burger.getId()));
		foodOrder order2 = orderService.createOrder(customer.getId(), List.of(burger.getId()));

		// Assign partner to order 1 -> status becomes OUT_OF_DELIVERY
		foodOrder assignedOrder1 = deliveryService.assignDeliveryPartner(order1.getId(), partner.getId());
		assertEquals(orderStatus.OUT_OF_DELIVERY, assignedOrder1.getStatus());
		assertEquals(partner.getId(), assignedOrder1.getDeliverypartner().getId());

		// Attempting to assign same partner to order 2 while order 1 is still active -> throws BadRequestException
		assertThrows(BadRequestException.class, () -> {
			deliveryService.assignDeliveryPartner(order2.getId(), partner.getId());
		});

		// Attempting to assign a customer as a delivery partner -> throws BadRequestException
		assertThrows(BadRequestException.class, () -> {
			deliveryService.assignDeliveryPartner(order2.getId(), customer.getId());
		});

		// Complete order 1 delivery -> status becomes DELIVERED
		foodOrder deliveredOrder1 = deliveryService.completeDelivery(order1.getId());
		assertEquals(orderStatus.DELIVERED, deliveredOrder1.getStatus());

		// Now partner is free, assigning to order 2 should succeed
		foodOrder assignedOrder2 = deliveryService.assignDeliveryPartner(order2.getId(), partner.getId());
		assertEquals(orderStatus.OUT_OF_DELIVERY, assignedOrder2.getStatus());
		assertEquals(partner.getId(), assignedOrder2.getDeliverypartner().getId());
	}

	@Test
	void testOrderCancellation() {
		user customer = userService.register(new RegisterRequest("Eve", "eve@example.com", "pass123", Role.CUSTOMER));
		resturant restaurant = restaurantService.addRestaurant(new RestaurantRequest("Taco Bar", "Westside", true));
		foodItem taco = foodItemService.addFoodItem(new FoodItemRequest("Veg Taco", 90, restaurant.getId()));

		foodOrder order = orderService.createOrder(customer.getId(), List.of(taco.getId()));
		assertEquals(orderStatus.PLACED, order.getStatus());

		foodOrder cancelled = orderService.cancelOrder(order.getId());
		assertEquals(orderStatus.CANCELLED, cancelled.getStatus());

		// Cancelling again should fail
		assertThrows(BadRequestException.class, () -> orderService.cancelOrder(order.getId()));
	}

	@Test
	void testResourceNotFoundExceptions() {
		assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(9999));
		assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(9999));
		assertThrows(ResourceNotFoundException.class, () -> foodItemService.getFoodItemById(9999));
		assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(9999));
	}
}
