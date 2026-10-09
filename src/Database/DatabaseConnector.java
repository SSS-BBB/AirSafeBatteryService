package Database;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Date;
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
import Struct.PowerBank;
import Struct.RentedPowerBank;
import Utils.Utils;

public class DatabaseConnector {

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
	
	public static int insertIntoTransaction(double paymentAmount, int userID) {
		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into transaction table.");
			return -1;
		}
		
		String query = "INSERT INTO TRANSACTION (PAYMENT_TIMESTAMP, PAYMENT_AMOUNT, USER_ID) VALUES (NOW(), ?, ?)";
		
		try(PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setDouble(1, paymentAmount);
			ps.setInt(2, userID);
			
			boolean sucess = ps.executeUpdate() > 0;
			
			if (!sucess) return -1;
			
			try (ResultSet resultSet = ps.getGeneratedKeys()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
			
			closeConnection();
			
			return -1;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static int getUserIdCount(int userId) {
		try {
			if (connection == null || connection.isClosed()) {
				System.err.println("Database is not connected, unable to get user id count.");
				return -1;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		String query = "SELECT * FROM USER WHERE USER_ID = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);
			ResultSet resultSet = ps.executeQuery();
			int count = 0;
			while (resultSet.next()) count++;
			return count;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static int getTransactionIdCount(int paymentId) {
		try {
			if (connection == null || connection.isClosed()) {
				System.err.println("Database is not connected, unable to get transaction id count.");
				return -1;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		String query = "SELECT * FROM TRANSACTION WHERE PAYMENT_ID = ?";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, paymentId);
			ResultSet resultSet = ps.executeQuery();
			int count = 0;
			while (resultSet.next()) count++;
			return count;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
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

	public static ArrayList<String> getPowerBankChargerType(int powerBankId, boolean isInput) {
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
		String query = "SELECT * FROM POWERBANK" + inOrOut + " WHERE POWERBANK_ID = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, powerBankId);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				chargerType.add(resultSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return chargerType;

	}

	public static PreparedStatement createForRentFilterStatement(
			String addressFilter, String brandFilter,
			String inputFilter, String outputFilter,
			double minPrice, double maxPrice,
			double minCap, double maxCap,
			double minWeight, double maxWeight,
			String orderByAttribute, boolean ascending) {

		try {
			if (connection == null || connection.isClosed()) {
				System.err.println("Database is not connected, unable to create for rent filter query");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Query Builder
		StringBuilder filteredQuery = new StringBuilder("SELECT * FROM FORRENTPOWERBANK P NATURAL JOIN POWERBANK" + " WHERE 1 = 1");

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
		
		// input output filter
		if (!inputFilter.isEmpty()) {
			filteredQuery.append(" AND ? IN (SELECT INPUT_TYPE FROM POWERBANKINPUT WHERE POWERBANK_ID = P.POWERBANK_ID)");
			textParameters.add(inputFilter);
		}
		
		if (!outputFilter.isEmpty()) {
			filteredQuery.append(" AND ? IN (SELECT OUTPUT_TYPE FROM POWERBANKOUTPUT WHERE POWERBANK_ID = P.POWERBANK_ID)");
			textParameters.add(outputFilter);
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
	
	public static ArrayList<PowerBank> getPowerBank(int powerBankId, boolean getChargerType) {
		if (connection == null) {
			System.err.println("Database is not connected, unable to get data from power bank table.");
			return null;
		}
		
		ArrayList<PowerBank> powerBankList = new ArrayList<PowerBank>();
		
		String query = "SELECT * FROM POWERBANK WHERE POWERBANK_ID = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, powerBankId);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				PowerBank powerBank = new PowerBank();
				powerBank.powerBankId = resultSet.getInt("powerbank_id");
				powerBank.brand = resultSet.getString("brand");
				powerBank.name = resultSet.getString("name");
				powerBank.model = resultSet.getString("model");
				powerBank.capacity = resultSet.getDouble("capacity");
				powerBank.wh = resultSet.getDouble("wh");
				powerBank.width = resultSet.getDouble("width");
				powerBank.length = resultSet.getDouble("length");
				powerBank.height = resultSet.getDouble("height");
				powerBank.weight = resultSet.getDouble("weight");
				
				if (getChargerType) {
					powerBank.input = getPowerBankChargerType(powerBank.powerBankId, true);
					powerBank.output = getPowerBankChargerType(powerBank.powerBankId, false);
				}
				
				powerBankList.add(powerBank);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return powerBankList;
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
		
		try {
			

			PreparedStatement ps = createForRentFilterStatement( 
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
				powerBank.deviceInfo.powerBankId = resultSet.getInt("powerbank_id");
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
				powerBank.deviceInfo.input = getPowerBankChargerType(powerBank.deviceInfo.powerBankId, true);
				// Output
				powerBank.deviceInfo.output = getPowerBankChargerType(powerBank.deviceInfo.powerBankId, false);

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

		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into for rent power bank table.");
			return false;
		}
		
		// Get power bank id
		String query = "SELECT POWERBANK_ID FROM POWERBANK WHERE BRAND = ? AND MODEL = ?";
		int powerBankId = -1;
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, brand);
			ps.setString(2, model);
			
			ResultSet resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				powerBankId = resultSet.getInt("POWERBANK_ID");
			}
			else {
				System.err.println("Couldn't find power bank id with brand " + brand + " and mode " + model);
				closeConnection();
				return false;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			closeConnection();
			return false;
		}
		
		// Insert Input
		query = "INSERT INTO POWERBANKINPUT VALUES (?, ?)";
		for (String input : inputArray) {
			query = "INSERT INTO POWERBANKINPUT VALUES (?, ?)";
			try (PreparedStatement ps = connection.prepareStatement(query)) {
				ps.setString(1, input);
				ps.setInt(2, powerBankId);

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
		query = "INSERT INTO POWERBANKOUTPUT VALUES (?, ?)";
		for (String output : outputArray) {
			query = "INSERT INTO POWERBANKOUTPUT VALUES (?, ?)";
			try (PreparedStatement ps = connection.prepareStatement(query)) {
				ps.setString(1, output);
				ps.setInt(2, powerBankId);

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
	
	public static boolean removeForRentPowerBank(String address, int lockerNumber) {
		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to remove from for rent power bank table.");
			return false;
		}
		
		String query = "DELETE FROM FORRENTPOWERBANK WHERE ADDRESS = ? AND LOCKER_NUMBER = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, address);
			ps.setInt(2, lockerNumber);
			boolean sucess = ps.executeUpdate() > 0;
			closeConnection();
			return sucess;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeConnection();
		return false;
	}
	
	public static String insertIntoRentedPowerBank(RentedPowerBank powerBank, int paymentId, int userId) {
		if (powerBank == null || powerBank.deviceInfo == null) {
			System.err.println("Null powerbank, unable to insert into rented power bank table.");
			return "";
		}
		
		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into rented power bank table.");
			return "";
		}
		
		// Make sure that there is a power bank with powerBankId on the power bank table
		if (getPowerBank(powerBank.deviceInfo.powerBankId, false).size() == 0) {
			System.err.println("No power bank with power bank id " + String.valueOf(powerBank.deviceInfo.powerBankId) + " on the power bank table. unable to insert into rented power bank table.");
			return "";
		}
		
		// check transaction payment id
		if (getTransactionIdCount(paymentId) <= 0) {
			System.err.println("No transaction with payment id " + String.valueOf(paymentId) + " on the transaction table. unable to insert into rented power bank table.");
			return "";
		}
		
		// check user id
		if (getUserIdCount(userId) <= 0) {
			System.err.println("No user with user id " + String.valueOf(userId) + " on the user table. unable to insert into rented power bank table.");
			return "";
		}
		
		// generate locker password (3 characters(ignore case) 3 numbers)
		// ex. ABC123
		char[] alphabets = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 
				'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char randAlphanet1 = alphabets[Utils.randRange(0, alphabets.length - 1)];
		char randAlphanet2 = alphabets[Utils.randRange(0, alphabets.length - 1)];
		char randAlphanet3 = alphabets[Utils.randRange(0, alphabets.length - 1)];
		String lockerPassword = "" + randAlphanet1 + randAlphanet2 + randAlphanet3;
		
		String numRand1 = String.valueOf(Utils.randRange(0, 9));
		String numRand2 = String.valueOf(Utils.randRange(0, 9));
		String numRand3 = String.valueOf(Utils.randRange(0, 9));
		lockerPassword = lockerPassword + numRand1 + numRand2 + numRand3;
		
		// Insert into RentedPowerBank
		String query = "INSERT INTO RENTEDPOWERBANK "
				+ "(POWERBANK_ID, LOCKER_PASSWORD, RETURN_ADDRESS, PICK_UP_ADDRESS, PICK_UP_DATE, RENT_STATUS, RETURN_DATE, LATE_FEE_PER_DAY, RENT_PRICE, LOCKER_NUMBER, USER_ID, PAYMENT_ID)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, powerBank.deviceInfo.powerBankId);
			ps.setString(2, lockerPassword);
			ps.setString(3, powerBank.returnAddress);
			ps.setString(4, powerBank.pickUpAddress);
			ps.setString(5, Utils.getCalendarDateFormat(powerBank.rentDate));
			ps.setString(6, "ยังไม่รับ");
			ps.setString(7, Utils.getCalendarDateFormat(powerBank.returnDate));
			ps.setDouble(8, powerBank.lateFeePerDay);
			ps.setDouble(9, powerBank.rentPrice);
			ps.setDouble(10, powerBank.lockerNumber);
			ps.setInt(11, userId);
			ps.setInt(12, paymentId);
			
			boolean success = ps.executeUpdate() > 0;
			
			if (success) {
				closeConnection();
				return lockerPassword;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		closeConnection();
		return "";
	}
	
	public static boolean insertIntoPowerbank(String brand, String name, String model, double capacity, double wh,
			double width, double length, double height, double weight, String imagePath) {
		connect();
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into for rent power bank table.");
			return false;
		}

		String query = "INSERT INTO POWERBANK (BRAND, NAME, MODEL, CAPACITY, WH, WIDTH, LENGTH, HEIGHT, WEIGHT, IMAGE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		InputStream inputStream = DatabaseConnector.class.getResourceAsStream(imagePath);

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