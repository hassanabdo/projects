package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

public class Array implements java.sql.Array {
	private String type;
	private Object [] elements;
	
	
	public Array(Object [] elements, String type){
		this.type = type;
		this.elements = elements;
	}
	
	@Override
	public void free() throws SQLException {
		type = null;
		elements = null;
	}

	@Override
	public Object getArray() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getArray(Map<String, Class<?>> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getArray(long arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getArray(long arg0, int arg1, Map<String, Class<?>> arg2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBaseType() throws SQLException {
		Types a = null;
		if(type.equalsIgnoreCase("boolean")) 
			return a.BOOLEAN;
		else if(type.equalsIgnoreCase("int")) 
			return a.INTEGER;
		else if(type.equalsIgnoreCase("long")) 
			return a.BIGINT;
		else if(type.equalsIgnoreCase("double")) 
			return a.DOUBLE;
		else if(type.equalsIgnoreCase("float")) 
			return a.FLOAT;
		else if(type.equalsIgnoreCase("string")) 
			return a.VARCHAR;
		else if(type.equalsIgnoreCase("date")) 
			return a.DATE;
		else if(type.equalsIgnoreCase("array")) 
			return a.ARRAY;

		return a.NULL;
	}

	@Override
	public String getBaseTypeName() throws SQLException {
		if(!(type.equalsIgnoreCase("boolean")) ||
				(type.equalsIgnoreCase("int")) ||
				(type.equalsIgnoreCase("long")) ||
				(type.equalsIgnoreCase("double")) ||
				(type.equalsIgnoreCase("float")) ||
				(type.equalsIgnoreCase("String")) ||
				(type.equalsIgnoreCase("date")))
			throw new SQLException("Invalid type.");
		return type;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return null;
	}

	@Override
	public ResultSet getResultSet(Map<String, Class<?>> arg0)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getResultSet(long arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet getResultSet(long arg0, int arg1,
			Map<String, Class<?>> arg2) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
