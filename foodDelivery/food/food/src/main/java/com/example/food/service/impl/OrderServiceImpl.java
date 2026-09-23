package com.example.food.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.dto.OrderRequest;
import com.example.food.exception.BadRequestException;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.food.model.foodItem;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;
import com.example.food.food.model.user;
import com.example.food.foodRepo.foodItemRepo;
import com.example.food.foodRepo.foodOrderRepo;
import com.example.food.service.OrderService;
import com.example.food.service.UserService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private foodOrderRepo orderRepository;

	@Autowired
	private foodItemRepo foodItemRepository;

	@Autowired
	private UserService userService;

	@Override
	public foodOrder createOrder(int customerId, List<Integer> foodIds) {
		if (foodIds == null || foodIds.isEmpty()) {
			throw new BadRequestException("Order must contain at least one food item");
		}

		user customer = userService.getUserById(customerId);

		List<foodItem> items = foodItemRepository.findAllById(foodIds);
		if (items.isEmpty()) {
			throw new ResourceNotFoundException("None of the specified food items were found");
		}
		if (items.size() != foodIds.size()) {
			throw new BadRequestException("Some specified food items could not be found");
		}

		foodOrder order = new foodOrder();
		order.setCustomer(customer);
		order.setItems(items);
		order.setStatus(orderStatus.PLACED);
		order.setOrderTime(LocalDateTime.now());
		order.calculateTotal();

		return orderRepository.save(order);
	}

	@Override
	public foodOrder createOrder(int customerId, OrderRequest request) {
		return createOrder(customerId, request.getFoodIds());
	}

	@Override
	@Transactional(readOnly = true)
	public foodOrder getOrderById(int orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<foodOrder> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<foodOrder> getOrdersByCustomerId(int customerId) {
		userService.getUserById(customerId);
		return orderRepository.findByCustomerId(customerId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<foodOrder> getOrdersByStatus(orderStatus status) {
		return orderRepository.findByStatus(status);
	}

	@Override
	public foodOrder updateOrderStatus(int orderId, orderStatus status) {
		foodOrder order = getOrderById(orderId);
		order.setStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public foodOrder cancelOrder(int orderId) {
		foodOrder order = getOrderById(orderId);
		if (order.getStatus() == orderStatus.DELIVERED) {
			throw new BadRequestException("Cannot cancel an order that has already been delivered");
		}
		if (order.getStatus() == orderStatus.CANCELLED) {
			throw new BadRequestException("Order is already cancelled");
		}
		order.setStatus(orderStatus.CANCELLED);
		return orderRepository.save(order);
	}
}
