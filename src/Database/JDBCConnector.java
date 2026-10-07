package Database;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Struct.ForRentPowerBank;

public class JDBCConnector {

	private static Connection connection;

	public static void connect() {
		// You need to create your own config file and your own local sql server before
		// connecting to MySQL

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
			 * Statement statement = connection.createStatement(); ResultSet resultSet =
			 * statement.executeQuery("SELECT * FROM USER");
			 * 
			 * while (resultSet.next()) { String firstName =
			 * resultSet.getString("first_name"); String lastName =
			 * resultSet.getString("last_name"); String email =
			 * resultSet.getString("email"); System.out.println(firstName + " " + lastName +
			 * " " + email); }
			 * 
			 * try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<ForRentPowerBank> getForRentPowerBank() {

		/*
		 * Dictionary<String, Double> numberMinFilterList = new Hashtable<String,
		 * Double>(); Dictionary<String, Double> numberMaxFilterList = new
		 * Hashtable<String, Double>(); Dictionary<String, String> textFilterList = new
		 * Hashtable<String, String>();
		 * 
		 * numberMinFilterList.put("wh", 38.0); numberMinFilterList.put("max_duration",
		 * 5.0);
		 * 
		 * numberMaxFilterList.put("price_per_day", 120.0);
		 * numberMaxFilterList.put("late_fee_per_day", 70.0);
		 * 
		 * textFilterList.put("address", "สนามบินดอนเมือง");
		 */

		return getForRentPowerBank("", "",
				"", "",
				-1, -1,
				-1, -1,
				-1, -1,
				"", true);
	}

	public static ArrayList<String> getPowerBankChargerType(String brand, String name, String model, boolean isInput) {
		try {
			if (connection.isClosed())
				connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (connection == null) {
			System.err.println("Database is not connected, unable to get data from input table.");
			return null;
		}

		ArrayList<String> chargerType = new ArrayList<String>();
		String inOrOut = (isInput) ? "INPUT" : "OUTPUT";
		String query = "SELECT * FROM POWERBANK" + inOrOut + " WHERE Brand = ? AND Name = ? AND Model = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, brand);
			ps.setString(2, name);
			ps.setString(3, model);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				chargerType.add(resultSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return chargerType;

	}

	public static PreparedStatement createForRentFilterStatement(String unfilteredQuery,
			String addressFilter, String brandFilter,
			String inputFilter, String outputFilter,
			double minPrice, double maxPrice,
			double minCap, double maxCap,
			double minWeight, double maxWeight,
			String orderByAttribute, boolean ascending) {

		try {
			if (connection == null || connection.isClosed()) {
				System.err.println("Database is not connected, unable to add filter to query " + unfilteredQuery);
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Query Builder
		StringBuilder filteredQuery = new StringBuilder(unfilteredQuery + " WHERE 1 = 1");

		// add filter
		ArrayList<Double> doubleParameters = new ArrayList<Double>();
		
		if (minPrice >= 0) {
			filteredQuery.append(" AND price_per_day >= ?");
			doubleParameters.add(minPrice);
		}
		
		if (maxPrice >= 0) {
			filteredQuery.append(" AND price_per_day <= ?");
			doubleParameters.add(maxPrice);
		}
		
		if (minCap >= 0) {
			filteredQuery.append(" AND capacity >= ?");
			doubleParameters.add(minCap);
		}
		
		if (maxCap >= 0) {
			filteredQuery.append(" AND capacity <= ?");
			doubleParameters.add(maxCap);
		}
		
		if (minWeight > 0) {
			filteredQuery.append(" AND weight >= ?");
			doubleParameters.add(minWeight);
		}
		
		if (maxWeight > 0) {
			filteredQuery.append(" AND weight <= ?");
			doubleParameters.add(maxWeight);
		}
		
		ArrayList<String> textParameters = new ArrayList<String>();
		
		if (!addressFilter.isEmpty()) {
			filteredQuery.append(" AND address = ?");
			textParameters.add(addressFilter);
		}
		
		if (!brandFilter.isEmpty()) {
			filteredQuery.append(" AND brand = ?");
			textParameters.add(brandFilter);
		}

		// add order by
		ArrayList<String> allowedAttributes = new ArrayList<String>();
		allowedAttributes.add("brand");
		allowedAttributes.add("name");
		allowedAttributes.add("model");
		allowedAttributes.add("address");
		allowedAttributes.add("locker_number");
		allowedAttributes.add("late_fee_per_day");
		allowedAttributes.add("price_per_day");
		allowedAttributes.add("max_duration");
		allowedAttributes.add("capacity");
		allowedAttributes.add("wh");
		allowedAttributes.add("width");
		allowedAttributes.add("length");
		allowedAttributes.add("height");
		allowedAttributes.add("weight");
		
		if (orderByAttribute != null && !orderByAttribute.isEmpty()) {
			if (allowedAttributes.contains(orderByAttribute.toLowerCase())) {
				String orderType = (ascending) ? "ASC" : "DESC";
				filteredQuery.append(" ORDER BY ").append(orderByAttribute).append(" " + orderType);
			}
		}

		// Statement
		try {
			PreparedStatement ps = connection.prepareStatement(filteredQuery.toString());

			// add number parameters to prepared statement
			for (int i = 0; i < doubleParameters.size(); i++) {
				ps.setDouble(i + 1, doubleParameters.get(i));
			}

			// add text parameters to prepared statement
			int doubleParaSize = doubleParameters.size();
			for (int i = 0; i < textParameters.size(); i++) {
				ps.setString(i + doubleParaSize + 1, textParameters.get(i));
			}

			return ps;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static ArrayList<ForRentPowerBank> getForRentPowerBank(
			String addressFilter, String brandFilter,
			String inputFilter, String outputFilter,
			double minPrice, double maxPrice,
			double minCap, double maxCap,
			double minWeight, double maxWeight,
			String orderByAttribute, boolean ascending) {
		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to get data from for rent power bank table.");
			return null;
		}

		String query = "SELECT * FROM FORRENTPOWERBANK NATURAL JOIN POWERBANK";

		try {
			

			PreparedStatement ps = createForRentFilterStatement(query, 
					addressFilter, brandFilter,
					inputFilter, outputFilter,
					minPrice, maxPrice,
					minCap, maxCap,
					minWeight, maxWeight,
					orderByAttribute, ascending);

			ResultSet resultSet = ps.executeQuery();

			ArrayList<ForRentPowerBank> forRentPowerBankList = new ArrayList<ForRentPowerBank>();

			while (resultSet.next()) {
				ForRentPowerBank powerBank = new ForRentPowerBank();
				// Power bank attributes
				powerBank.deviceInfo.brand = resultSet.getString("brand");
				powerBank.deviceInfo.name = resultSet.getString("name");
				powerBank.deviceInfo.model = resultSet.getString("model");
				powerBank.deviceInfo.capacity = resultSet.getDouble("capacity");
				powerBank.deviceInfo.wh = resultSet.getDouble("wh");
				powerBank.deviceInfo.width = resultSet.getDouble("width");
				powerBank.deviceInfo.length = resultSet.getDouble("length");
				powerBank.deviceInfo.height = resultSet.getDouble("height");
				powerBank.deviceInfo.weight = resultSet.getDouble("weight");

				// For rent attributes
				powerBank.address = resultSet.getString("address");
				powerBank.lockerNumber = resultSet.getInt("locker_number");
				powerBank.maxDuration = resultSet.getInt("max_duration");
				powerBank.lateFeePerDay = resultSet.getDouble("late_fee_per_day");
				powerBank.pricePerDay = resultSet.getDouble("price_per_day");

				// Image
				try {
					powerBank.deviceInfo.image = ImageIO.read(new ByteArrayInputStream(resultSet.getBytes("image")));
				} catch (IOException e) {
					powerBank.deviceInfo.image = null;
					e.printStackTrace();
				}

				// Input
				powerBank.deviceInfo.input = getPowerBankChargerType(powerBank.deviceInfo.brand,
						powerBank.deviceInfo.name, powerBank.deviceInfo.model, true);
				// Output
				powerBank.deviceInfo.output = getPowerBankChargerType(powerBank.deviceInfo.brand,
						powerBank.deviceInfo.name, powerBank.deviceInfo.model, false);

				forRentPowerBankList.add(powerBank);
			}

			return forRentPowerBankList;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ImageIcon byteArrayToImageIcon(byte[] byteArray) {
		if (byteArray == null || byteArray.length == 0)
			return null;

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArray));
			if (image == null)
				return null;

			return new ImageIcon(image);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void closeConnection() {
		if (connection == null) {
			System.err.println("Database is not connected, unable to close connection.");
			return;
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean insertIntoPowerbank(String brand, String name, String model, double capacity, double wh,
			double width, double length, double height, double weight, String imagePath, String[] inputArray,
			String[] outputArray) {
		boolean insertPowerBankStatus = insertIntoPowerbank(brand, name, model, capacity, wh, width, length, height,
				weight, imagePath);

		if (!insertPowerBankStatus)
			return false;

		// Insert Input
		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into for rent power bank table.");
			return false;
		}
		String query = "INSERT INTO POWERBANKINPUT VALUES (?, ?, ?, ?)";
		for (String input : inputArray) {
			query = "INSERT INTO POWERBANKINPUT VALUES (?, ?, ?, ?)";
			try (PreparedStatement ps = connection.prepareStatement(query)) {
				ps.setString(1, input);
				ps.setString(2, brand);
				ps.setString(3, name);
				ps.setString(4, model);

				boolean success = ps.executeUpdate() > 0;

				if (!success) {
					System.err.println(
							"Unable to insert input " + input + " to " + "(" + brand + "," + name + "," + model + ")");
					closeConnection();
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				closeConnection();
				return false;
			}
		}

		// Insert Output
		query = "INSERT INTO POWERBANKOUTPUT VALUES (?, ?, ?, ?)";
		for (String output : outputArray) {
			query = "INSERT INTO POWERBANKOUTPUT VALUES (?, ?, ?, ?)";
			try (PreparedStatement ps = connection.prepareStatement(query)) {
				ps.setString(1, output);
				ps.setString(2, brand);
				ps.setString(3, name);
				ps.setString(4, model);

				boolean success = ps.executeUpdate() > 0;

				if (!success) {
					System.err.println("Unable to insert output " + output + " to " + "(" + brand + "," + name + ","
							+ model + ")");
					closeConnection();
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				closeConnection();
				return false;
			}
		}

		// Successful insert
		closeConnection();
		return true;
	}

	public static boolean insertIntoPowerbank(String brand, String name, String model, double capacity, double wh,
			double width, double length, double height, double weight, String imagePath) {

		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into for rent power bank table.");
			return false;
		}

		String query = "INSERT INTO POWERBANK VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		InputStream inputStream = JDBCConnector.class.getResourceAsStream(imagePath);

		if (inputStream == null) {
			System.err.println("Couldn't find " + imagePath + ". Unable to insert " + "(" + brand + "," + name + ","
					+ model + ")" + " to PowerBank table.");
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
				System.out
						.println("(" + brand + "," + name + "," + model + ") inserted to PowerBank table sucessfully!");
			} else {
				System.err.println(
						"Unable to insert " + "(" + brand + "," + name + "," + model + ")" + " to PowerBank table.");
			}

			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return sucess;

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			return false;
		}

	}
}