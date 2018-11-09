package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.filechooser.FileSystemView;

import dbms.Parser;
import dbms.Query;
import dbms.TableOperator;

public class Statement implements java.sql.Statement
{
	private ArrayList<String> listOfCommands;
	private boolean isClosed;
	private ResultSet resultSet;
	private Connection connection;
	private Parser parser;
	private Query query;
	private TableOperator dbms;
	private int seconds;
	
	public Statement(Connection connection)
	{	
		listOfCommands = new ArrayList<String>();
		isClosed = false;
		this.connection = connection;
		dbms = new TableOperator();
		this.seconds = 0;
		resultSet = null;
		
		FileSystemView filesys = FileSystemView.getFileSystemView();
		parser = new Parser();
	}
	
	@Override
	public void addBatch(String sql) throws SQLException 
	{
		if(isClosed) 
		{
			System.out.println("The Statement is closed.");
			return;
		}else 
		{
	         query = parser.excuteQuery(sql);
	        if(query.getType() != 5)    listOfCommands.add(sql);
		}
	}
	
	@Override
	public void clearBatch() throws SQLException 
	{
		if(isClosed) 
		{
			System.out.println("The Statement is closed.");
			return;
		}
		
		if(!listOfCommands.isEmpty())
		{
			listOfCommands.clear();
			System.out.println("List of commands cleared successfully.");
		}
		else	System.out.println("List of commands already empty.");
	}
	
	@Override
	public void close() throws SQLException 
	{
		if(!isClosed)
		{
			if(resultSet!=null) resultSet.close();
			isClosed = true;
		}
		else System.out.println("Statement is already closed.");
	}
	
	@Override
	public boolean execute(String sql) throws SQLException 
	{
		if(isClosed) 
		{
			System.out.println("The Statement is closed.");
			return false;
		}
	    query = parser.excuteQuery(sql);
	    dbms.setQueryAndValidator(query);
	    if(query.getType() == 5)
	    {
	    	dbms.select();
	    	return true;
	    }
	    dbms.detectIntegerCommand(query.getType());
		return false;
	}
	
	@Override
	public int[] executeBatch() throws SQLException 
	{
		if(isClosed) 
		{
			System.out.println("The Statement is closed.");
			return (new int[0]);
		}
	    if(listOfCommands.size() != 0)
    	{ 
    	    int[] updateCounts = new int[listOfCommands.size()];
    	    int result;
    	    for (int i =0 ; i < listOfCommands.size(); i++)
    	     {
    	        query = parser.excuteQuery(listOfCommands.get(i));
    	        dbms.setQueryAndValidator(query);
    	        if(query.getType()==-1) updateCounts[i] = EXECUTE_FAILED; // invalid query
    	        else
    	        {
    	            result = dbms.detectIntegerCommand(query.getType());
    	            if(result == -1)  updateCounts[i]  = SUCCESS_NO_INFO ; // can't execute
    	            else    updateCounts[i]  = result;
    	        }
    	     }
    	    return updateCounts ;
    	}else  return (new int[0]);
	}

	public ResultSet executeQuery(String sql) throws SQLException 
	{
		if(isClosed) {
			System.out.println("The Statement is closed.");
			return null;
		}
	    query = parser.excuteQuery(sql);
        if(query.getType()==5){
            dbms.setQueryAndValidator(query);
            ArrayList<ArrayList<String> > resultTable = dbms.select();
            HashMap<String,Integer> columnNameToIndex = new HashMap<String,Integer>();
            HashMap<Integer,String> IndexToColumnType = new HashMap<Integer,String>();
            if (resultTable.size() <= 1) return null; // empty table
            for(int i = 0 ; i < resultTable.get(0).size() ; i++)
            {	if(i%2 == 0) columnNameToIndex.put(resultTable.get(0).get(i) , i/2);
                else  IndexToColumnType.put(i/2 , resultTable.get(0).get(i));			} 
            String [][] records = new String[resultTable.size()-1][resultTable.get(1).size()];
            for(int i = 0 ; i < records.length ; i++) // for each row , for each column
            {	for(int j = 0 ; j < records[0].length ; j++){ records[i][j] = resultTable.get(i+1).get(j); }	}
            resultSet = new jdbc.ResultSet(columnNameToIndex,IndexToColumnType,records,dbms.propArray(),this);
            return  resultSet;
        }else   return null;
	}
	
	@Override
	public int executeUpdate(String sql) throws SQLException 
	{
		if(isClosed) {
			System.out.println("The Statement is closed.");
			return -1;
		}
	    query = parser.excuteQuery(sql);
	    if(query.getType() == 5) 
	    {
	        System.out.println("SELECT statement can not be executed using executeUpdate().");
	        return -1;
	    }
		else 
		{
		    dbms.setQueryAndValidator(query);
		    return dbms.detectIntegerCommand(query.getType());
		}
	}
	
		@Override
	public void setQueryTimeout(int s) throws SQLException {
		if(isClosed) {
				System.out.println("The Statement is closed.");
				return;
		}
		if(s<0) System.out.println("Time can't be negative.");
		else	this.seconds = s ;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		if(isClosed) {
			System.out.println("The Statement is closed.");
			return -1;
		}
		return seconds;
	}
	
	
	@Override
	public Connection getConnection() throws SQLException 
	{
		if(isClosed) {
			System.out.println("The Statement is closed.");
			return null;
		}
		return connection;
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException 
	{
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException 
	{
		return null;
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
