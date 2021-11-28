/*
* DBConnect.java
* Programmer: Yongtao Wang
* v 1.01
* Connection for database
*/
package dao;

import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.util.Properties;
import java.io.FileInputStream;

public class DBConnect  {

	protected Connection connection;
	public Connection getConnection() { return connection; }

	private static String url;
	private static String username;
	private static String password;
	private String configFile;
	public DBConnect() {
		Properties prop = new Properties();
		// load database credential from local configration file
		configFile = "app.config";
		if (System.getenv("ENV") != null && System.getenv("ENV").equals("DEV")) {
			configFile = "dev.app.config";
		}
		System.out.println(configFile);
		try (FileInputStream fis = new FileInputStream(configFile)) {
			prop.load(fis);
		} catch ( Exception e) {
			e.printStackTrace();
			System.out.println("failed to load local config, please check it exists!");
			System.exit(-1);
		}
		url = "jdbc:mysql://" + prop.getProperty("db_host") + "?autoReconnect=true&useSSL=false";
		username = prop.getProperty("db_username");
		password = prop.getProperty("db_password");
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Error creating connection to database: " + e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
}