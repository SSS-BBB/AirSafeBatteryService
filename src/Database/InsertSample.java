package Database;

public class InsertSample {
	
	public static void insertPowerBank() {
		// Data from https://www.bnn.in.th/th/p/smartphone-and-accessories/smartphone-accessories/smartphone-powerbank
		
		JDBCConnector.insertIntoPowerbank("Blue Box", "10000 mAh built-in Lightning/Type-C PD22.5W Cream (CCC)", "EP-I109", 10000.00, 37, 2.8, 6.4, 9.4, 0.21, "/power_banks/blue_box_ep-I109.png");
		JDBCConnector.insertIntoPowerbank("Blue Box", "20000 mAh built-in Lightning/Type-C cable PD22.5W Cream", "EPI209", 20000, 77, 3.2, 7.3, 11.7, 0.36, "/power_banks/blue_box_epi209.png");
		JDBCConnector.insertIntoPowerbank("QPLUS", "15000 mAh LED Display with 2-in-1 Cable White", "W1501C", 15000, 55.5, 8.3, 8.3, 3.3, 0.31, "/power_banks/qplus_w1501c.png");
		JDBCConnector.insertIntoPowerbank("ALPHA", "20,000 mAh White", "B20PD", 20000, 74, 8.3, 15.64, 1.73, 0.33, "/power_banks/alpha_b20pd.png");
		JDBCConnector.insertIntoPowerbank("Ugreen", "20000 mAh 130W Two-way Fast Charing Black", "35524B", 20000, 74, 6.81, 10.58, 2.40, 0.39, "/power_banks/ugreen_35524B.png");
	}
	
}
