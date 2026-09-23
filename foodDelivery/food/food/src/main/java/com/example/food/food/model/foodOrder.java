package com.example.food.food.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_orders")
public class foodOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private user customer;

	@ManyToOne
	@JoinColumn(name = "delivery_partner_id")
	private user deliverypartner;

	@ManyToMany
	@JoinTable(
		name = "order_food_items",
		joinColumns = @JoinColumn(name = "order_id"),
		inverseJoinColumns = @JoinColumn(name = "food_item_id")
	)
	private List<foodItem> items = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private orderStatus status = orderStatus.PLACED;

	@Column(name = "total_amount")
	private int totalAmount;

	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@PrePersist
	protected void onCreate() {
		if (this.orderTime == null) {
			this.orderTime = LocalDateTime.now();
		}
	}

	public foodOrder() {
		super();
		this.status = orderStatus.PLACED;
	}

	public foodOrder(user customer, user deliverypartner, List<foodItem> items, orderStatus status) {
		super();
		this.customer = customer;
		this.deliverypartner = deliverypartner;
		this.items = items;
		this.status = status != null ? status : orderStatus.PLACED;
		this.orderTime = LocalDateTime.now();
		calculateTotal();
	}

	public void calculateTotal() {
		if (items != null) {
			this.totalAmount = items.stream().mapToInt(foodItem::getPrice).sum();
		} else {
			this.totalAmount = 0;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public user getCustomer() {
		return customer;
	}

	public void setCustomer(user customer) {
		this.customer = customer;
	}

	public user getDeliverypartner() {
		return deliverypartner;
	}

	public void setDeliverypartner(user deliverypartner) {
		this.deliverypartner = deliverypartner;
	}

	public List<foodItem> getItems() {
		return items;
	}

	public void setItems(List<foodItem> items) {
		this.items = items;
		calculateTotal();
	}

	public orderStatus getStatus() {
		return status;
	}

	public void setStatus(orderStatus status) {
		this.status = status;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}
}
