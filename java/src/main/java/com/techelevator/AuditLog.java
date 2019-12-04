package com.techelevator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLog {
	private DateTimeFormatter formatter;
	PrintWriter printWriter;
	
	private final String fileName;
	
	public AuditLog() throws IOException {
		formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		fileName = "Log.txt";
		
		File file = new File(fileName);
		if (!file.isFile()) {
			file.createNewFile();
		}
		
		FileWriter fileWriter = new FileWriter(fileName, true);
		printWriter = new PrintWriter(fileWriter);
	}
	
	public void addLogEntry(String body) {
		printWriter.println(LocalDateTime.now().format(formatter) + " " + body);
		printWriter.flush();
	}
	
	public void closeLog() {
		printWriter.close();
	}
}
