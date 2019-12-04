package com.techelevator;

import java.math.BigDecimal;

public class Product {
	
	private String name;
	private BigDecimal price;
	private ProductType type;

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public ProductType getType() {
		return type;
	}

	public Product(String name, BigDecimal price, ProductType type) {
		this.name = name;
		this.price = price;
		this.type = type;
	}
	
	public String getMessage() {
		switch(type) {
		case CANDY:
			return "Munch Munch, Yum!";
		case CHIP:
			return "Crunch Crunch, Yum!";
		case DRINK:
			return "Glug Glug, Yum!";
		case GUM:
			return "Chew Chew, Yum!";
		}
		return "";
	}

}
