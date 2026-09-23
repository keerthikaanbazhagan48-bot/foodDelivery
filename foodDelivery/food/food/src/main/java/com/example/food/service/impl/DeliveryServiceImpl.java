package com.example.food.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.exception.BadRequestException;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.food.model.Role;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;
import com.example.food.food.model.user;
import com.example.food.foodRepo.foodOrderRepo;
import com.example.food.service.DeliveryService;
import com.example.food.service.OrderService;
import com.example.food.service.UserService;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private foodOrderRepo orderRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Override
	public foodOrder assignDeliveryPartner(int orderId, int partnerId) {
		foodOrder order = orderService.getOrderById(orderId);

		if (order.getStatus() == orderStatus.DELIVERED || order.getStatus() == orderStatus.CANCELLED) {
			throw new BadRequestException("Cannot assign delivery partner to an order with status: " + order.getStatus());
		}

		user partner = userService.getUserById(partnerId);
		if (partner.getRole() != Role.DELIVERY_PARTNER) {
			throw new BadRequestException("User with id " + partnerId + " is not a DELIVERY_PARTNER");
		}

		// Check if partner is already assigned to an active conflicting order
		List<orderStatus> activeStatuses = Arrays.asList(orderStatus.PLACED, orderStatus.PREPARING, orderStatus.OUT_OF_DELIVERY);
		List<foodOrder> activeOrders = orderRepository.findByDeliverypartnerIdAndStatusIn(partnerId, activeStatuses);
		
		boolean hasOtherActiveOrder = activeOrders.stream().anyMatch(o -> o.getId() != orderId);
		if (hasOtherActiveOrder) {
			throw new BadRequestException("Delivery partner " + partner.getName() + " already has an active order in progress");
		}

		order.setDeliverypartner(partner);
		order.setStatus(orderStatus.OUT_OF_DELIVERY);

		return orderRepository.save(order);
	}

	@Override
	@Transactional(readOnly = true)
	public List<foodOrder> getOrdersForPartner(int partnerId) {
		userService.getUserById(partnerId);
		return orderRepository.findByDeliverypartnerId(partnerId);
	}

	@Override
	public foodOrder updateDeliveryStatus(int orderId, orderStatus status) {
		foodOrder order = orderService.getOrderById(orderId);
		if (order.getDeliverypartner() == null) {
			throw new BadRequestException("Cannot update delivery status: No delivery partner assigned to order " + orderId);
		}
		order.setStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public foodOrder completeDelivery(int orderId) {
		return updateDeliveryStatus(orderId, orderStatus.DELIVERED);
	}
}
