package com.example.food.food.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
public class resturant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String location;

	@Column(name = "is_open")
	private Boolean isOpen = true;

	public resturant() {
		super();
	}

	public resturant(String name, String location) {
		super();
		this.name = name;
		this.location = location;
		this.isOpen = true;
	}

	public resturant(int id, String name, String location, Boolean isOpen) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.isOpen = isOpen != null ? isOpen : true;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}
}
