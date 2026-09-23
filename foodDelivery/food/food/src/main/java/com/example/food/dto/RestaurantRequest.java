package com.example.food.dto;

import jakarta.validation.constraints.NotBlank;

public class RestaurantRequest {

	@NotBlank(message = "Restaurant name cannot be blank")
	private String name;

	@NotBlank(message = "Location cannot be blank")
	private String location;

	private Boolean isOpen = true;

	public RestaurantRequest() {
	}

	public RestaurantRequest(String name, String location, Boolean isOpen) {
		this.name = name;
		this.location = location;
		this.isOpen = isOpen != null ? isOpen : true;
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
