package Database;

import java.util.ArrayList;
import java.util.List;

import Struct.PowerBank;
import Utils.Utils;

public class InsertSample {

	public static void insertPowerBank() {
		// Data from https://www.remaxthailand.co.th/collections/power_bank
		/*
		DatabaseConnector.insertIntoPowerbank("REMAX", "10000mAh Black", "RPP-37", 10000, 37, 6.8, 14.1, 1.8, 0.238,
				"/power_banks/remax_rpp_37_max.jpg", "jpg", new String[] { "USB-C", "USB Cable" },
				new String[] { "USB-C cable", "iPh Cable", "USB", "USB-C", "Gross" });
		DatabaseConnector.insertIntoPowerbank("REMAX", "10000mAh Gray", "WP-117", 10000, 38.5, 8.2, 4.7, 3.6, 0.173,
				"/power_banks/remax_wp-117_gray.jpg", "jpg", new String[] { "USB-C" }, new String[] { "USB-C" });
		DatabaseConnector.insertIntoPowerbank("REMAX", "20000mAh Gray", "CP-17", 20000, 77, 14.2, 7.2, 3.3, 0.433,
				"/power_banks/remax_cp-17_gray.jpg", "jpg", new String[] { "AC", "USB-C", "USB-C cable" },
				new String[] { "USB", "USB-C", "USB-C cable" });

		// https://www.mi.com/th/product/xiaomi-power-bank-10000mah-integrated-cable/specs/
		DatabaseConnector.insertIntoPowerbank("Xiaomi", "10000 MAH (Integrated Cable)", "P15ZM", 10000, 37, 6.52, 10.52,
				2.69, 0.35, "/power_banks/xiaomi_p15zm.jpg", "jpg", new String[] { "USB-C", "USB-C cable" },
				new String[] { "USB-A", "USB-C", "USB-C cable" });

		// https://www.jib.co.th/web/product/readProduct/83170/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--ANKER-ZOLO-POWERBANK---20000MAH-22-5W-FAST-CHARGING-WITH-BUILT-IN-USB-C-CABLE-WHITE-A110EH21
		DatabaseConnector.insertIntoPowerbank("Anker", "ZOLO White", "A110EH21", 20000, 74, 7.17, 14.07, 2.77, 0.392,
				"/power_banks/anker_a110eh21_white.jpg", "jpg", new String[] { "USB-C" }, new String[] { "USB-C" });

		// https://www.jib.co.th/web/product/readProduct/58517/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--AUKEY-POWERBANK-BASIX-MINI-10000mAh--PPS--FCP--SCP--22-5W--PB-N83S--BLACK
		DatabaseConnector.insertIntoPowerbank("Aukey", "Black Basix Mini", "PB-N83S", 10000, 37, 5.77, 8.05, 2.7, 0.174,
				"/power_banks/aukey_pb-n83s_black.jpg", "jpg", new String[] { "USB-C" }, new String[] { "USB-C", "USB-A" });

		// https://www.jib.co.th/web/product/readProduct/60993/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--MOFIT-POWERBANK-M11PD-10000mAh-20W-WHITE
		DatabaseConnector.insertIntoPowerbank("Mofit", "Mofit Power Bank", "M11PD", 10000, 37, 7.1, 1.41, 1.6, 0.220,
				"/power_banks/mofit_m11pd.jpg", "jpg", new String[] { "USB-C", "Micro USB" }, new String[] { "USB-C" });
		
		// https://www.jib.co.th/web/product/readProduct/81663/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--ANKER-NANO-POWER-BANK---10K-45W-BUILT-IN-RETRACTABLE-USB-C-CABLE-BLACK-A1638H11
		DatabaseConnector.insertIntoPowerbank("Anker", "NANO POWER BANK", "A1638H11", 10000, 37, 5.05, 8.15, 3.61, 0.231,
				"/power_banks/anker_a1638h11.jpg", "jpg", new String[] { "USB-C" }, new String[] { "USB-C", "USB-A" });
		
		// https://www.jib.co.th/web/product/readProduct/37578/POWER-BANK--%E0%B9%81%E0%B8%9A%E0%B8%95%E0%B9%80%E0%B8%95%E0%B8%AD%E0%B8%A3%E0%B8%B5%E0%B9%88%E0%B8%AA%E0%B8%B3%E0%B8%A3%E0%B8%AD%E0%B8%87---Xiaomi--XMI-VXN4273GL-10000mAh-18W-Fast-Charge--Silver-
		DatabaseConnector.insertIntoPowerbank("Xiaomi", "XIAOMI XMI", "VXN4273GL", 10000, 37, 7.12, 1.47, 1.42, 0.47,
				"/power_banks/xiaomi_vxn4273gl.jpg", "jpg", new String[] { "USB-C", "Micro USB" }, new String[] { "USB-A" });
		
		// https://www.jib.co.th/web/product/readProduct/74874/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--UVOLT-UVP10C-05-35W-10000mAh---SILVER
		DatabaseConnector.insertIntoPowerbank("UVOLT", "POWER BANK UVOLT", "UVP10C-05", 10000, 38.5, 7, 7.9, 2.7, 0.178,
				"/power_banks/uvolt_uvp10c-05.jpg", "jpg", new String[] { "USB-C", "Micro USB" }, new String[] { "USB-A" });
		
		// https://www.jib.co.th/web/product/readProduct/85372/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--WHY-PB-113E-PICO---10000mAh-PD-22-5W-BROWN
		DatabaseConnector.insertIntoPowerbank("Why Pico", "POWER BANK WHY PICO", "PB-113E", 10000, 37, 6, 7.4, 3, 0.184,
				"/power_banks/why_pico_pb-113e.jpg", "jpg", new String[] { "USB-C" }, new String[] { "USB-C" });
		
		// https://www.jib.co.th/web/product/readProduct/87412/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--ANKER-A1664-MAGGO---10000mAh-WIRELESS-BLACK-A1664W11
		DatabaseConnector.insertIntoPowerbank("Anker", "MAGGO", "A1664", 10000, 37, 7.06, 10.4, 1.47, 0.2159,
				"/power_banks/anker_a1664.jpg", "jpg", new String[] { "USB-C" }, new String[] { "USB-C" });
		*/
		
		// https://www.jib.co.th/web/product/readProduct/84004/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--D-POWER-GC1002---10000-mAh-BLUE
		PowerBank powerBank = new PowerBank();
		powerBank.brand = "d-power";
		powerBank.name = "Power Bank D-POWER";
		powerBank.model = "GC1002";
		powerBank.capacity = 10000;
		powerBank.wh = 38.5;
		powerBank.width = 5.5;
		powerBank.length = 7.3;
		powerBank.height = 2.75;
		powerBank.weight = 0.25;
		powerBank.image = Utils.createBufferedImage("/power_banks/d-power_gc1002.jpg");
		powerBank.imageFormat = "jpg";
		powerBank.input = new ArrayList<String>(List.of("USB-C"));
		powerBank.output = new ArrayList<String>(List.of("USB-C", "USB-A"));
		DatabaseConnector.insertIntoPowerbank(powerBank);
		
		// https://www.jib.co.th/web/product/readProduct/80178/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--ANITECH-X-SNOOPY-PB10-B-10000mAh---WHITE
		powerBank.brand = "ANITECH";
		powerBank.name = "ANITECH X SNOOPY";
		powerBank.model = "PB10-B";
		powerBank.capacity = 10000;
		powerBank.wh = 37;
		powerBank.width = 6.94;
		powerBank.length = 10.76;
		powerBank.height = 1.62;
		powerBank.weight = 0.337;
		powerBank.image = Utils.createBufferedImage("/power_banks/anitech_pb10-b.jpg");
		powerBank.imageFormat = "jpg";
		powerBank.input = new ArrayList<String>(List.of("USB-C"));
		powerBank.output = new ArrayList<String>(List.of("USB-C", "USB-A"));
		DatabaseConnector.insertIntoPowerbank(powerBank);
		
		// https://www.jib.co.th/web/product/readProduct/81923/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--PISEN-TS-D373-POWERPIX---30W-MINI-10000-mAh-WHITE
		powerBank.brand = "Pisen";
		powerBank.name = "POWERPIX 30W MINI";
		powerBank.model = "TS-D373";
		powerBank.capacity = 10000;
		powerBank.wh = 37;
		powerBank.width = 6.5;
		powerBank.length = 8.5;
		powerBank.height = 2.95;
		powerBank.weight = 0.21;
		powerBank.image = Utils.createBufferedImage("/power_banks/pisen_ts-d373.jpg");
		powerBank.imageFormat = "jpg";
		powerBank.input = new ArrayList<String>(List.of("USB-C"));
		powerBank.output = new ArrayList<String>(List.of("USB-C"));
		DatabaseConnector.insertIntoPowerbank(powerBank);
		
		// https://www.jib.co.th/web/product/readProduct/84026/20/POWER-BANK--%E0%B8%9E%E0%B8%B2%E0%B8%A7%E0%B9%80%E0%B8%A7%E0%B8%AD%E0%B8%A3%E0%B9%8C%E0%B9%81%E0%B8%9A%E0%B8%87%E0%B8%84%E0%B9%8C--D-POWER-GC57-Q-MAX---10000-mAh-BLACK
		powerBank.brand = "D-Power";
		powerBank.name = "10000 mAh BLACK";
		powerBank.model = "GC 57 Q.max";
		powerBank.capacity = 10000;
		powerBank.wh = 37;
		powerBank.width = 7.0;
		powerBank.length = 14.7;
		powerBank.height = 1.6;
		powerBank.weight = 0.17;
		powerBank.image = Utils.createBufferedImage("/power_banks/d-power_gc-57-qmax.jpg");
		powerBank.imageFormat = "jpg";
		powerBank.input = new ArrayList<String>(List.of("USB-C"));
		powerBank.output = new ArrayList<String>(List.of("USB-A"));
		DatabaseConnector.insertIntoPowerbank(powerBank);
	}

}
