package dbms;

import java.util.ArrayList;

public class Conditions 
{
////////////////////////////////////////////// Instances
	private ArrayList<String> postFix;
	private ArrayList<String> columnName;
	private ArrayList<String> operator;
	private ArrayList<String> value;
	
	public ArrayList<String> getColumnName() {
		return columnName;
	}
	public ArrayList<String> getOperator() {
		return operator;
	}
	public ArrayList<String> getValue() {
		return value;
	}
	//////////////////////////////////////////////	
	public Conditions() 
	{
		postFix = new ArrayList();
		columnName = new ArrayList();
		operator = new ArrayList();
		value = new ArrayList();
	}
////////////////////////////////////////////
	public void infixToPostfix(ArrayList<String> infix)
	{
		postFix = Postfix.infixToPostFix(infix);
	}
//////////////////////////////////////////////get array
	public ArrayList<String> getPostfix()
	{
		return postFix;
	}
////////////////////////////////////////////// get by index
	public String getColumnByIndex(int index)
	{
		return columnName.get(index);
	}
	public String getOperatorByIndex(int index)
	{
		return operator.get(index);
	}
	public String getValueByIndex(int index)
	{
		return value.get(index);
	}
////////////////////////////////////////////////// adders
	public void addColumnByIndex(String columnName)
	{
		this.columnName.add(columnName);
	}
	public void addOperatorByIndex(String operator)
	{
		 this.operator.add(operator);
	}
	public void addValueByIndex(String value)
	{
		this.value.add(value);
	}
////////////////////////////////////////////////// 


}
