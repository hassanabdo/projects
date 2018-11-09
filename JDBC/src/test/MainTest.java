package test;

import java.io.File;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import dbms.TestUnit;

import jdbc.*;

public class MainTest {
	public static void main(String[] args) {
//		Result result = JUnitCore.runClasses(UnitTest.class);
		Logger log = Logger.getLogger(MainTest.class);
//		for (Failure failure : result.getFailures()) {
//			log.info(failure.toString());
//		}
//		log.info("done");
//		System.out.println(result.wasSuccessful());
//		Result result2 = JUnitCore.runClasses(TestClose.class);
//		for (Failure failure : result2.getFailures()) {
//			System.out.println(failure.toString());
//		}
//		System.out.println(result2.wasSuccessful());
		Result result3 = JUnitCore.runClasses(testCondition.class);
		for (Failure failure : result3.getFailures()) {
			log.info(failure.toString());
		}
		log.info(result3.wasSuccessful());
	}

}
