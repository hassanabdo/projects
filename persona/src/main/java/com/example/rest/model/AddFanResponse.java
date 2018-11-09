package com.example.rest.model;

public class AddFanResponse extends ErrorResponse{
	
	private int user_id;

	public AddFanResponse() {
		super();
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	

}
