package Database;

public class InsertSample {

	public static void insertPowerBank() {
		// Data from
		// https://www.bnn.in.th/th/p/smartphone-and-accessories/smartphone-accessories/smartphone-powerbank
		/*
		 * JDBCConnector.insertIntoPowerbank("Blue Box",
		 * "10000 mAh built-in Lightning/Type-C PD22.5W Cream (CCC)", "EP-I109",
		 * 10000.00, 37, 2.8, 6.4, 9.4, 0.21, "/power_banks/blue_box_ep-I109.png");
		 * JDBCConnector.insertIntoPowerbank("Blue Box",
		 * "20000 mAh built-in Lightning/Type-C cable PD22.5W Cream", "EPI209", 20000,
		 * 77, 3.2, 7.3, 11.7, 0.36, "/power_banks/blue_box_epi209.png");
		 * JDBCConnector.insertIntoPowerbank("QPLUS",
		 * "15000 mAh LED Display with 2-in-1 Cable White", "W1501C", 15000, 55.5, 8.3,
		 * 8.3, 3.3, 0.31, "/power_banks/qplus_w1501c.png");
		 * JDBCConnector.insertIntoPowerbank("ALPHA", "20,000 mAh White", "B20PD",
		 * 20000, 74, 8.3, 15.64, 1.73, 0.33, "/power_banks/alpha_b20pd.png");
		 * JDBCConnector.insertIntoPowerbank("Ugreen",
		 * "20000 mAh 130W Two-way Fast Charing Black", "35524B", 20000, 74, 6.81,
		 * 10.58, 2.40, 0.39, "/power_banks/ugreen_35524B.png");
		 */

		// Data from https://www.remaxthailand.co.th/collections/power_bank
		JDBCConnector.insertIntoPowerbank("REMAX", "10000mAh Black", "RPP-37", 10000, 37, 6.8, 14.1, 1.8, 0.238,
				"/power_banks/remax_rpp_37_max.jpg", new String[] { "Type-C", "USB Cable" },
				new String[] { "Type-C Cable", "iPh Cable", "USB", "Type-C", "Gross" });
		JDBCConnector.insertIntoPowerbank("REMAX", "10000mAh Gray", "WP-117", 10000, 38.5, 8.2, 4.7, 3.6, 0.173,
				"/power_banks/remax_wp-117_gray.jpg", new String[] { "Type-C" }, new String[] { "Type-C" });
		JDBCConnector.insertIntoPowerbank("REMAX", "20000mAh Gray", "CP-17", 20000, 77, 14.2, 7.2, 3.3, 0.433,
				"/power_banks/remax_cp-17_gray.jpg", new String[] { "AC", "Type-C", "Type-C Cable" },
				new String[] { "USB", "Type-C", "Type-C Cable" });
		
		/*
		JDBCConnector.insertIntoPowerbank("", "", "", 0, 0, 0, 0, 0, 0, "/power_banks/", new String[] {},
				new String[] {});
		JDBCConnector.insertIntoPowerbank("", "", "", 0, 0, 0, 0, 0, 0, "/power_banks/", new String[] {},
				new String[] {});
				*/
	}

}
