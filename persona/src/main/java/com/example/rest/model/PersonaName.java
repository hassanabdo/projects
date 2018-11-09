package com.example.rest.model;

public class PersonaName {
	private String[] names_list;

	private String country_name;

	public String[] getNames_list() {
		return names_list;
	}

	public void setNames_list(String[] names_list) {
		this.names_list = names_list;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	@Override
	public String toString() {
		return "ClassPojo [names_list = " + names_list + ", country_name = " + country_name + "]";
	}
}
