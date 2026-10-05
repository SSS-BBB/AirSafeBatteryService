package Database;

public class InsertSample {

	public static void insertPowerBank() {
		// Data from https://www.remaxthailand.co.th/collections/power_bank
		JDBCConnector.insertIntoPowerbank("REMAX", "10000mAh Black", "RPP-37", 10000, 37, 6.8, 14.1, 1.8, 0.238,
				"/power_banks/remax_rpp_37_max.jpg", new String[] { "USB-C", "USB Cable" },
				new String[] { "USB-C cable", "iPh Cable", "USB", "USB-C", "Gross" });
		JDBCConnector.insertIntoPowerbank("REMAX", "10000mAh Gray", "WP-117", 10000, 38.5, 8.2, 4.7, 3.6, 0.173,
				"/power_banks/remax_wp-117_gray.jpg", new String[] { "USB-C" }, new String[] { "USB-C" });
		JDBCConnector.insertIntoPowerbank("REMAX", "20000mAh Gray", "CP-17", 20000, 77, 14.2, 7.2, 3.3, 0.433,
				"/power_banks/remax_cp-17_gray.jpg", new String[] { "AC", "USB-C", "USB-C cable" },
				new String[] { "USB", "USB-C", "USB-C cable" });

		// https://www.anker.com/eu-en/products/a1263
		// https://manuals.plus/th/asin/B0194WDVHI
		JDBCConnector.insertIntoPowerbank("Anker", "PowerCore 10000", "A1263", 10000, 36, 6, 9.2, 2.2, 0.180,
				"/power_banks/anker_a1263.jpg", new String[] { "Micro USB" }, new String[] { "USB-A" });

		// https://www.mi.com/th/product/xiaomi-power-bank-10000mah-integrated-cable/specs/
		JDBCConnector.insertIntoPowerbank("Xiaomi", "10000 MAH (Integrated Cable)", "P15ZM", 10000, 37, 6.52, 10.52,
				2.69, 0.35, "/power_banks/xiaomi_p15zm.jpg", new String[] { "USB-C", "USB-C cable" },
				new String[] { "USB-A", "USB-C", "USB-C cable" });

		// https://www.jib.co.th/web/product/readProduct/83170/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--ANKER-ZOLO-POWERBANK---20000MAH-22-5W-FAST-CHARGING-WITH-BUILT-IN-USB-C-CABLE-WHITE-A110EH21
		JDBCConnector.insertIntoPowerbank("Anker", "ZOLO White", "A110EH21", 20000, 74, 7.17, 14.07, 2.77, 0.392,
				"/power_banks/anker_a110eh21_white.jpg", new String[] { "USB-C" }, new String[] { "USB-C" });

		// https://www.jib.co.th/web/product/readProduct/58517/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--AUKEY-POWERBANK-BASIX-MINI-10000mAh--PPS--FCP--SCP--22-5W--PB-N83S--BLACK
		JDBCConnector.insertIntoPowerbank("Aukey", "Black Basix Mini", "PB-N83S", 10000, 37, 5.77, 8.05, 2.7, 0.174,
				"/power_banks/aukey_pb-n83s_black.jpg", new String[] { "USB-C" }, new String[] { "USB-C", "USB-A" });

		// https://www.jib.co.th/web/product/readProduct/60993/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--MOFIT-POWERBANK-M11PD-10000mAh-20W-WHITE
		JDBCConnector.insertIntoPowerbank("Mofit", "Mofit Power Bank", "M11PD", 10000, 37, 7.1, 1.41, 1.6, 0.220,
				"/power_banks/mofit_m11pd.jpg", new String[] { "USB-C", "Micro USB" }, new String[] { "USB-C" });
		
		// https://www.jib.co.th/web/product/readProduct/81663/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--ANKER-NANO-POWER-BANK---10K-45W-BUILT-IN-RETRACTABLE-USB-C-CABLE-BLACK-A1638H11
		JDBCConnector.insertIntoPowerbank("Anker", "NANO POWER BANK", "A1638H11", 10000, 37, 5.05, 8.15, 3.61, 0.231,
				"/power_banks/anker_a1638h11.jpg", new String[] { "USB-C" }, new String[] { "USB-C", "USB-A" });

	}

}
