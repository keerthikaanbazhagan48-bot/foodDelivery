package com.example.food.foodController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;
import com.example.food.food.model.user;
import com.example.food.service.DeliveryService;
import com.example.food.service.UserService;

@RestController
@RequestMapping("/delivery")
public class deliveryController {

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private UserService userService;

	@GetMapping("/orders/{partnerId}")
	public List<foodOrder> get6(@PathVariable int partnerId) {
		return deliveryService.getOrdersForPartner(partnerId);
	}

	@PutMapping("/status/{orderId}")
	public ResponseEntity<foodOrder> get7(@PathVariable int orderId) {
		foodOrder updated = deliveryService.completeDelivery(orderId);
		return ResponseEntity.ok(updated);
	}

	@PutMapping("/status/{orderId}/update")
	public ResponseEntity<foodOrder> updateStatus(
			@PathVariable int orderId,
			@RequestParam orderStatus status) {
		foodOrder updated = deliveryService.updateDeliveryStatus(orderId, status);
		return ResponseEntity.ok(updated);
	}

	@PutMapping("/availability/{partnerId}")
	public ResponseEntity<user> toggleAvailability(
			@PathVariable int partnerId,
			@RequestParam boolean isAvailable) {
		user updated = userService.updateAvailability(partnerId, isAvailable);
		return ResponseEntity.ok(updated);
	}
}
