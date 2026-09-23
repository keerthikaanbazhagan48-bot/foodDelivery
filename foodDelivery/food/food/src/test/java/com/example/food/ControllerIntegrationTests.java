package com.example.food;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.dto.AuthResponse;
import com.example.food.dto.FoodItemRequest;
import com.example.food.dto.LoginRequest;
import com.example.food.dto.RegisterRequest;
import com.example.food.dto.RestaurantRequest;
import com.example.food.exception.BadRequestException;
import com.example.food.exception.UserAlreadyExistsException;
import com.example.food.food.model.Role;
import com.example.food.food.model.foodItem;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;
import com.example.food.food.model.resturant;
import com.example.food.food.model.user;
import com.example.food.foodController.adminController;
import com.example.food.foodController.authConroller;
import com.example.food.foodController.customerController;
import com.example.food.foodController.deliveryController;

@SpringBootTest
@Transactional
class ControllerIntegrationTests {

	@Autowired
	private authConroller authController;

	@Autowired
	private adminController adminController;

	@Autowired
	private customerController customerController;

	@Autowired
	private deliveryController deliveryController;

	@Test
	void testAuthControllerEndpoints() {
		user newUser = new user("John Doe", "john@example.com", "pass123", Role.CUSTOMER);
		ResponseEntity<user> regResponse = authController.register(newUser);
		assertEquals(HttpStatus.CREATED, regResponse.getStatusCode());
		assertNotNull(regResponse.getBody());
		assertEquals("John Doe", regResponse.getBody().getName());

		RegisterRequest dtoRequest = new RegisterRequest("Jane Doe", "jane@example.com", "pass123", Role.DELIVERY_PARTNER);
		ResponseEntity<user> regDtoResponse = authController.registerDto(dtoRequest);
		assertEquals(HttpStatus.CREATED, regDtoResponse.getStatusCode());

		// Login
		LoginRequest loginRequest = new LoginRequest("john@example.com", "pass123");
		ResponseEntity<AuthResponse> loginResponse = authController.login(loginRequest);
		assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
		assertEquals("Login successful", loginResponse.getBody().getMessage());

		// Duplicate registration exception
		assertThrows(UserAlreadyExistsException.class, () -> authController.register(newUser));
	}

	@Test
	void testAdminControllerEndpoints() {
		resturant r = new resturant("Royal Spice", "Sector 5");
		ResponseEntity<resturant> restResponse = adminController.get1(r);
		assertEquals(HttpStatus.CREATED, restResponse.getStatusCode());
		int restId = restResponse.getBody().getId();

		RestaurantRequest restDto = new RestaurantRequest("Green Garden", "Sector 6", true);
		ResponseEntity<resturant> restDtoResp = adminController.addRestaurantDto(restDto);
		assertEquals(HttpStatus.CREATED, restDtoResp.getStatusCode());

		foodItem food = new foodItem("Butter Chicken", 350, restResponse.getBody());
		ResponseEntity<foodItem> foodResponse = adminController.get2(food);
		assertEquals(HttpStatus.CREATED, foodResponse.getStatusCode());

		FoodItemRequest foodDto = new FoodItemRequest("Naan", 40, restId);
		ResponseEntity<foodItem> foodDtoResp = adminController.addFoodDto(foodDto);
		assertEquals(HttpStatus.CREATED, foodDtoResp.getStatusCode());

		ResponseEntity<List<resturant>> allRest = adminController.getAllRestaurants();
		assertEquals(HttpStatus.OK, allRest.getStatusCode());
		assertEquals(2, allRest.getBody().size());
	}

	@Test
	void testCustomerAndDeliveryControllerEndpoints() {
		// Register customer & partner
		user customer = authController.register(new user("Cust A", "custa@example.com", "pwd", Role.CUSTOMER)).getBody();
		user partner = authController.register(new user("Rider A", "ridera@example.com", "pwd", Role.DELIVERY_PARTNER)).getBody();

		// Add restaurant & food
		resturant r = adminController.get1(new resturant("Express Eats", "Park Lane")).getBody();
		foodItem item1 = adminController.get2(new foodItem("Burger", 150, r)).getBody();
		foodItem item2 = adminController.get2(new foodItem("Fries", 80, r)).getBody();

		// Customer views food list
		List<foodItem> foods = customerController.get4();
		assertNotNull(foods);
		assertEquals(2, foods.size());

		// Customer places order
		ResponseEntity<foodOrder> orderResp = customerController.get5(customer.getId(), Arrays.asList(item1.getId(), item2.getId()));
		assertEquals(HttpStatus.CREATED, orderResp.getStatusCode());
		foodOrder placedOrder = orderResp.getBody();
		assertNotNull(placedOrder);
		assertEquals(230, placedOrder.getTotalAmount()); // 150 + 80
		assertEquals(orderStatus.PLACED, placedOrder.getStatus());

		// Customer checks order history
		ResponseEntity<List<foodOrder>> customerOrders = customerController.getCustomerOrders(customer.getId());
		assertEquals(HttpStatus.OK, customerOrders.getStatusCode());
		assertEquals(1, customerOrders.getBody().size());

		// Admin assigns delivery partner
		ResponseEntity<foodOrder> assignResp = adminController.get3(placedOrder.getId(), partner.getId());
		assertEquals(HttpStatus.OK, assignResp.getStatusCode());
		assertEquals(orderStatus.OUT_OF_DELIVERY, assignResp.getBody().getStatus());
		assertEquals(partner.getId(), assignResp.getBody().getDeliverypartner().getId());

		// Delivery partner views assigned orders
		List<foodOrder> partnerOrders = deliveryController.get6(partner.getId());
		assertEquals(1, partnerOrders.size());
		assertEquals(placedOrder.getId(), partnerOrders.get(0).getId());

		// Delivery partner marks order as DELIVERED
		ResponseEntity<foodOrder> deliveredResp = deliveryController.get7(placedOrder.getId());
		assertEquals(HttpStatus.OK, deliveredResp.getStatusCode());
		assertEquals(orderStatus.DELIVERED, deliveredResp.getBody().getStatus());
	}

	@Test
	void testDeliveryConflictViaControllers() {
		user customer = authController.register(new user("Cust B", "custb@example.com", "pwd", Role.CUSTOMER)).getBody();
		user partner = authController.register(new user("Rider B", "riderb@example.com", "pwd", Role.DELIVERY_PARTNER)).getBody();
		resturant r = adminController.get1(new resturant("Pizza Express", "Main Street")).getBody();
		foodItem item = adminController.get2(new foodItem("Pizza Slice", 100, r)).getBody();

		foodOrder order1 = customerController.get5(customer.getId(), List.of(item.getId())).getBody();
		foodOrder order2 = customerController.get5(customer.getId(), List.of(item.getId())).getBody();

		// Assign partner to order1
		adminController.get3(order1.getId(), partner.getId());

		// Assign same partner to order2 while order1 is active should fail
		assertThrows(BadRequestException.class, () -> adminController.get3(order2.getId(), partner.getId()));
	}
}
