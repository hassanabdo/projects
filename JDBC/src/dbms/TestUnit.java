package dbms;

import javax.swing.filechooser.FileSystemView;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestUnit {
	FileSystemView filesys = FileSystemView.getFileSystemView();
	String desktopPath = filesys.getHomeDirectory()+"";
	Parser parser = new Parser();
	TableOperator tp;
	
	@Test
	public void createDB(){
		String query = "CREATE DATABASE UnitTest;";
		parser.excuteQuery(query);
		tp = parser.getTableOperator();
		String Expected = "Database "+"UnitTest"+" Created Successfully.";
		String Actual = tp.getResultOfOperation();
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void createTable(){
		String query1 = "USE UnitTest;";
		parser.excuteQuery(query1);
//		System.out.println("yaaaaaaaa raaaaaaaab");
		String query = "CREATE TABLE test (ID int,Name String);";
		parser.excuteQuery(query);
		tp = parser.getTableOperator();
		String Expected = "Table Created Successfully.";
		String Actual = tp.getResultOfOperation();
//		System.out.println(Actual+" ndandoasld ");
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testInsertion(){
		String query1 = "USE UnitTest;";
		parser.excuteQuery(query1);
		String query="INSERT INTO test VALUES (1,HASSAN);";
		parser.excuteQuery(query);
		tp = parser.getTableOperator();
		String Expected = "Record inserted successfully.";
		String Actual = tp.getResultOfOperation();
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testInsertion2(){
		String query1 = "USE UnitTest;";
		parser.excuteQuery(query1);
		String query="INSERT INTO test VALUES (2,ALI);";
		parser.excuteQuery(query);
		tp = parser.getTableOperator();
		String Expected = "Record inserted successfully.";
		String Actual = tp.getResultOfOperation();
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testDelete(){
		String query1 = "USE UnitTest;";
		parser.excuteQuery(query1);
		String query = "DELETE FROM test WHERE ID=2;";
		parser.excuteQuery(query);
		String Expected = "Record deleted successfully.";
		String Actual = parser.getTableOperator().getResultOfOperation();
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testSelect(){
		String query1 = "USE UnitTest;";
		parser.excuteQuery(query1);
		String query = "SELECT * FROM test;";
		parser.excuteQuery(query);
		String Expected = "Successful Selection";
		String Actual = TableOperator.resultOfOperation;
		assertEquals(Expected,Actual);
	}
	
	@Test
	public void testUpdate(){
		String query1 = "USE UnitTest;";
		parser.excuteQuery(query1);
		String query="UPDATE test SET ID=5,Name='M7MD' WHERE ID=1;";
		parser.excuteQuery(query);
		String Expected = "Table updated successfully.";
		String Actual = TableOperator.resultOfOperation;
		assertEquals(Expected,Actual);
	}

}
