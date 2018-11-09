package com.example.rest.spark;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import scala.runtime.AbstractFunction1;

public class MapCityToString  extends AbstractFunction1<ResultSet, String> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String apply(ResultSet row) {
		// TODO Auto-generated method stub
		try {
			
			String city = row.getString("city");
			return city;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
