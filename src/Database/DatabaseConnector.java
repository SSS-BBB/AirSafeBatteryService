package Database;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Struct.CheckHistory;
import Struct.DepositedPowerBank;
import Struct.ForRentPowerBank;
import Struct.PowerBank;
import Struct.PowerBankRule;
import Struct.RentedPowerBank;
import Struct.UserDetail;
import Utils.PasswordHasher;
import Utils.Utils;

public class DatabaseConnector {

	private static Connection connection;

	public static void userConnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				closeConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// You need to create your own config file and your own local sql server before
		// connecting to MySQL

		// Get Database properties
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream("config/user_database_config.properties");
			properties.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Connect the database
		try {
			connection = DriverManager.getConnection(properties.getProperty("db.url"),
					properties.getProperty("db.username"), properties.getProperty("db.password"));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void lockerConnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				closeConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// You need to create your own config file and your own local sql server before
		// connecting to MySQL

		// Get Database properties
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream("config/locker_database_config.properties");
			properties.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Connect the database
		try {
			connection = DriverManager.getConnection(properties.getProperty("db.url"),
					properties.getProperty("db.username"), properties.getProperty("db.password"));

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static Connection getConnection() {
		return connection;
	}

	public static int insertIntoTransaction(double paymentAmount, int userID) {
		if (connection == null) {
			System.err.println("Database is not connected, unable to insert into transaction table.");
			return -1;
		}

		String query = "INSERT INTO TRANSACTION (PAYMENT_TIMESTAMP, PAYMENT_AMOUNT, USER_ID) VALUES (NOW(), ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setDouble(1, paymentAmount);
			ps.setInt(2, userID);

			boolean sucess = ps.executeUpdate() > 0;

			if (!sucess)
				return -1;

			try (ResultSet resultSet = ps.getGeneratedKeys()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}

			return -1;
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		String query = "SELECT * FROM USER WHERE USER_ID = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);
			ResultSet resultSet = ps.executeQuery();
			int count = 0;
			while (resultSet.next())
				count++;
			return count;
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		String query = "SELECT * FROM TRANSACTION WHERE PAYMENT_ID = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, paymentId);
			ResultSet resultSet = ps.executeQuery();
			int count = 0;
			while (resultSet.next())
				count++;
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static PowerBankRule getValidRule() {
		if (!checkConnection("Unable to get a valid power bank rule."))
			return null;

		PowerBankRule rule = new PowerBankRule();

		String query = "SELECT * FROM POWERBANKRULE "
				+ "WHERE EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM POWERBANKRULE) "
				+ "AND NOW() >= EFFECTIVE_DATE AND NOW() < END_DATE";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				rule.ruleId = resultSet.getInt("rule_id");
				rule.maxCarry = resultSet.getInt("max_carry");
				rule.maxWh = resultSet.getInt("max_wh");
				rule.checkedBaggage = resultSet.getBoolean("checked_baggage");
				rule.carryOnBaggage = resultSet.getBoolean("carry_on_baggage");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rule;
	}

	public static ArrayList<CheckHistory> getCheckHistory(int userId) {
		if (!checkConnection("Unable to get check history data."))
			return null;

		ArrayList<CheckHistory> checkHistoryList = new ArrayList<CheckHistory>();

		String query = "SELECT * FROM CHECKHISTORY NATURAL JOIN POWERBANKRULE " + "WHERE USER_ID = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				CheckHistory checkHistory = new CheckHistory();
				checkHistory.checkId = resultSet.getInt("check_id");
				checkHistory.checkTime = resultSet.getTimestamp("check_timestamp").toLocalDateTime();
				checkHistory.capacity = resultSet.getDouble("capacity");
				if (resultSet.wasNull())
					checkHistory.capacity = -1;
				checkHistory.wh = resultSet.getDouble("wh");
				checkHistory.ruleId = resultSet.getInt("rule_id");

				// status
				double maxWh = resultSet.getDouble("max_wh");
				checkHistory.approve = checkHistory.wh <= maxWh;
				checkHistory.maxWh = maxWh;
				checkHistory.endDate = resultSet.getDate("end_date").toLocalDate();

				checkHistoryList.add(checkHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return checkHistoryList;
	}

	public static ArrayList<ForRentPowerBank> getForRentPowerBank() {

		return getForRentPowerBank("", "", "", "", -1, -1, -1, -1, -1, -1, "", true);
	}

	public static ArrayList<String> getPowerBankChargerType(int powerBankId, boolean isInput) {
		if (!checkConnection("unable to get data from input table.")) return null;

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

	public static PreparedStatement createForRentFilterStatement(String addressFilter, String brandFilter,
			String inputFilter, String outputFilter, double minPrice, double maxPrice, double minCap, double maxCap,
			double minWeight, double maxWeight, String orderByAttribute, boolean ascending) {

		try {
			if (connection == null || connection.isClosed()) {
				System.err.println("Database is not connected, unable to create for rent filter query");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Query Builder
		StringBuilder filteredQuery = new StringBuilder(
				"SELECT * FROM FORRENTPOWERBANK P NATURAL JOIN POWERBANK" + " WHERE 1 = 1");

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
			filteredQuery
					.append(" AND ? IN (SELECT INPUT_TYPE FROM POWERBANKINPUT WHERE POWERBANK_ID = P.POWERBANK_ID)");
			textParameters.add(inputFilter);
		}

		if (!outputFilter.isEmpty()) {
			filteredQuery
					.append(" AND ? IN (SELECT OUTPUT_TYPE FROM POWERBANKOUTPUT WHERE POWERBANK_ID = P.POWERBANK_ID)");
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

	public static ArrayList<Integer> getUnavailableDepositLocker(String address) {
		// Locker from DepositedPowerBank
		if (!checkConnection("Unable to get unavailable deposit locker."))
			return null;
		ArrayList<Integer> unAvailableLocker = new ArrayList<Integer>();

		// DepositedPowerBank
		String query = "SELECT DISTINCT(LOCKER_NUMBER) FROM DEPOSITEDPOWERBANK WHERE ADDRESS = ? " +
					   " AND DEPOSIT_STATUS != 'รับคืนแล้ว' AND DEPOSIT_STATUS != 'ไม่ได้ฝาก'";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, address);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				unAvailableLocker.add(resultSet.getInt("locker_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return unAvailableLocker;
	}

	public static ArrayList<Integer> getUnavailableRentLocker(String address) {
		// Locker from RentedPowerBank and ForRentPowerBank
		if (!checkConnection("Unable to get unavailable rent locker."))
			return null;
		ArrayList<Integer> unAvailableLocker = new ArrayList<Integer>();

		// RentedPowerBank
		String query = "SELECT DISTINCT(LOCKER_NUMBER) FROM RENTEDPOWERBANK WHERE PICK_UP_ADDRESS = ? AND RENT_STATUS = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, address);
			ps.setString(2, "ยังไม่รับ");
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				unAvailableLocker.add(resultSet.getInt("locker_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		// ForRentPowerBank
		query = "SELECT DISTINCT(LOCKER_NUMBER) FROM FORRENTPOWERBANK WHERE ADDRESS = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, address);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				unAvailableLocker.add(resultSet.getInt("locker_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return unAvailableLocker;
	}

	public static ArrayList<String> getDistinctBrand() {
		if (!checkConnection("Unable to get distinct brands."))
			return null;

		ArrayList<String> brandList = new ArrayList<String>();

		String query = "SELECT DISTINCT(BRAND) FROM POWERBANK";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				brandList.add(resultSet.getString("brand"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return brandList;
	}

	public static ArrayList<String> getModelFromBrand(String brand) {
		if (!checkConnection("Unable to get model from a brand."))
			return null;

		ArrayList<String> modelList = new ArrayList<String>();

		String query = "SELECT DISTINCT(MODEL) FROM POWERBANK WHERE BRAND = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, brand);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				modelList.add(resultSet.getString("model"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return modelList;
	}

	public static boolean powerBankExist(String brand, String model) {
		if (!checkConnection("Unable to check if the power bank exists."))
			return false;

		String query = "SELECT POWERBANK_ID FROM POWERBANK WHERE BRAND = ? AND MODEL = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, brand);
			ps.setString(2, model);
			ResultSet resultSet = ps.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static int updateLateStatus() {
		// Note: This function should be on the back end server
		// but since we don't have that we put it on the front end '-'
		// In fact this entire class should be on the server lol

		if (!checkConnection("Unable to update late status."))
			return -1;

		// Update Rent
		// have not received -> didn't receive the device
		String query = "UPDATE RENTEDPOWERBANK SET RENT_STATUS = 'ไม่ได้รับ' "
				+ "WHERE CURDATE() > RETURN_DATE AND RENT_STATUS = 'ยังไม่รับ'";
		int rowChanged = 0;
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			rowChanged += ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// activated -> late return
		query = "UPDATE RENTEDPOWERBANK SET RENT_STATUS = 'ส่งคืนสาย' "
				+ "WHERE CURDATE() > RETURN_DATE AND RENT_STATUS = 'ใช้งานได้'";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			rowChanged += ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Update Deposit
		// locker reserved -> didn't deposit
		query = "UPDATE DEPOSITEDPOWERBANK SET DEPOSIT_STATUS = 'ไม่ได้ฝาก' "
				+ "WHERE CURDATE() > RETURN_DATE AND DEPOSIT_STATUS = 'จองที่ฝากแล้ว'";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			rowChanged += ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// is being deposited -> late pick up
		query = "UPDATE DEPOSITEDPOWERBANK SET DEPOSIT_STATUS = 'รับคืนสาย' "
				+ "WHERE CURDATE() > RETURN_DATE AND DEPOSIT_STATUS = 'กำลังฝาก'";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			rowChanged += ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rowChanged;
	}

	public static PowerBank getPowerBank(String brand, String model, boolean getChargerType) {
		if (!checkConnection("Unable to get data from power bank table."))
			return null;

		PowerBank powerBank = null;

		String query = "SELECT * FROM POWERBANK WHERE BRAND = ? AND MODEL = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, brand);
			ps.setString(2, model);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				powerBank = new PowerBank();
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
				powerBank.imageFormat = resultSet.getString("image_format");

				if (getChargerType) {
					powerBank.input = getPowerBankChargerType(powerBank.powerBankId, true);
					powerBank.output = getPowerBankChargerType(powerBank.powerBankId, false);
				}

				// Image
				try {
					powerBank.image = ImageIO.read(new ByteArrayInputStream(resultSet.getBytes("image")));
				} catch (IOException e) {
					powerBank.image = null;
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return powerBank;
	}

	public static ArrayList<PowerBank> getPowerBank(int powerBankId, boolean getChargerType) {
		if (!checkConnection("Unable to get data from power bank table."))
			return null;

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
				powerBank.imageFormat = resultSet.getString("image_format");

				if (getChargerType) {
					powerBank.input = getPowerBankChargerType(powerBank.powerBankId, true);
					powerBank.output = getPowerBankChargerType(powerBank.powerBankId, false);
				}

				// Image
				try {
					powerBank.image = ImageIO.read(new ByteArrayInputStream(resultSet.getBytes("image")));
				} catch (IOException e) {
					powerBank.image = null;
					e.printStackTrace();
				}

				powerBankList.add(powerBank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return powerBankList;
	}

	public static ArrayList<DepositedPowerBank> getDepositedPowerBank(int userId, boolean showPastDue,
			boolean showOnlyInprogress) {
		if (!checkConnection("Unable to get deposited power bank data."))
			return null;

		ArrayList<DepositedPowerBank> depositedPowerBankList = null;

		StringBuilder queryBuilder = new StringBuilder(
				"SELECT * FROM DEPOSITEDPOWERBANK NATURAL JOIN POWERBANK NATURAL JOIN TRANSACTION WHERE USER_ID = ?");
		if (showPastDue) {
			queryBuilder.append(" AND CURDATE() > RETURN_DATE");
			queryBuilder.append(" AND DEPOSIT_STATUS != 'รับคืนแล้ว' AND DEPOSIT_STATUS != 'ไม่ได้ฝาก'");
		}

		else if (showOnlyInprogress) {
			queryBuilder.append(" AND DEPOSIT_STATUS != 'รับคืนแล้ว' AND DEPOSIT_STATUS != 'ไม่ได้ฝาก'");
		}

		String query = queryBuilder.toString();
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);

			depositedPowerBankList = new ArrayList<DepositedPowerBank>();
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				// get device info
				PowerBank deviceInfo = new PowerBank();
				deviceInfo.powerBankId = resultSet.getInt("powerbank_id");
				deviceInfo.brand = resultSet.getString("brand");
				deviceInfo.name = resultSet.getString("name");
				deviceInfo.model = resultSet.getString("model");
				deviceInfo.capacity = resultSet.getDouble("capacity");
				deviceInfo.wh = resultSet.getDouble("wh");
				deviceInfo.width = resultSet.getDouble("width");
				deviceInfo.length = resultSet.getDouble("length");
				deviceInfo.height = resultSet.getDouble("height");
				deviceInfo.weight = resultSet.getDouble("weight");

				// get device image
				try {
					deviceInfo.image = ImageIO.read(new ByteArrayInputStream(resultSet.getBytes("image")));
				} catch (IOException e) {
					deviceInfo.image = null;
					e.printStackTrace();
				}
				deviceInfo.imageFormat = resultSet.getString("image_format");

				// get input
				deviceInfo.input = getPowerBankChargerType(deviceInfo.powerBankId, true);

				// get output
				// get input
				deviceInfo.output = getPowerBankChargerType(deviceInfo.powerBankId, false);

				// get deposit information
				DepositedPowerBank depositedPowerBank = new DepositedPowerBank();
				depositedPowerBank.deviceInfo = deviceInfo;
				depositedPowerBank.depositId = resultSet.getInt("deposit_id");
				depositedPowerBank.depositStatus = resultSet.getString("deposit_status");
				depositedPowerBank.address = resultSet.getString("address");
				depositedPowerBank.depositDate = resultSet.getDate("deposit_date").toLocalDate();
				depositedPowerBank.returnDate = resultSet.getDate("return_date").toLocalDate();
				depositedPowerBank.lockerNumber = resultSet.getInt("locker_number");
				depositedPowerBank.lateFeePerDay = resultSet.getDouble("late_fee_per_day");
				depositedPowerBank.depositPrice = resultSet.getDouble("deposit_price");
				depositedPowerBank.lockerPassword = resultSet.getString("locker_password");
				depositedPowerBank.transactionTime = resultSet.getTimestamp("payment_timestamp").toLocalDateTime();

				depositedPowerBankList.add(depositedPowerBank);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return depositedPowerBankList;
	}
	
	public static DepositedPowerBank getDepositInfoFromLocker(int userId, String lockerPassword, String address) {
		if (!checkConnection("Unable to get deposit locker number and status."))
			return null;
		
		String query = "SELECT DEPOSIT_ID, LOCKER_NUMBER, DEPOSIT_STATUS, LATE_FEE_PER_DAY, RETURN_DATE FROM DEPOSITEDPOWERBANK NATURAL JOIN TRANSACTION" +
					   " WHERE USER_ID = ? AND LOCKER_PASSWORD = ? AND ADDRESS = ?" + 
					   " AND CURDATE() >= DEPOSIT_DATE AND DEPOSIT_STATUS != 'ไม่ได้ฝาก'" +
					   " AND DEPOSIT_STATUS != 'รับคืนแล้ว'";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);
			ps.setString(2, lockerPassword);
			ps.setString(3, address);
			
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				DepositedPowerBank depositInfo = new DepositedPowerBank();
				depositInfo.depositId = resultSet.getInt("deposit_id");
				depositInfo.lockerNumber = resultSet.getInt("locker_number");
				depositInfo.depositStatus = resultSet.getString("deposit_status");
				depositInfo.lateFeePerDay = resultSet.getDouble("late_fee_per_day");
				depositInfo.returnDate = resultSet.getDate("return_date").toLocalDate();
				return depositInfo;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static RentedPowerBank getRentInfoFromLocker(int userId, String lockerPassword, String pickUpAddress) {
		if (!checkConnection("Unable to get rent locker number."))
			return null;
		
		String query = "SELECT RENT_ID, LOCKER_NUMBER FROM RENTEDPOWERBANK NATURAL JOIN TRANSACTION" +
					  " WHERE USER_ID = ? AND LOCKER_PASSWORD = ? AND PICK_UP_ADDRESS = ?" +
					  " AND CURDATE() >= PICK_UP_DATE AND CURDATE() <= RETURN_DATE" +
					  " AND RENT_STATUS = 'ยังไม่รับ'";
		
		RentedPowerBank rentInfo = null;
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);
			ps.setString(2, lockerPassword);
			ps.setString(3, pickUpAddress);
			
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				rentInfo = new RentedPowerBank();
				rentInfo.rentID = resultSet.getInt("rent_id");
				rentInfo.lockerNumber = resultSet.getInt("locker_number");
				
				return rentInfo;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rentInfo;
	}
	
	public static ArrayList<RentedPowerBank> getRentedPowerBank(int userId, boolean showPastDue,
			boolean showOnlyInprogress) {
		if (!checkConnection("Unable to get rented power bank data."))
			return null;

		ArrayList<RentedPowerBank> rentedPowerBankList = null;

		StringBuilder queryBuilder = new StringBuilder(
				"SELECT * FROM RENTEDPOWERBANK NATURAL JOIN POWERBANK NATURAL JOIN TRANSACTION WHERE USER_ID = ?");
		if (showPastDue) {
			queryBuilder.append(" AND CURDATE() > RETURN_DATE");
			queryBuilder.append(" AND RENT_STATUS != 'คืนแล้ว' AND RENT_STATUS != 'ไม่ได้รับ'");
		}

		else if (showOnlyInprogress) {
			queryBuilder.append(" AND RENT_STATUS != 'คืนแล้ว' AND RENT_STATUS != 'ไม่ได้รับ'");
		}

		String query = queryBuilder.toString();
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);

			rentedPowerBankList = new ArrayList<RentedPowerBank>();
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				// get device info
				PowerBank deviceInfo = new PowerBank();
				deviceInfo.powerBankId = resultSet.getInt("powerbank_id");
				deviceInfo.brand = resultSet.getString("brand");
				deviceInfo.name = resultSet.getString("name");
				deviceInfo.model = resultSet.getString("model");
				deviceInfo.capacity = resultSet.getDouble("capacity");
				deviceInfo.wh = resultSet.getDouble("wh");
				deviceInfo.width = resultSet.getDouble("width");
				deviceInfo.length = resultSet.getDouble("length");
				deviceInfo.height = resultSet.getDouble("height");
				deviceInfo.weight = resultSet.getDouble("weight");

				// get device image
				try {
					deviceInfo.image = ImageIO.read(new ByteArrayInputStream(resultSet.getBytes("image")));
				} catch (IOException e) {
					deviceInfo.image = null;
					e.printStackTrace();
				}
				deviceInfo.imageFormat = resultSet.getString("image_format");

				// get input
				deviceInfo.input = getPowerBankChargerType(deviceInfo.powerBankId, true);

				// get output
				// get input
				deviceInfo.output = getPowerBankChargerType(deviceInfo.powerBankId, false);

				// get rent information
				RentedPowerBank rentedPowerBank = new RentedPowerBank();
				rentedPowerBank.deviceInfo = deviceInfo;
				rentedPowerBank.rentID = resultSet.getInt("rent_id");
				rentedPowerBank.lockerPassword = resultSet.getString("locker_password");
				rentedPowerBank.pickUpAddress = resultSet.getString("pick_up_address");
				rentedPowerBank.returnAddress = resultSet.getString("return_address");
				rentedPowerBank.rentDate = resultSet.getDate("pick_up_date").toLocalDate();
				rentedPowerBank.status = resultSet.getString("rent_status");
				rentedPowerBank.returnDate = resultSet.getDate("return_date").toLocalDate();
				rentedPowerBank.lateFeePerDay = resultSet.getDouble("late_fee_per_day");
				rentedPowerBank.rentPrice = resultSet.getDouble("rent_price");
				rentedPowerBank.lockerNumber = resultSet.getInt("locker_number");
				rentedPowerBank.transactionTime = resultSet.getTimestamp("payment_timestamp").toLocalDateTime();

				rentedPowerBankList.add(rentedPowerBank);
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return rentedPowerBankList;
	}

	public static ArrayList<ForRentPowerBank> getForRentPowerBank(String addressFilter, String brandFilter,
			String inputFilter, String outputFilter, double minPrice, double maxPrice, double minCap, double maxCap,
			double minWeight, double maxWeight, String orderByAttribute, boolean ascending) {
		if (connection == null) {
			System.err.println("Database is not connected, unable to get data from for rent power bank table.");
			return null;
		}

		try {

			PreparedStatement ps = createForRentFilterStatement(addressFilter, brandFilter, inputFilter, outputFilter,
					minPrice, maxPrice, minCap, maxCap, minWeight, maxWeight, orderByAttribute, ascending);

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
				powerBank.deviceInfo.imageFormat = resultSet.getString("image_format");

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

	public static boolean checkConnection(String errorMessage) {
		if (connection == null) {
			System.err.println("Null connection. " + errorMessage);
			return false;
		}

		try {
			if (connection.isClosed()) {
				System.err.println("Database is not connected. " + errorMessage);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void closeConnection() {
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

	private static byte[] bufferedImageToByte(BufferedImage image, String imageFormat) {
		if (image == null) {
			System.err.println("Null buffered image. Unable to turn this into to byte array.");
			return null;
		}

		if (!imageFormat.equalsIgnoreCase("jpg") && !imageFormat.equalsIgnoreCase("png")) {
			System.err.println("Only support jpg and png image format. Unable to turn this into to byte array.");
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, imageFormat, baos);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean updateRentStatus(int rentId, String status) {
		if (!checkConnection("Update rent status."))
			return false;
		
		String query = "UPDATE RENTEDPOWERBANK SET RENT_STATUS = ? WHERE RENT_ID = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, status);
			ps.setInt(2, rentId);
			
			boolean success = ps.executeUpdate() > 0;
			return success;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean updateDepositStatus(int depositId, String status) {
		if (!checkConnection("Update deposit status."))
			return false;
		
		String query = "UPDATE DEPOSITEDPOWERBANK SET DEPOSIT_STATUS = ? WHERE DEPOSIT_ID = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, status);
			ps.setInt(2, depositId);
			
			boolean success = ps.executeUpdate() > 0;
			return success;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean insertIntoCheckHistory(double capacity, double wh, int userId, int ruleId) {
		if (!checkConnection("Insert Into Check History Table."))
			return false;

		if (userId < 0)
			return false;

		String query = "INSERT INTO CHECKHISTORY (CHECK_TIMESTAMP, CAPACITY, WH, USER_ID, RULE_ID) "
				+ "VALUES (NOW(), ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			if (capacity < 0) {
				ps.setNull(1, Types.DOUBLE);
			} else {
				ps.setDouble(1, capacity);
			}

			ps.setDouble(2, wh);
			ps.setInt(3, userId);
			ps.setInt(4, ruleId);

			boolean result = ps.executeUpdate() > 0;
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean insertIntoCharger(int powerBankId, ArrayList<String> chargerList, boolean isInput) {
		// insert into input and output
		if (chargerList.isEmpty()) {
			System.err.println("Empty charger type. Unable to insert into power bank input or output table.");
			return false;
		}

		String inOrOut = (isInput) ? "INPUT" : "OUTPUT";

		StringBuilder queryBuilder = new StringBuilder("INSERT INTO POWERBANK" + inOrOut + " VALUES (?, ?)");
		for (int i = 1; i < chargerList.size(); i++) {
			queryBuilder.append(", (?, ?)");
		}

		String query = queryBuilder.toString();
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			for (int i = 1; i <= chargerList.size(); i++) {
				ps.setString(2 * i - 1, chargerList.get(i - 1));
				ps.setInt(2 * i, powerBankId);
			}

			boolean result = ps.executeUpdate() > 0;
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int insertIntoPowerbank(PowerBank powerBank) {
		if (!checkConnection("Unable to insert data into power bank table."))
			return -1;

		byte[] imageBytes = bufferedImageToByte(powerBank.image, powerBank.imageFormat);
		if (imageBytes == null) {
			System.err.println(
					"Couldn't turn buffered image into byte array. Unable to insert data into power bank table.");
			return -1;
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);

		// insert into power bank table
		String query = "INSERT INTO POWERBANK (BRAND, NAME, MODEL, CAPACITY, WH, WIDTH, LENGTH, HEIGHT, WEIGHT, IMAGE, IMAGE_FORMAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int powerBankId = -1;
		try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, powerBank.brand);
			ps.setString(2, powerBank.name);
			ps.setString(3, powerBank.model);
			ps.setDouble(4, powerBank.capacity);
			ps.setDouble(5, powerBank.wh);
			ps.setDouble(6, powerBank.width);
			ps.setDouble(7, powerBank.length);
			ps.setDouble(8, powerBank.height);
			ps.setDouble(9, powerBank.weight);
			ps.setBinaryStream(10, bais, imageBytes.length);
			ps.setString(11, powerBank.imageFormat);
			ps.executeUpdate();

			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next())
				powerBankId = resultSet.getInt(1);
			else {
				System.err
						.println("Couldn't get generated power bank id. Unable to insert into input and output table.");
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		// insert into input and output table
		boolean inputInsertResult = insertIntoCharger(powerBankId, powerBank.input, true);
		if (!inputInsertResult) {
			System.err.println("Couldn't insert into input power bank table.");
			return -1;
		}
		boolean outputInsertResult = insertIntoCharger(powerBankId, powerBank.input, false);
		if (!outputInsertResult) {
			System.err.println("Couldn't insert into output power bank table.");
			return -1;
		}

		return powerBankId;
	}

	public static boolean insertIntoPowerbank(String brand, String name, String model, double capacity, double wh,
			double width, double length, double height, double weight, String imagePath, String imageFormat,
			String[] inputArray, String[] outputArray) {
		boolean insertPowerBankStatus = insertIntoPowerbank(brand, name, model, capacity, wh, width, length, height,
				weight, imagePath, imageFormat);

		if (!insertPowerBankStatus)
			return false;

		if (!checkConnection("Unable to insert data into power bank table."))
			return false;

		// Get power bank id
		String query = "SELECT POWERBANK_ID FROM POWERBANK WHERE BRAND = ? AND MODEL = ?";
		int powerBankId = -1;
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, brand);
			ps.setString(2, model);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				powerBankId = resultSet.getInt("POWERBANK_ID");
			} else {
				System.err.println("Couldn't find power bank id with brand " + brand + " and mode " + model);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
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
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

		// Successful insert
		return true;
	}

	public static boolean removeForRentPowerBank(String address, int lockerNumber) {
		if (connection == null) {
			System.err.println("Database is not connected, unable to remove from for rent power bank table.");
			return false;
		}

		String query = "DELETE FROM FORRENTPOWERBANK WHERE ADDRESS = ? AND LOCKER_NUMBER = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, address);
			ps.setInt(2, lockerNumber);
			boolean sucess = ps.executeUpdate() > 0;
			return sucess;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static String insertIntoRentedPowerBank(RentedPowerBank powerBank, int paymentId) {
		if (powerBank == null || powerBank.deviceInfo == null) {
			System.err.println("Null powerbank, unable to insert into rented power bank table.");
			return "";
		}

		if (!checkConnection("Unable to insert into rented power bank table."))
			return "";

		// Make sure that there is a power bank with powerBankId on the power bank table
		if (getPowerBank(powerBank.deviceInfo.powerBankId, false).size() == 0) {
			System.err.println("No power bank with power bank id " + String.valueOf(powerBank.deviceInfo.powerBankId)
					+ " on the power bank table. unable to insert into rented power bank table.");
			return "";
		}

		// check transaction payment id
		if (getTransactionIdCount(paymentId) <= 0) {
			System.err.println("No transaction with payment id " + String.valueOf(paymentId)
					+ " on the transaction table. unable to insert into rented power bank table.");
			return "";
		}

		// generate locker password (3 characters(ignore case) 3 numbers)
		// ex. ABC123
		char[] alphabets = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
				'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
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
				+ "(POWERBANK_ID, LOCKER_PASSWORD, RETURN_ADDRESS, PICK_UP_ADDRESS, PICK_UP_DATE, RENT_STATUS, RETURN_DATE, LATE_FEE_PER_DAY, RENT_PRICE, LOCKER_NUMBER, PAYMENT_ID)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, powerBank.deviceInfo.powerBankId);
			ps.setString(2, lockerPassword);
			ps.setString(3, powerBank.returnAddress);
			ps.setString(4, powerBank.pickUpAddress);
			ps.setString(5, Utils.getDateFormat(powerBank.rentDate));
			ps.setString(6, "ยังไม่รับ");
			ps.setString(7, Utils.getDateFormat(powerBank.returnDate));
			ps.setDouble(8, powerBank.lateFeePerDay);
			ps.setDouble(9, powerBank.rentPrice);
			ps.setDouble(10, powerBank.lockerNumber);
			ps.setInt(11, paymentId);

			boolean success = ps.executeUpdate() > 0;

			if (success) {
				return lockerPassword;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	private static String generateRandomPassword() {
		// generate locker password (3 characters(ignore case) 3 numbers)
		// ex. ABC123
		char[] alphabets = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
				'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char randAlphanet1 = alphabets[Utils.randRange(0, alphabets.length - 1)];
		char randAlphanet2 = alphabets[Utils.randRange(0, alphabets.length - 1)];
		char randAlphanet3 = alphabets[Utils.randRange(0, alphabets.length - 1)];
		String lockerPassword = "" + randAlphanet1 + randAlphanet2 + randAlphanet3;

		String numRand1 = String.valueOf(Utils.randRange(0, 9));
		String numRand2 = String.valueOf(Utils.randRange(0, 9));
		String numRand3 = String.valueOf(Utils.randRange(0, 9));
		lockerPassword = lockerPassword + numRand1 + numRand2 + numRand3;

		return lockerPassword;
	}

	public static String insertIntoDepositedPowerBank(DepositedPowerBank depositInfo, int powerBankId, int paymentId) {
		if (!checkConnection("Unable to insert data into deposited power bank table."))
			return "";

		if (depositInfo == null || powerBankId < 0 || paymentId < 0) {
			System.err.println("Invalid parameters. Unable to insert data into deposited power bank table.");
			return "";
		}

		String query = "INSERT INTO DEPOSITEDPOWERBANK (POWERBANK_ID, DEPOSIT_STATUS, ADDRESS, DEPOSIT_DATE, RETURN_DATE, LOCKER_NUMBER, LATE_FEE_PER_DAY, DEPOSIT_PRICE, LOCKER_PASSWORD, PAYMENT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// insert data into DepositedPowerBank table
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, powerBankId);
			ps.setString(2, "จองที่ฝากแล้ว");
			ps.setString(3, depositInfo.address);
			ps.setString(4, Utils.getDateFormat(depositInfo.depositDate));
			ps.setString(5, Utils.getDateFormat(depositInfo.returnDate));
			ps.setInt(6, depositInfo.lockerNumber);
			ps.setDouble(7, depositInfo.lateFeePerDay);
			ps.setDouble(8, depositInfo.depositPrice);
			String lockerPassword = generateRandomPassword();
			ps.setString(9, lockerPassword);
			ps.setInt(10, paymentId);

			boolean success = ps.executeUpdate() > 0;
			if (success)
				return lockerPassword;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static boolean insertIntoPowerbank(String brand, String name, String model, double capacity, double wh,
			double width, double length, double height, double weight, String imagePath, String imageFormat) {
		if (!checkConnection("Unable to insert data into power bank table."))
			return false;

		String query = "INSERT INTO POWERBANK (BRAND, NAME, MODEL, CAPACITY, WH, WIDTH, LENGTH, HEIGHT, WEIGHT, IMAGE, IMAGE_FORMAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
			ps.setString(11, imageFormat);

			boolean sucess = ps.executeUpdate() > 0;

			if (sucess) {
				System.out
						.println("(" + brand + "," + name + "," + model + ") inserted to PowerBank table sucessfully!");
			} else {
				System.err.println(
						"Unable to insert " + "(" + brand + "," + name + "," + model + ")" + " to PowerBank table.");
			}

			return sucess;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}