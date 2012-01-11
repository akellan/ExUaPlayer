package com.akella.utils;

public class StringUtils {
	
	public static String Implode(String[] array, String glue){
		
		if(array == null) return null;
		if(glue == null) glue = "";
		
		if(array.length == 0) return null;
		
		StringBuilder result = new StringBuilder();
		
		
		for (int i = 0; i < array.length - 1; i++) {
			result.append(array[i]);
			result.append(glue);
		}
		
		
		result.append(array[array.length - 1]);
		
		return result.toString();
		
	}
}
