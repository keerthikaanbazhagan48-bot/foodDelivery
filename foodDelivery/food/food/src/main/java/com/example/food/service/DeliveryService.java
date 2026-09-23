package com.example.food.service;

import java.util.List;

import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;

public interface DeliveryService {

	foodOrder assignDeliveryPartner(int orderId, int partnerId);

	List<foodOrder> getOrdersForPartner(int partnerId);

	foodOrder updateDeliveryStatus(int orderId, orderStatus status);

	foodOrder completeDelivery(int orderId);
}
