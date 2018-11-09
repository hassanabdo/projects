package test;
import jdbc.*;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

public class TestClose {
	Logger log = Logger.getLogger(TestClose.class);
	Connection con = new Connection();
	@Test
	public void testClose(){
		log.info("testing close");
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(con.test);
	}

}
