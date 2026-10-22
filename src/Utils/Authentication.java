package Utils;

import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DatabaseConnector;
import Struct.UserDetail;

public final class Authentication {
	
	public static int userRegister(UserDetail user, String passowrd) {
		if (!DatabaseConnector.checkConnection("Unable to register user."))
			return -1;

		// Hash the password
		try {
			String hashedPassword = PasswordHasher.hashPassword(passowrd.toCharArray());

			String query = "INSERT INTO USER (FIRST_NAME, LAST_NAME, EMAIL, PHONE, HASHED_PASSWORD) "
					+ "VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, user.firstName);
				ps.setString(2, user.lastName);
				ps.setString(3, user.email);
				ps.setString(4, user.phone);
				ps.setString(5, hashedPassword);

				boolean success = ps.executeUpdate() > 0;

				if (success) {
					ResultSet resultSet = ps.getGeneratedKeys();
					if (resultSet.next())
						return resultSet.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		return -1;
	}
	
	public static UserDetail userLogin(String userIdentifier, char[] password) {
		if (!DatabaseConnector.checkConnection("Unable to login user."))
			return null;
		
		UserDetail user = null;
		
		String query = "SELECT * FROM USER WHERE EMAIL = ? OR PHONE = ?";
		try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(query)) {
			ps.setString(1, userIdentifier);
			ps.setString(2, userIdentifier);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				// verify password
				String hashedPassword = resultSet.getString("hashed_password");
				try {
					if (!PasswordHasher.verifyPassword(password, hashedPassword)) 
						return null;
					
					user = new UserDetail();
					user.userID = resultSet.getInt("user_id");
					user.firstName = resultSet.getString("first_name");
					user.lastName = resultSet.getString("last_name");
					user.email = resultSet.getString("email");
					user.phone = resultSet.getString("phone");

					return user;
					
				} 
				catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
}