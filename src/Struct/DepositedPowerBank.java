package Struct;

import java.util.Calendar;

public class DepositedPowerBank {
	
	public PowerBank deviceInfo;
	public int depositId;
	public String depositStatus, address;
	public Calendar depositDate, returnDate;
	public int lockerNumber;
	public double lateFeePerDay, depositPrice;
	public String lockerPassword;
	public int paymentId;
	
	public DepositedPowerBank() {
		deviceInfo = new PowerBank();
	}
	
	public DepositedPowerBank(PowerBank deviceInfo, String address, 
			Calendar depositDate, Calendar returnDate, int lockerNumber,
			double lateFeePerDay, double depositPrice) {
		
		this.deviceInfo = deviceInfo;
		this.address = address;
		this.depositDate = depositDate;
		this.returnDate = returnDate;
		this.lockerNumber = lockerNumber;
		this.lateFeePerDay = lateFeePerDay;
		this.depositPrice = depositPrice;
	}
}
