package dbms;

import java.util.ArrayList;

public interface DBMS 
{
	public int createDB(); //you will use from query dbName
	public int createTable(); //you will use from query tableName, attribute names and types
	public ArrayList<ArrayList<String>> select(); //you will use from query tableName, attribute names, condition
	public int update(); //you will use from query tableName, attribute names and values, condition
	public int delete(); //you will use from query tableName, condition
	public int insertInto(); // you will use from query tableName, attribute names and values
	int detectIntegerCommand(int command);
}
