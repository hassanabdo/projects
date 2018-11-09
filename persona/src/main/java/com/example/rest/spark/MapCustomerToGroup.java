package com.example.rest.spark;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import scala.runtime.AbstractFunction1;

public class MapCustomerToGroup extends AbstractFunction1<ResultSet, String> implements Serializable {

	@Override
	public String apply(ResultSet row) {
		StringBuilder builder = new StringBuilder();
		try {
			builder.append(row.getString("city")).append("&");
			builder.append(row.getString("country")).append("&");
			builder.append(row.getString("age")).append("&");
			builder.append(row.getString("gender")).append("&");
			builder.append(row.getInt("fb_pos")).append("&");
			builder.append(row.getInt("fb_neg")).append("&");
			builder.append(row.getInt("tw_pos")).append("&");
			builder.append(row.getInt("tw_neg"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return builder.toString();
	}

}
