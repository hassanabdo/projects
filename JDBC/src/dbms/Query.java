package dbms;

import java.util.ArrayList;

public class Query 
{
///////////////////////////////////////// Instances
	private String tableName, dbName;
	private Attributes attribute;
	private Conditions condition;
	private ArrayList<String> infixCondition;
	private int type;
///////////////////////////////////////// Constructor
	public Query() 
	{
		attribute = new Attributes();
		condition = new Conditions();
		infixCondition = new ArrayList();
	}
///////////////////////////////////////// Getters
	public String getTableName()
	{
		return tableName;
	}
	public String getDbName() // used when creating a database 
	{
		return dbName;
	}
	public Attributes getAttribute() 
	{
		return attribute;
	}
	public Conditions getCondition() 
	{
		return condition;
	}
	public int getType()
	{
		return type;
	}
	public ArrayList<String> getInfixCondition()
	{
		return infixCondition;
	}
//////////////////////////////////////////// Setters
	public void setDbName(String dbName) 
	{
		this.dbName = dbName;
	}
	public void setTableName(String tableName) 
	{
		this.tableName = tableName;
	}
	public void setCondition(Conditions condition)
	{
		this.condition = condition;
	}
	public void setAttribute(Attributes attribute)
	{
		this.attribute = attribute;
	}
	public void setType(int type)
	{
		/*
		 	1>>creatDB
			2>>creatTable
			3>>insertInto
			4>>delete
			5>>select
			6>>update
		 */
		this.type = type;
	}
/////////////////////////////////////adder
	public void addConditionElement(String element)
	{
		infixCondition.add(element);
	}

	
	

}
