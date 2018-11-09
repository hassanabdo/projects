package test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import dbms.*;

public class testCondition {
	
	@Test
	public void testCon(){
		HashMap<String,ArrayList<String>> table = new HashMap<String,ArrayList<String>>();
		String col1 = "ID";
		ArrayList<String> col1prop = new ArrayList<String>();
		col1prop.add("int");
		col1prop.add("readOnly");
		////////////////////////////////////////////////////////
		String col2 = "Name";
		ArrayList<String> col2prop = new ArrayList<String>();
		col2prop.add("String");
		col2prop.add("readOnly");
////////////////////////////////////////////////////////
		String col3 = "Married";
		ArrayList<String> col3prop = new ArrayList<String>();
		col3prop.add("boolean");
		col3prop.add("readOnly");
////////////////////////////////////////////////////////
		table.put(col1, col1prop);
		table.put(col2, col2prop);
		table.put(col3, col3prop);
		
		Validation vd = new Validation();
		assertTrue(vd.isCondition(table));
	}

}
