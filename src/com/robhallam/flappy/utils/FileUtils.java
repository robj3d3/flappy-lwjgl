package com.robhallam.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	// Reads in a file as a string
	
	private FileUtils() {
		
	}
	
	public static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		// Instead of using result += - this will re-allocate the entire string again - we use StringBuilder
		// Character arrays are constant - not variable size - so we have to re-allocate whole new char array to hold new chars
		// StringBuilder allocates huge buffer behind the scenes, meaning no re-allocation necessary
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
