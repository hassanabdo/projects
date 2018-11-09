package com.example.rest.model;

import java.io.Serializable;

public class GroupInteraction implements Serializable {

	private static final long serialVersionUID = 1000L;
	
	private int fb_pos;
	private int fb_neg;
	private int tw_pos;
	private int tw_neg;
	
	public GroupInteraction() {
		super();
	}

	public int getFb_pos() {
		return fb_pos;
	}

	public void setFb_pos(int fb_pos) {
		this.fb_pos = fb_pos;
	}

	public int getFb_neg() {
		return fb_neg;
	}

	public void setFb_neg(int fb_neg) {
		this.fb_neg = fb_neg;
	}

	public int getTw_pos() {
		return tw_pos;
	}

	public void setTw_pos(int tw_pos) {
		this.tw_pos = tw_pos;
	}

	public int getTw_neg() {
		return tw_neg;
	}

	public void setTw_neg(int tw_neg) {
		this.tw_neg = tw_neg;
	}
	
	

}
