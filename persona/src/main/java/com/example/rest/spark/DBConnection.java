package com.example.rest.spark;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import scala.runtime.AbstractFunction0;

public class DBConnection extends AbstractFunction0<Connection> implements Serializable {

	private String driverClassName;
	private String connectionUrl;
	private String userName;
	private String password;

	public DBConnection(String driverClassName, String connectionUrl, String userName, String password) {
		this.driverClassName = driverClassName;
		this.connectionUrl = connectionUrl;
		this.userName = userName;
		this.password = password;
	}

	@Override
	public Connection apply() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load driver class");
			e.printStackTrace();
		}

		Properties properties = new Properties();
		properties.setProperty("user", userName);
		properties.setProperty("password", password);

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionUrl, properties);
		} catch (SQLException e) {
			System.err.println("Error in DBConnnection");
			e.printStackTrace();
		}

		return connection;
	}
}