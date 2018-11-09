package jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class ResultSet implements java.sql.ResultSet {


	private HashMap<String, Integer> columnNameToIndex;
	private HashMap<Integer, String> IndexToColumnType;
	private String [][] records;
	private int currentRow=-1;
	private boolean wasNullFlag; // check if the element at certain column at  currentRow is Null
	private ResultSetMetaData metaData;
	private Statement statement;
	private int fetchDirection = ResultSet.FETCH_UNKNOWN;
	private int [][]prop;
	private static String testResult = null;
	private static Logger log = Logger.getLogger(ResultSet.class);
	
	public ResultSet(HashMap<String, Integer> columnNameToIndex,
			HashMap<Integer, String> IndexToColumnType, String[][] records , int [] [] prop, Statement statement) {
		this.columnNameToIndex = columnNameToIndex;
		this.IndexToColumnType = IndexToColumnType;
		this.records = records;
		this.prop = prop.clone();
		this.statement = statement;
	}

	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 1- Checks that the result set is not closed, 2- it's positioned on a
	 * valid row 3- that the given column number is valid and 4- check if column
	 * index is exist 5- Also updates the wasNullFlag to correct value.
	 */
	private void checkResultSet(int column) throws SQLException {
		checkClosed();  // 1

		if (currentRow < 0 || currentRow >= records.length){ // 2
			log.error("ResultSet not positioned properly, perhaps you need to call next.");
			throw new SQLException("ResultSet not positioned properly, perhaps you need to call next.");
			
		}

		checkColumnIndex(column); // 3

		if (!IndexToColumnType.containsKey(column)) {// 4
			log.error("This column index is not exist.");
			throw new SQLException("This column index is not exist.");
			}

		wasNullFlag = (records[currentRow][column - 1] == null); // 5
	}

	private void checkClosed() throws SQLException{
		if (isClosed()){
			log.error("This ResultSet is closed.");
			throw new SQLException("This ResultSet is closed.");
		}
	}
	
	private void checkColumnIndex(int column) throws SQLException {
		if (column < 1 || column > records[currentRow].length){ // one-based
			log.error("The column index is out of range.");
			throw new SQLException("The column index is out of range.");
			}
	}
	
///////////////////////////////////////////////////////////////////////////////////////
//converting methods///////////////////////////////////////////////////////////////////	
//////////////////////////////////////////////////////////////////////////////////////
	private boolean toBoolean(String s){
        if (s != null){
            s = s.trim();
            
            if (s.equalsIgnoreCase("t") || s.equalsIgnoreCase("true") || s.equals("1")){
        		testResult = "true";
            	return true;
            }

            if (s.equalsIgnoreCase("f") || s.equalsIgnoreCase("false") || s.equals("0")){
        		testResult = "false";
            	return false;
            }
        }
		testResult = "false";
        return false;  // SQL NULL
    }
	
	private double toDouble(String s) throws SQLException{
        if (s != null){
            try{
                s = s.trim();
        		testResult = ((Double)Double.parseDouble(s)).toString();
                return Double.parseDouble(s);
                
            }catch (NumberFormatException e){
                throw new SQLException("Wrong value for type double");
            }
        }
		testResult = "0";
        return 0;  // SQL NULL
    }
	
	private float toFloat(String s) throws SQLException{
        if (s != null){
            try{
                s = s.trim();
        		testResult = ((Float)Float.parseFloat(s)).toString();
                return Float.parseFloat(s);
                
            }catch (NumberFormatException e){
            	log.error("Wrong value for type float");
                throw new SQLException("Wrong value for type float");
            }
        }
		testResult = "0";
        return 0;  // SQL NULL
    }
    
	private int toInt(String s) throws SQLException{
        if (s != null){
            try{
                s = s.trim();
        		testResult = ((Integer)Integer.parseInt(s)).toString();
                return Integer.parseInt(s);
                
            }catch (NumberFormatException e){
            	log.error("Wrong value for type integer");
                throw new SQLException("Wrong value for type integer");
            }
        }
		testResult = "0";
        return 0;  // SQL NULL
    }
    
	private long toLong(String s) throws SQLException{
        if (s != null){
            try{
                s = s.trim();
        		testResult = ((Long)Long.parseLong(s)).toString();
                return Long.parseLong(s);
                
            }catch (NumberFormatException e){
            	log.error("Wrong value for type long");
                throw new SQLException("Wrong value for type long");
            }
        }
		testResult = "0";
        return 0;  // SQL NULL
    }
	
	public Date toDate(String s) throws SQLException{
		
        if (s != null){
            try{
                s = s.trim();
        		testResult = ((Date)Date.valueOf(s)).toString();
                return Date.valueOf(s);
                
            }catch (IllegalArgumentException e){
            	log.error("Wrong value for type date");
                throw new SQLException("Wrong value for type date");
            }
        }
		testResult = "null";
        return null;  // SQL NULL
	}	
    
	
	public Array toArray(String s,int columnIndex) throws SQLException{
		Object elements [] = new Object[1];
		
		elements[0]	= toObject(columnIndex);	
		
		Array array = new jdbc.Array(elements, IndexToColumnType.get(columnIndex));
		
		testResult = "done";
		return array;
	}
	
	
	private Object toObject(int columnIndex) throws SQLException{
		switch(getTypeNumber(IndexToColumnType.get(columnIndex))){
		
		case  0:
			return getBoolean(columnIndex);
		case  1:
			return getInt(columnIndex);
		case  2:
			return getLong(columnIndex);
		case  3:
			return getDouble(columnIndex);
		case  4:
			return getFloat(columnIndex);
		case  5:
			return getString(columnIndex);
		case  6:
			return getDate(columnIndex);
		case  7:
			return getArray(columnIndex);
		case 8:{
			log.error("The column type is invalid");
			throw new SQLException("The column type is invalid");
		}
		}
		return null;
	}
	
	private int getTypeNumber(String type){
		
		if(type.equalsIgnoreCase("boolean")) 
			return 0;
		else if(type.equalsIgnoreCase("int")) 
			return 1;
		else if(type.equalsIgnoreCase("long")) 
			return 2;
		else if(type.equalsIgnoreCase("double")) 
			return 3;
		else if(type.equalsIgnoreCase("float")) 
			return 4;
		else if(type.equalsIgnoreCase("string")) 
			return 5;
		else if(type.equalsIgnoreCase("date")) 
			return 6;
		else if(type.equalsIgnoreCase("array")) 
			return 7;
		else 
			return 8;
	}

	
	public static String getTestResult(){	
		return testResult;
	}
	// ////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean absolute(int row) throws SQLException {
		checkClosed();

		// index is 1-based, but internally we use 0-based indices
		int internalIndex;

		if (row == 0) {
			beforeFirst();
			return false;
		}

		final int rowsSize = records.length;

		if (row < 0) { // if index<0, count from the end of the result set
			if (row >= -rowsSize)
				internalIndex = rowsSize + row;
			else {
				beforeFirst();
				return false;
			}
		} else { // row>0
			if (row <= rowsSize)
				internalIndex = row - 1;
			else {
				afterLast();
				return false;
			}
		}
		currentRow = internalIndex;
		testResult = ((Integer)currentRow).toString();
		return true;
	}

	@Override
	public void afterLast() throws SQLException {
		checkClosed();

		if (records.length > 0)
			currentRow = records.length; // zero based
		testResult = ((Integer)currentRow).toString();
	}

	@Override
	public void beforeFirst() throws SQLException {
		checkClosed();

		if (records.length > 0)
			currentRow = -1; // zero based
		testResult = ((Integer)currentRow).toString();
	}


	@Override
	public void close() throws SQLException {
		records = null;
		columnNameToIndex = null;
		IndexToColumnType = null;
		prop = null;
		statement = null;
		testResult = "closed";
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		checkClosed();
		
		if (!columnNameToIndex.containsKey(columnLabel)){
			log.error("The column name" + columnLabel +" was not found in this ResultSet.");
			throw new SQLException("The column name" + columnLabel +" was not found in this ResultSet.");
		}
	
		int result = columnNameToIndex.get(columnLabel);	
		
		testResult = ((Integer)result).toString();
		return  result;
	}

	@Override
	public boolean first() throws SQLException {
		checkClosed();
	      
		if (records.length <= 0){
			testResult = "false";
			return false;
		}
		
		currentRow = 0;
		testResult = "0";
		testResult = "true";
		return true;
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag){
			testResult = "done";
			return null; // SQL NULL
		}
		
		return toArray(records[currentRow][columnIndex - 1], columnIndex);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		
		return getArray(findColumn(columnLabel));
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag){
			testResult = "false";
			return false; // SQL NULL
		}
		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("Boolean")){
			log.error("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}

		return toBoolean(records[currentRow][columnIndex - 1]);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return getBoolean(findColumn(columnLabel));
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag)
			return null; // SQL NULL

		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("Date")){
			log.error("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}

		return toDate(records[currentRow][columnIndex - 1]);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return getDate(findColumn(columnLabel));
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);
		if (wasNullFlag)
			return 0; // SQL NULL

		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("double")){
			log.error("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}

		return toDouble(records[currentRow][columnIndex - 1]);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return getDouble(findColumn(columnLabel));
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		if(direction!=ResultSet.FETCH_FORWARD || direction!=ResultSet.FETCH_REVERSE || direction!=ResultSet.FETCH_UNKNOWN)
				{
			log.error("Invalid fetch direction constant");
			throw new SQLException("Invalid fetch direction constant");
				}
		fetchDirection = direction;
		testResult = ((Integer)direction).toString();
	}
	
	@Override
    public int getFetchDirection() throws SQLException{
        checkClosed();
		testResult = ((Integer)fetchDirection).toString();
        return fetchDirection;
    }

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag)
			return 0; // SQL NULL

		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("float")){
			log.info("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}
		return toFloat(records[currentRow][columnIndex - 1]);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return getFloat(findColumn(columnLabel));
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag)
			return 0; // SQL NULL

		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("int")){
			log.error("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}

		return toInt(records[currentRow][columnIndex - 1]);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return getInt(findColumn(columnLabel));
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag)
			return 0; // SQL NULL

		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("long")){
			log.error("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}

		return toLong(records[currentRow][columnIndex - 1]);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return getLong(findColumn(columnLabel));
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
        checkClosed();
        
        if (metaData == null) {
        	metaData = new jdbc.ResultSetMetaData(columnNameToIndex, IndexToColumnType, prop);
        }
		testResult = "done";
        return metaData;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);
		
		if (wasNullFlag)
			return null; // SQL NULL

		return toObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return getObject(findColumn(columnLabel));
	}

	@Override
    public Statement getStatement() throws SQLException {
        checkClosed();
        
		testResult = "done";
        return (Statement) statement;
    }

	@Override
	public String getString(int columnIndex) throws SQLException {
		checkResultSet(columnIndex);

		if (wasNullFlag)
			return null; // SQL NULL

		if (!IndexToColumnType.get(columnIndex).equalsIgnoreCase("String")){
			log.error("This column has a different type.");
			throw new SQLException("This column has a different type.");
		}

		testResult = records[currentRow][columnIndex - 1];
		return records[currentRow][columnIndex - 1];
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return getString(findColumn(columnLabel));
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		checkClosed();
		
		boolean result = records.length>0 && (currentRow>=records.length);
		
		testResult = ((Boolean)result).toString();
		
		return result;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		checkClosed();
		
		boolean result = records.length>0 && currentRow<0;
		
		testResult = ((Boolean)result).toString();
		
		return result;
	}

	@Override
	public boolean isClosed() throws SQLException {
		boolean result = records==null;
		
		testResult = ((Boolean)result).toString();
		
		return result;
	}

	@Override
	public boolean isFirst() throws SQLException {
		checkClosed();
		
		boolean result = records.length>0 && currentRow == 0;
		
		testResult = ((Boolean)result).toString();
		
		return result;
	}

	@Override
	public boolean isLast() throws SQLException {
		checkClosed();
		
		boolean result = records.length>0 && (currentRow==records.length-1);
		
		testResult = ((Boolean)result).toString();
		
		return result;
	}

	@Override
	public boolean last() throws SQLException {
		checkClosed();
		
		if(records.length==0){
			testResult = "false";
			return false;
		}
		
		currentRow = records.length-1;
		testResult = "true";
		return true;
	}

	@Override
	public boolean next() throws SQLException {
		checkClosed();
		
		if(currentRow<records.length)
			currentRow++;
		boolean result = currentRow<records.length;
		testResult = ((Boolean)result).toString();
		return result;
	}

	@Override
	public boolean previous() throws SQLException {
		checkClosed();
		
		if(currentRow<0)
			currentRow--;
		boolean result = currentRow>=0;
		testResult = ((Boolean)result).toString();
		return result;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean relative(int rows) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

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
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

}
