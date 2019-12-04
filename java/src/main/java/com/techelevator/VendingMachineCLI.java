package com.techelevator;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Scanner;

/**************************************************************************************************************************
*  This is your Vending Machine Command Line Interface (CLI) class
*  
*  It is instantiated and invoked from the VendingMachineApp (main() application)
*  
*  Your code should be placed in here
***************************************************************************************************************************/
import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE      = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT          = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT  = "Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS,
													    MAIN_MENU_OPTION_PURCHASE,
													    MAIN_MENU_OPTION_EXIT,
													    MAIN_MENU_OPTION_SALES_REPORT,
													    };
	
	private static final String PURCHASE_MENU_OPTION_FEED_MONEY 		= "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT 	= "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {	PURCHASE_MENU_OPTION_FEED_MONEY,
															PURCHASE_MENU_OPTION_SELECT_PRODUCT,
															PURCHASE_MENU_OPTION_FINISH_TRANSACTION
															} ;
	
	private static final String FEED_MENU_OPTION_FEED_1 	= "Feed $1";
	private static final String FEED_MENU_OPTION_FEED_2 	= "Feed $2";
	private static final String FEED_MENU_OPTION_FEED_5 	= "Feed $5";
	private static final String FEED_MENU_OPTION_FEED_10 	= "Feed $10";
	private static final String FEED_MENU_OPTION_END 		= "Done Feeding";
	private static final String[] FEED_MENU_OPTIONS = {	FEED_MENU_OPTION_FEED_1,
														FEED_MENU_OPTION_FEED_2,
														FEED_MENU_OPTION_FEED_5,
														FEED_MENU_OPTION_FEED_10,
														FEED_MENU_OPTION_END
														};
	
	
	private Menu vendingMenu;              // Menu object to be used by an instance of this class
	private VendingMachine machine;
	
	public VendingMachineCLI(Menu menu) {  // Constructor - user will pas a menu for this class to use
		this.vendingMenu = menu;           // Make the Menu the user object passed, our Menu
		machine = new VendingMachine();
	}
	/**************************************************************************************************************************
	*  VendingMachineCLI main processing loop
	*  
	*  Display the main menu and process option chosen
	***************************************************************************************************************************/

	public void run() {

		boolean shouldProcess = true;         // Loop control variable
		
		while(shouldProcess) {                // Loop until user indicates they want to exit
			
			String choice = (String)vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS);  // Display menu and get choice
			
			switch(choice) {                  // Process based on user menu choice
			
				case MAIN_MENU_OPTION_DISPLAY_ITEMS:
					displayItems();           // invoke method to display items in Vending Machine
					break;                    // Exit switch statement
			
				case MAIN_MENU_OPTION_PURCHASE:
					purchaseItems();          // invoke method to purchase items from Vending Machine
					break;                    // Exit switch statement
			
				case MAIN_MENU_OPTION_EXIT:
					endMethodProcessing();    // Invoke method to perform end of method processing
					shouldProcess = false;    // Set variable to end loop
					break;                    // Exit switch statement
					
				case MAIN_MENU_OPTION_SALES_REPORT:
					machine.printSales();
					break;
			}	
		}
		return;                               // End method and return to caller
	}

/********************************************************************************************************
 * Methods used to perform processing
 ********************************************************************************************************/
	public void displayItems() {      // static attribute used as method is not associated with specific object instance
		
		LinkedList<String> itemList = machine.getItemList();
		
		System.out.println();
		System.out.print(String.format("%-10s", "SLOT"));
		System.out.print(String.format("%-25s", "ITEM"));
		System.out.print(String.format("%-10s", "QTY"));
		System.out.print(String.format("%-8s", "PRICE"));
		System.out.println();
		System.out.println("================================================");
		
		for (String line : itemList) {
			String[] splitLine = line.split(",");
			System.out.print(String.format("%-10s", splitLine[0]));
			System.out.print(String.format("%-25s", splitLine[1]));
			System.out.print(String.format("%-10s", !splitLine[2].equals("0") ? splitLine[2] : "SOLD OUT"));
			System.out.print(String.format("%-8s", splitLine[3]));
			System.out.println();
		}
		
	}
	
	public void purchaseItems() { // static attribute used as method is not associated with specific object
											// instance

		boolean shouldProcess = true; // Loop control variable

		while (shouldProcess) { // Loop until user indicates they want to exit

			System.out.println();
			System.out.println("Current Money Provided: $" + machine.getCurrentBalance());

			String choice = (String) vendingMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS); // Display menu and get choice

			switch (choice) { // Process based on user menu choice

			case PURCHASE_MENU_OPTION_FEED_MONEY:
				feedMoney();
				break; // Exit switch statement

			case PURCHASE_MENU_OPTION_SELECT_PRODUCT:
				Scanner console = new Scanner(System.in);
				displayItems();
				System.out.println();
				System.out.println("Please enter the Slot ID >>> ");
				String slotID = console.nextLine();
				String message = machine.purchase(slotID);
				
				if (message.contains(";")) {
					System.out.println();
					System.out.print(String.format("%-25s", "ITEM"));
					System.out.print(String.format("%-10s", "PRICE"));
					System.out.println();
					System.out.println("========================================");
					String[] splitMessage = message.split(";");
					System.out.print(String.format("%-25s", splitMessage[0]));
					System.out.print(String.format("%-10s", splitMessage[1]));
					System.out.println();
					System.out.println("\n" + splitMessage[2]);
					System.out.println();
				} else {
					System.out.println(message);
				}
				break; // Exit switch statement

			case PURCHASE_MENU_OPTION_FINISH_TRANSACTION:
				String change = machine.finishTransaction(); 
				
				System.out.println();
				System.out.print(String.format("%-10s", "QUARTERS"));
				System.out.print(String.format("%-10s", "DIMES"));
				System.out.print(String.format("%-10s", "NICKELS"));

				System.out.println();
				System.out.println("========================================");
				
				String[] splitChange = change.split(";");
				System.out.print(String.format("%-10s", splitChange[0]));
				System.out.print(String.format("%-10s", splitChange[1]));
				System.out.print(String.format("%-10s", splitChange[2]));

				System.out.println();
				System.out.println();
				shouldProcess = false; // Set variable to end loop
				break; // Exit switch statement
			}
		}
		return; // End method and return to caller
	}
	
	private void feedMoney() {
		boolean shouldProcess = true; // Loop control variable

		while (shouldProcess) { // Loop until user indicates they want to exit
			
			System.out.println();
			System.out.println("Current Money Provided: $" + machine.getCurrentBalance());

			String choice = (String) vendingMenu.getChoiceFromOptions(FEED_MENU_OPTIONS); // Display menu and get choice
			System.out.println();

			switch (choice) { // Process based on user menu choice

			case FEED_MENU_OPTION_FEED_1:
				machine.feedMoney(new BigDecimal("1.00"));
				break; // Exit switch statement

			case FEED_MENU_OPTION_FEED_2:
				machine.feedMoney(new BigDecimal("2.00"));
				break; // Exit switch statement

			case FEED_MENU_OPTION_FEED_5:
				machine.feedMoney(new BigDecimal("5.00"));
				break;
				
			case FEED_MENU_OPTION_FEED_10:
				machine.feedMoney(new BigDecimal("10.00"));
				break; // Exit switch statement
				
			case FEED_MENU_OPTION_END:
				shouldProcess = false;
				break;
			}
			
		}
		return; // End method and return to caller		
	}

	public static void endMethodProcessing() { // static attribute used as method is not associated with specific object instance
		// Any processing that needs to be done before method ends
	}
}
