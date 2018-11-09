package dbms;

import java.util.ArrayList;
import java.util.Stack;

import org.jdom2.Element;

public class Postfix 
{
	public static String resultOfOperation;
	
	public static ArrayList<String> infixToPostFix(ArrayList<String> infix)
	{
		ArrayList<String> postfix = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		for(int i = 0 ; i < infix.size() ; i++){
			if(infix.get(i).equals("(")) stack.push("(");
			else if(infix.get(i).equals(")"))	handleClosedBracket(stack,postfix);
			else if(isCompareOperator(infix.get(i)) || isLogicalOperator(infix.get(i)))	
			{		handleOperator(infix.get(i),stack,postfix);				}	
			else postfix.add(infix.get(i));			// column names , values
		}
		while(stack.size()!=0)	postfix.add(stack.pop());
		resultOfOperation = postfix.toString();	
		return postfix;
	}
	
	public static boolean evaluatePostFix(ArrayList<String> postFix,Element record)
	{
		Stack<String> conditionStack = new Stack<String>();
		Stack<Boolean> booleanStack = new Stack<Boolean>( );
		for(int i = 0 ; i < postFix.size() ; i++)
		{
			if(isCompareOperator(postFix.get(i)))			booleanStack.push( applyCondition( record,conditionStack.pop(),postFix.get(i),conditionStack.pop() ) );
			else if(postFix.get(i).equalsIgnoreCase("NOT"))	booleanStack.push(!booleanStack.pop());
			else if(postFix.get(i).equalsIgnoreCase("AND")) booleanStack.push(booleanStack.pop() && booleanStack.pop());
			else if(postFix.get(i).equalsIgnoreCase("OR"))  booleanStack.push(booleanStack.pop() || booleanStack.pop());
			else	conditionStack.push(postFix.get(i));
		}
		return booleanStack.pop();
	}
	
	private static void handleClosedBracket(Stack<String> stack,ArrayList<String> postfix)
	{
		while(!stack.peek().equals("("))	postfix.add(stack.pop());
		stack.pop(); // discard (
	}
	private static void handleOperator(String currentOperator,Stack<String> stack,ArrayList<String> postfix)
	{
		if(stack.size()==0 || (getPrecedence(currentOperator) > getPrecedence(stack.peek())) )	stack.push(currentOperator.toUpperCase());
		else
		{
			String topStackOperator = stack.peek();
			while( (stack.size()!=0) && (getPrecedence(currentOperator) <= getPrecedence(topStackOperator)))
			{
				postfix.add(stack.pop());
				if(stack.size()!=0)	topStackOperator = stack.peek();
			}
			stack.push(currentOperator.toUpperCase());
		}
	
	}
	
	private static int getPrecedence(String operator)
	{
		switch(operator.toUpperCase())
		{
			case "NOT": return 3;
			case "AND": return 2;
			case "OR": 	return 1;
			case "(": 	return 0;
			default: 	return 4; // = , > , < , <= , >= , !=
		}
	}
	
	private static boolean isCompareOperator(String operator)
	{
		return (operator.equals("=") || operator.equals(">") || operator.equals("<") || operator.equals("<=") || operator.equals(">=") || operator.equals("!="));
	}
	
	private static boolean isLogicalOperator(String operator)
	{
		return (operator.equalsIgnoreCase("AND") || operator.equalsIgnoreCase("OR") || operator.equalsIgnoreCase("NOT"));
	}
	
	private static boolean applyCondition(Element record , String value , String operator, String columnName)
	{
	    String savedValue = record.getChildText(columnName); // get value saved for column for the current record
	    switch(operator)
	    { 
	        case ">":	return (savedValue.compareToIgnoreCase(value) > 0);	
	        case "=":	return (savedValue.compareToIgnoreCase(value)  == 0) ;
	        case "<":	return (savedValue.compareToIgnoreCase(value) < 0);
	        case "<=":	return (savedValue.compareToIgnoreCase(value) <= 0) ;
	        case ">=":	return (savedValue.compareToIgnoreCase(value)  >= 0) ;
	        case "!=":	return (savedValue.compareToIgnoreCase(value)  != 0) ;
	        default: 	return false;
	    }
	}

}
