package com.example.food.service;

import java.util.List;

import com.example.food.dto.OrderRequest;
import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;

public interface OrderService {

	foodOrder createOrder(int customerId, List<Integer> foodIds);

	foodOrder createOrder(int customerId, OrderRequest request);

	foodOrder getOrderById(int orderId);

	List<foodOrder> getAllOrders();

	List<foodOrder> getOrdersByCustomerId(int customerId);

	List<foodOrder> getOrdersByStatus(orderStatus status);

	foodOrder updateOrderStatus(int orderId, orderStatus status);

	foodOrder cancelOrder(int orderId);
}
