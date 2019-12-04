package com.techelevator;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class VendingMachineTest {
	
	VendingMachine machine;

	@Before
	public void setUp() throws Exception {
		machine = new VendingMachine();
	}

	@Test
	public void testFeedMoney() {
		machine.feedMoney(new BigDecimal("2.00"));
		assertEquals("Feeding 2.00 from 0.00 should yield 2.00 balance", new BigDecimal("2.00"), machine.getCurrentBalance());
		machine.feedMoney(new BigDecimal("10.00"));
		assertEquals("Feeding 10.00 from 2.00 should yield 12.00 balance", new BigDecimal("12.00"), machine.getCurrentBalance());
		machine.feedMoney(new BigDecimal("0.00"));
		assertEquals("Feeding 0.00 from 12.00 should yield 12.00 balance", new BigDecimal("12.00"), machine.getCurrentBalance());
	}

	@Test
	public void testFinishTransaction() {
		
		assertEquals("No money should give no change", "0;0;0", machine.finishTransaction());
		
		machine.feedMoney(new BigDecimal("2.00"));
		assertEquals("2.00 should give 8 quarters change", "8;0;0", machine.finishTransaction());
		
		machine.feedMoney(new BigDecimal("0.15"));
		assertEquals("0.15 should give 8 quarters, 1 dime, 1 nickel change", "0;1;1", machine.finishTransaction());
	}

	@Test
	public void testGetItemList() {
		LinkedList<String> expected = new LinkedList<String>();
		expected.add("A1,Potato Crisps,5,3.05");
		expected.add("A2,Stackers,5,1.45");
		expected.add("A3,Grain Waves,5,2.75");
		expected.add("A4,Cloud Popcorn,5,3.65");
		expected.add("B1,Moonpie,5,1.80");
		expected.add("B2,Cowtales,5,1.50");
		expected.add("B3,Wonka Bar,5,1.50");
		expected.add("B4,Crunchie,5,1.75");
		expected.add("C1,Cola,5,1.25");
		expected.add("C2,Dr. Salt,5,1.50");
		expected.add("C3,Mountain Melter,5,1.50");
		expected.add("C4,Heavy,5,1.50");
		expected.add("D1,U-Chews,5,0.85");
		expected.add("D2,Little League Chew,5,0.95");
		expected.add("D3,Chiclets,5,0.75");
		expected.add("D4,Triplemint,5,0.75");
		
		LinkedList<String> actual = machine.getItemList();
		
		for (int i = 0; i < expected.size(); i++) {
			assertEquals("Line doesn't match", expected.get(i), actual.get(i));
		}
		
		expected = new LinkedList<String>();
		expected.add("A1,Potato Crisps,5,3.05");
		expected.add("A2,Stackers,5,1.45");
		expected.add("A3,Grain Waves,5,2.75");
		expected.add("A4,Cloud Popcorn,5,3.65");
		expected.add("B1,Moonpie,5,1.80");
		expected.add("B2,Cowtales,5,1.50");
		expected.add("B3,Wonka Bar,5,1.50");
		expected.add("B4,Crunchie,5,1.75");
		expected.add("C1,Cola,5,1.25");
		expected.add("C2,Dr. Salt,5,1.50");
		expected.add("C3,Mountain Melter,5,1.50");
		expected.add("C4,Heavy,2,1.50");
		expected.add("D1,U-Chews,5,0.85");
		expected.add("D2,Little League Chew,5,0.95");
		expected.add("D3,Chiclets,5,0.75");
		expected.add("D4,Triplemint,5,0.75");
		
		machine.feedMoney(new BigDecimal("10.00"));
		machine.purchase("C4");
		machine.purchase("C4");
		machine.purchase("C4");
		
		actual = machine.getItemList();
		
		for (int i = 0; i < expected.size(); i++) {
			assertEquals("Line doesn't match", expected.get(i), actual.get(i));
		}
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPurchase() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		machine.feedMoney(new BigDecimal("10.00"));
		machine.purchase("B3");
		assertEquals("Purchasing a $1.50 item should reduce balance to $8.50", new BigDecimal("8.50"), machine.getCurrentBalance());
		Field slotMapField = machine.getClass().getDeclaredField("slotMap");
		slotMapField.setAccessible(true);
		Map<String, Slot> slotMap = (Map<String, Slot>) slotMapField.get(machine);
		assertEquals("Purchasing 1 B3 item should reduce its qty to 4", 4, slotMap.get("B3").getQty());
		machine.purchase("B3");
		assertEquals("Purchasing another $1.50 item should reduce balance to $7.00", new BigDecimal("7.00"), machine.getCurrentBalance());
		assertEquals("Purchasing another B3 item should reduce its qty to 3", 3, slotMap.get("B3").getQty());
		machine.purchase("B3");
		machine.purchase("B3");
		machine.purchase("B3");
		assertEquals("Buying an sold out item should fail", "Out of Stock", machine.purchase("B3"));
		assertEquals("Balance shouldn't change after a failed purchase attempt", new BigDecimal("2.50"), machine.getCurrentBalance());
		assertEquals("Buying a $3.65 item with $2.50 balance should fail", "Insufficient Funds", machine.purchase("A4"));
		assertEquals("Balance shouldn't change after a failed purchase attempt", new BigDecimal("2.50"), machine.getCurrentBalance());
		assertEquals("Buying from a nonexistent slot should fail", "Invalid Slot ID", machine.purchase("Q48"));
		assertEquals("Balance shouldn't change after a failed purchase attempt", new BigDecimal("2.50"), machine.getCurrentBalance());

	}

}
