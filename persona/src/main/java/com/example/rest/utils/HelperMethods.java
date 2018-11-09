package com.example.rest.utils;

import java.io.Serializable;

public class HelperMethods implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1333L;

	public static String getNormalizedCityName(String city) {
		String normalizedCityName = city.toLowerCase();
		return normalizedCityName;
	}
	
	public static String getNormalizedAgeName(String age) {
		return age;
	}
	
	public static String getNormalizedActivityName(String activity) {
		return activity;
	}
	
	public static String getNormalizedGenderName(String gender) {
		return gender;
	}
	

}
