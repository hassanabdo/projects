package test;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import jdbc.*;
import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

public class UnitTest {
	Driver driver = new Driver();
	Connection con=new Connection();
	Logger log = Logger.getLogger(MainTest.class);
	
	@Test
	public void testAcceptsURL(){
		log.info("testing acceptURL");
		String URL = "JDBC:jdbc:dbms";
		//assertTrue(driver.acceptsURL(URL));
	}
	
	@Test
	public void testGetDriverName(){
		log.info("testing driver name");
		String actual = driver.getDriverName();
		String expected = "jdbc:Driver";
		assertEquals(expected, actual);
	}
	
	@Test
	public void testConnect(){
		log.info("testing connect");
		Properties prob = new Properties();
		prob.setProperty("User", "lol");
		prob.setProperty("PassWord", "12345");
		String URL = "JDBC:jdbc:dbms";
		try {
			driver.connect(URL, prob);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(driver.testConnect);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetProperties(){
		log.info("testing getProperties");
		Properties prob = new Properties();
		prob.setProperty("DriverName", "");
		prob.setProperty("User", "");
		prob.setProperty("PassWord", "");
		String URL = "JDBC:jdbc:dbms";
		DriverPropertyInfo[] actualArray = null;
		try {
			 actualArray = driver.getPropertyInfo(URL, prob);
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		DriverPropertyInfo[] expectedArray = new DriverPropertyInfo[3];
		expectedArray[0] = new DriverPropertyInfo("DriverName", "jdbc:Driver");
		expectedArray[1] = new DriverPropertyInfo("User", "lol");
		expectedArray[2] = new DriverPropertyInfo("PassWord", "12345");
		boolean test = true;
		for (int i = 0; i < actualArray.length; i++) {
			String Key = actualArray[i].name;
			String Value = actualArray[i].value;
			test = false;
			for (int j = 0; j < expectedArray.length; j++) {
				if(Key.equals(expectedArray[j].name)){
					if(Value.equals(expectedArray[j].value)){
						test = true;
					}
				}
			}
			if(!test) break;
		}
		boolean test2 = true;
		for (int i = 0; i < expectedArray.length; i++) {
			String Key = expectedArray[i].name;
			String Value = expectedArray[i].value;
			test2 = false;
			for (int j = 0; j < actualArray.length; j++) {
				if(Key.equals(actualArray[j].name)){
					if(Value.equals(actualArray[j].value)){
						test2 = true;
					}
				}
			}
			if(!test2) break;
		}
		if(test && test2) assertTrue(true);
		else assertTrue(false);
	}
	
	@Test
	public void testCreateStatement(){
//		log.info("testing createStatement");
//		try {
//			//con.createStatment();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertTrue(con.test);
	}
	

}
