package Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnector {
	
	private static Connection connection;
	private static Statement statement;
	
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
			statement = connection.createStatement();
			
			// Get query result test
			/*
			
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
	
	public static boolean insertIntoPowerbank(String brand, String name, String model,
			double capacity, double wh, double width, double length, double height, double weight,
			String imagePath) {
		
		
		if (connection == null)
		{
			System.err.println("Database is not connected, unable to insert into for rent power bank table.");
			return false;
		}
		
		String query = "INSERT INTO POWERBANK VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		InputStream inputStream = JDBCConnector.class.getResourceAsStream(imagePath);
		
		if (inputStream == null) {
			System.err.println("Couldn't find " + imagePath + ". Unable to insert " + "(" + brand + "," + name + "," + model + ")" + " to PowerBank table.");
			return false;
		}
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, brand);
			ps.setString(2, name);
			ps.setString(3, model);
			ps.setDouble(4, capacity);
			ps.setDouble(5, wh);
			ps.setDouble(6, width);
			ps.setDouble(7, length);
			ps.setDouble(8, height);
			ps.setDouble(9, weight);
			ps.setBlob(10, inputStream);
			
			boolean sucess = ps.executeUpdate() > 0;
			
			if (sucess) {
				System.out.println("(" + brand + "," + name + "," + model + ") inserted to PowerBank table sucessfully!");
			}
			else {
				System.err.println("Unable to insert " + "(" + brand + "," + name + "," + model + ")" + " to PowerBank table.");
			}
			
			return sucess;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}