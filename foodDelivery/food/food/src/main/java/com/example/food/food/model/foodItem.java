package com.example.food.food.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_items")
public class foodItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int price;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private resturant rest;

	public foodItem() {
		super();
	}

	public foodItem(String name, int price, resturant rest) {
		super();
		this.name = name;
		this.price = price;
		this.rest = rest;
	}

	public foodItem(int id, String name, int price, resturant rest) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.rest = rest;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public resturant getRest() {
		return rest;
	}

	public void setRest(resturant rest) {
		this.rest = rest;
	}
}
