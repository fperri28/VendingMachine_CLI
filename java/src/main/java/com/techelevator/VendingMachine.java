package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class VendingMachine {
	private Map<String, Slot> slotMap;
	private AuditLog log;
	private BigDecimal currentBalance;
	private BigDecimal totalSales;
	
	public VendingMachine() {
		slotMap = new TreeMap<>();
		try {
			log = new AuditLog();
		} catch (IOException e) {
			System.out.println("Unable to create audit log file");
			e.printStackTrace();
			System.exit(1);
		}
		currentBalance = new BigDecimal("0.00");
		totalSales = new BigDecimal("0.00");
		loadInventory();
	}

	private void loadInventory() {
		File dataFile = new File("vendingmachine.csv");
		
		try (Scanner scanner = new Scanner(dataFile)) {
			
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("\\|");
				String slotID = line[0];
				String name = line[1];
				BigDecimal price = new BigDecimal(line[2]);
				ProductType type = ProductType.valueOf(line[3].toUpperCase());
				
				if (!slotMap.containsKey(slotID)) {
					slotMap.put(slotID, new Slot(new Product(name, price, type)));
				}
				
				for (int i = 0; i < 5; i++) {
					slotMap.get(slotID).addProduct(new Product(name, price, type));
				}
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void feedMoney(BigDecimal moneyFed) {
		currentBalance = currentBalance.add(moneyFed);
		log.addLogEntry("FEED MONEY: $" + moneyFed + " $" + currentBalance);
		
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public String finishTransaction() {
		int quarter = 0;
		int dime = 0;
		int nickel = 0;
		
		log.addLogEntry("GIVE CHANGE: $" + currentBalance + " $0.00");
		while (currentBalance.compareTo(new BigDecimal("0.25")) >= 0) {
			quarter++;
			currentBalance = currentBalance.subtract(new BigDecimal("0.25"));
		}
		while (currentBalance.compareTo(new BigDecimal("0.10")) >= 0) {
			dime++;
			currentBalance = currentBalance.subtract(new BigDecimal("0.10"));
		}
		while (currentBalance.compareTo(new BigDecimal("0.05")) >= 0) {
			nickel++;
			currentBalance = currentBalance.subtract(new BigDecimal("0.05"));
		}
		return quarter + ";" + dime + ";" + nickel;
	}

	public LinkedList<String> getItemList() {
		LinkedList<String> result = new LinkedList<String>();
		for (Map.Entry<String, Slot> entry : slotMap.entrySet()) {
			String line = (String) entry.getKey() + "," + ((Slot) entry.getValue()).getDisplayItem().getName()
					+ "," + ((Slot) entry.getValue()).getQty() + "," + ((Slot) entry.getValue()).getDisplayItem().getPrice() ;
			result.add(line);
		}
		return result;
	}

	public String purchase(String slotID) {
		String message = "";
		slotID = slotID.toUpperCase();

		if (slotMap.containsKey(slotID)) {
			Slot slot = slotMap.get(slotID);
			Product product = slot.getDisplayItem();
			BigDecimal price = product.getPrice();
					
			if (currentBalance.compareTo(price) >= 0) {
				if (slot.getQty() > 0) {
					message = product.getName() + ";" + product.getPrice() + ";" + slot.vendProduct();
					BigDecimal beforeBalance = new BigDecimal(currentBalance.toString());
					currentBalance = currentBalance.subtract(price);
					totalSales = totalSales.add(price);
					log.addLogEntry(product.getName() + " " + slotID + " $" + beforeBalance + " $" + currentBalance);
				} else {
					message = "Out of Stock";
				}
			} else {
				message = "Insufficient Funds";
			}
		} else {
			message = "Invalid Slot ID";
		}

		return message;
	}

	public void printSales() {
		List <String> entryList = new LinkedList<String> ();
		for(Slot slot : slotMap.values()) {
			Product prod = slot.getDisplayItem();
			entryList.add(prod.getName() + "|" + (5 - slot.getQty()));
		}
		String reportBody = "";
		for(String entry : entryList) {
			reportBody += entry + "\n";
		}
		
		reportBody += "\nTotal Sales: $" + totalSales; 
		try {
			SalesReport report = new SalesReport();
			report.printSales(reportBody);
		} catch (IOException e) {
			System.out.println("Cannot create Sales Report");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
