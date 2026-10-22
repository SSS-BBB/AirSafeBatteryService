package Database;

import java.util.ArrayList;

import Struct.CheckHistory;
import Struct.DepositedPowerBank;
import Struct.ForRentPowerBank;
import Struct.RentedPowerBank;

public class DataContainer {
	
	private static ArrayList<RentedPowerBank> inProgressRentList, allRentList, rentPastDueList;
	private static ArrayList<DepositedPowerBank> inProgressDepositList, allDepositList, depositPastDueList;
	private static ArrayList<CheckHistory> checkHistoryList;
	private static ArrayList<ForRentPowerBank> forRentList;

	// Refresh Data
	// Rent
	public static void refreshInprogressRentList(int userId) {
		inProgressRentList = DatabaseConnector.getRentedPowerBank(userId, false, true);
	}
	
	public static void refreshAllRentList(int userId) {
		allRentList = DatabaseConnector.getRentedPowerBank(userId, false, false);
	}
	
	public static void refreshRentPastDueList(int userId) {
		rentPastDueList = DatabaseConnector.getRentedPowerBank(userId, true, false);
	}
	
	public static void refreshEveryRentList(int userId) {
		refreshInprogressRentList(userId);
		refreshAllRentList(userId);
		refreshRentPastDueList(userId);
	}
	
	// Deposit
	public static void refreshInprogressDepositList(int userId) {
		inProgressDepositList = DatabaseConnector.getDepositedPowerBank(userId, false, true);
	}
	
	public static void refreshAllDepositList(int userId) {
		allDepositList = DatabaseConnector.getDepositedPowerBank(userId, false, false);
	}
	
	public static void refreshDepositPastDueList(int userId) {
		depositPastDueList = DatabaseConnector.getDepositedPowerBank(userId, true, false);
	}
	
	public static void refreshEveryDepositList(int userId) {
		refreshInprogressDepositList(userId);
		refreshAllDepositList(userId);
		refreshDepositPastDueList(userId);
	}
	
	// Check History
	public static void refreshCheckHistoryList(int userId) {
		checkHistoryList = DatabaseConnector.getCheckHistory(userId);
	}
	
	// For Rent
	public static void refreshForRentList() {
		forRentList = DatabaseConnector.getForRentPowerBank();
	}
	
	public static void refreshLateStatus() {
		DatabaseConnector.updateLateStatus();
	}
	
	public static void refreshAll(int userId) {
		refreshLateStatus();
		
		refreshInprogressRentList(userId);
		refreshAllRentList(userId);
		refreshRentPastDueList(userId);
		
		refreshInprogressDepositList(userId);
		refreshAllDepositList(userId);
		refreshDepositPastDueList(userId);
		
		refreshCheckHistoryList(userId);
		
		refreshForRentList();
	}
	
	// Get Deposit List
	public static ArrayList<DepositedPowerBank> getDepositList(boolean showPastDue, boolean showOnlyInprogress) {
		if (showPastDue) return depositPastDueList;
		else if (showOnlyInprogress) return inProgressDepositList;
		return allDepositList;
	}
	
	public static ArrayList<DepositedPowerBank> getInProgressDepositList() {
		return inProgressDepositList;
	}

	public static ArrayList<DepositedPowerBank> getAllDepositList() {
		return allDepositList;
	}
	
	public static ArrayList<DepositedPowerBank> getDepositPastDueList() {
		return depositPastDueList;
	}

	public static ArrayList<RentedPowerBank> getInProgressRentList() {
		return inProgressRentList;
	}
	
	// Get Rent List
	public static ArrayList<RentedPowerBank> getRentList(boolean showPastDue, boolean showOnlyInprogress) {
		if (showPastDue) return rentPastDueList;
		else if (showOnlyInprogress) return inProgressRentList;
		return allRentList;
	}
	
	public static ArrayList<RentedPowerBank> getAllRentList() {
		return allRentList;
	}
	
	public static ArrayList<RentedPowerBank> getRentPastDueList() {
		return rentPastDueList;
	}
	
	// Get Check History List
	public static ArrayList<CheckHistory> getCheckHistoryList() {
		return checkHistoryList;
	}
	
	// Get For Rent List
	public static ArrayList<ForRentPowerBank> getForRentList() {
		return forRentList;
	}
}
