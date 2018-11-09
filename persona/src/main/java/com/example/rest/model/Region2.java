package com.example.rest.model;

import java.util.ArrayList;

public class Region2 {
	
	private int id;
	private String region_name;
	private ArrayList<Integer> neighbours;
	
	public Region2() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public ArrayList<Integer> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<Integer> neighbours) {
		this.neighbours = neighbours;
	}

	
	
	
	

}
