package jdbc;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Set;


public class ResultSetMetaData implements java.sql.ResultSetMetaData {


	private HashMap<String, Integer> columnNameToIndex;
	private HashMap<Integer, String> IndexToColumnType;
	private int [][] prop;
	private static String testResult;
	
	public ResultSetMetaData(HashMap<String, Integer> columnNameToIndex, HashMap<Integer, String> IndexToColumnType, int [][] prop){
		this.columnNameToIndex = columnNameToIndex;
		this.IndexToColumnType = IndexToColumnType;
		this.prop = prop.clone();
	}
	
	public static String getTestResult(){	
		return testResult;
	}
	
	public int getBaseType(String type) throws SQLException {
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
	public int getColumnCount() throws SQLException {
		testResult = ((Integer)(columnNameToIndex.size())).toString();
		return columnNameToIndex.size();		
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		Set <String> a = columnNameToIndex.keySet();
		Object[] temp = a.toArray();
		for(int i = 0; i<a.size(); i++){
			if(columnNameToIndex.get(temp[i])==column){
				testResult = (String)temp[i];
				return testResult;
			}
		}
		throw new SQLException("Column index is not exist.");
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		//ColumnName = ColumnLabel
		testResult = getColumnLabel(column);
		return 	testResult;
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		int result = getBaseType(IndexToColumnType.get(column));
		testResult = ((Integer)(result)).toString();
		return result;		
	}
     
	@Override
	public String getTableName(int column) throws SQLException {
		// return column name, or "" if not applicable
		testResult = "";
		return "";
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException { //<<
		if(prop[column-1][0]==1){
			testResult = "true";
			return true;
		}
		testResult = "false";
		return false;
	}

	@Override
	public int isNullable(int column) throws SQLException { //<<
		testResult = ((Integer)(prop[column-1][2])).toString();
		return prop[column-1][2];
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException { 
		if(prop[column-1][1]==1){
			testResult = "true";
			return true;
		}
		testResult = "false";
		return false;
	}

	@Override
	public boolean isSearchable(int column) throws SQLException { 
		// all Types were handled are searchable
		if(prop[column-1][3]==1){
			testResult = "true";
			return true;
		}
		testResult = "false";
		return false;
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		return !isReadOnly(column);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////

	
	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getColumnTypeName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	


}
