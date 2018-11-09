package com.example.rest.model;

public class UpdateFanResponse extends ErrorResponse{
	
	private boolean isDoneForBoth;
	private boolean isDoneFacebook;
	private boolean isDoneTwitter;
	public UpdateFanResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isDoneForBoth() {
		return isDoneForBoth;
	}
	public void setDoneForBoth(boolean isDoneForBoth) {
		this.isDoneForBoth = isDoneForBoth;
	}
	public boolean isDoneFacebook() {
		return isDoneFacebook;
	}
	public void setDoneFacebook(boolean isDoneFacebook) {
		this.isDoneFacebook = isDoneFacebook;
	}
	public boolean isDoneTwitter() {
		return isDoneTwitter;
	}
	public void setDoneTwitter(boolean isDoneTwitter) {
		this.isDoneTwitter = isDoneTwitter;
	}
	
	
	
	

}
