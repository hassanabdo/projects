package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import dbms.Postfix;

public class JDBCTestUnit 
{
	@Test
	public void infixToPostFix1()
	{
		ArrayList<String> infix = new ArrayList<String>();
		infix.add("(");
		infix.add("col1");
		infix.add("=");
		infix.add("val1");
		infix.add(")");
		Postfix.infixToPostFix(infix);
		String Expected = "[col1, val1, =]";
		String Actual = Postfix.resultOfOperation;
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void infixToPostFix2()
	{
		ArrayList<String> infix = new ArrayList<String>();
		infix.add("(");
		infix.add("col1");
		infix.add("=");
		infix.add("val1");
		infix.add(")");
		infix.add("AND");
		infix.add("NOT");
		infix.add("(");
		infix.add("col2");
		infix.add(">=");
		infix.add("val2");
		infix.add(")");
		Postfix.infixToPostFix(infix);
		String Expected = "[col1, val1, =, col2, val2, >=, NOT, AND]";
		String Actual = Postfix.resultOfOperation;
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void infixToPostFix3()
	{
		ArrayList<String> infix = new ArrayList<String>();
		//a = b and c = d and v > g OR m <= 3 AND ( c = o OR g = t )
		// a b = c d = and v > g and m 3 <=  c o = g t = OR AND OR
		infix.add("a");
		infix.add("=");
		infix.add("b");
		infix.add("AND");
		infix.add("c");
		infix.add("=");
		infix.add("d");
		infix.add("AND");
		infix.add("v");
		infix.add(">");
		infix.add("g");
		infix.add("OR");
		infix.add("m");
		infix.add("<=");
		infix.add("3");
		infix.add("AND");
		infix.add("(");
		infix.add("c");
		infix.add("=");
		infix.add("o");
		infix.add("OR");
		infix.add("g");
		infix.add("=");
		infix.add("t");
		infix.add(")");
		Postfix.infixToPostFix(infix);
		String Expected = "[a, b, =, c, d, =, AND, v, g, >, AND, m, 3, <=, c, o, =, g, t, =, OR, AND, OR]";
		String Actual = Postfix.resultOfOperation;
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void infixToPostFix4()
	{
		ArrayList<String> infix = new ArrayList<String>();
		// boda = aaa AND NOT 5alid  > 3
		infix.add("boda");
		infix.add("=");
		infix.add("aaa");
		infix.add("AND");
		infix.add("NOT");
		infix.add("5alid");
		infix.add(">");
		infix.add("3");
		Postfix.infixToPostFix(infix);
		String Expected = "[boda, aaa, =, 5alid, 3, >, NOT, AND]";
		String Actual = Postfix.resultOfOperation;
		assertEquals(Expected,Actual);
	}
	


}
