package com.techelevator;

import java.util.LinkedList;
import java.util.Queue;

public class Slot {
	private Queue<Product> products;
	private Product displayItem;
	
	public Slot(Product displayItem) {
		products = new LinkedList<Product>();
		this.displayItem = displayItem;
	}
	
	public Product getDisplayItem() {
		return displayItem;
	}

	public int getQty() {
		return products.size();
	}
	
	public void addProduct(Product productToAdd) {
		products.add(productToAdd);
	}
	
	public String vendProduct() {
		return products.poll().getMessage();
	}
}
