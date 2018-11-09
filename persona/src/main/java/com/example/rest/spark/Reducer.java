package com.example.rest.spark;

import org.apache.spark.api.java.function.Function2;

import com.example.rest.model.GroupInteraction;

public class Reducer implements Function2<GroupInteraction, GroupInteraction, GroupInteraction> {

	@Override
	public GroupInteraction call(GroupInteraction v1, GroupInteraction v2) throws Exception {
		GroupInteraction group = new GroupInteraction();
		
		group.setFb_pos(v1.getFb_pos() + v2.getFb_pos());
		
		group.setFb_neg(v1.getFb_neg() + v2.getFb_neg());
		
		group.setTw_pos(v1.getTw_pos() + v2.getTw_pos());
		
		group.setTw_neg(v1.getTw_neg() + v2.getTw_neg());
		
		return group;
	}

}
