package com.techelevator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalesReport {

	PrintWriter printWriter;
	
	private final String fileName;
	
	public SalesReport() throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM_dd_yyyy hh_mm_ss a");
		fileName = LocalDateTime.now().format(formatter) + " Sales Report.txt";
		
		File file = new File(fileName);
		if (!file.isFile()) {
			file.createNewFile();
		}
		
		printWriter = new PrintWriter(file);
		
	}
	
	public void printSales(String body) {
		printWriter.println(body);
		printWriter.flush();
		printWriter.close();
	}
}
