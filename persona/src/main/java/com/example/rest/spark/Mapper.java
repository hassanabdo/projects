package com.example.rest.spark;

import java.util.Map;

import org.apache.spark.api.java.function.PairFunction;

import com.example.rest.model.GroupInteraction;
import com.example.rest.utils.HelperMethods;

import scala.Tuple2;

public class Mapper implements PairFunction<String, Integer, GroupInteraction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2000L;
	
	private Map<String, Integer> map;
	private boolean isCityKey;

	public Mapper(Map<String, Integer> map2, Boolean cityOrCountry) {
		super();
		map = map2;
		this.isCityKey = cityOrCountry;
	}




	@Override
	public Tuple2<Integer, GroupInteraction> call(String row) throws Exception {
		String[] fields = row.split("&");
		int i = 0;
		String city = fields[i++];
		String country = fields[i++];
		String age = fields[i++];
		String gender = fields[i++];
		int fb_pos = Integer.parseInt(fields[i++]);
		int fb_neg = Integer.parseInt(fields[i++]);
		int tw_pos = Integer.parseInt(fields[i++]);
		int tw_neg = Integer.parseInt(fields[i++]);

		// builder.append(city).append(",").append(age).append(",").append(gender);
		String keyStr = new StringBuilder().append(city).append(",").append(age).append(",").append(gender).toString();
		if (!isCityKey) {
			keyStr = new StringBuilder().append(country).append(",").append(age).append(",").append(gender).toString();
		}
		
		Integer key = map.get(keyStr);
		System.out.println(keyStr +" "+key);

		GroupInteraction group = new GroupInteraction();
		
		group.setFb_pos(fb_pos);
		group.setFb_neg(fb_neg);
		group.setTw_pos(tw_pos);
		group.setTw_neg(tw_neg);

		return new Tuple2<Integer, GroupInteraction>(key, group);
	}

}
