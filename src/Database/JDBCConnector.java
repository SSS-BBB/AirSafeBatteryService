package Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnector {
	
	private static Connection connection;
	
	public static void connect() {
		// You need to create your own config file and your own local sql server before connecting to MySQL
		
		// Get Database properties
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream("config/config.properties");
			properties.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Connect the database
		try {
			connection = DriverManager.getConnection(properties.getProperty("db.url"),
					properties.getProperty("db.username"), properties.getProperty("db.password"));
			
			// Get query result test
			/*
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");

			while (resultSet.next()) {
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				String email = resultSet.getString("email");
				System.out.println(firstName + " " + lastName + " " + email);
			}
			*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}
}