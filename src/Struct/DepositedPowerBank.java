package Struct;

import java.time.LocalDate;

public class DepositedPowerBank {
	
	public PowerBank deviceInfo;
	public int depositId;
	public String depositStatus, address;
	public LocalDate depositDate, returnDate;
	public int lockerNumber;
	public double lateFeePerDay, depositPrice;
	public String lockerPassword;
	public int paymentId;
	
	public DepositedPowerBank() {
		deviceInfo = new PowerBank();
	}
	
	public DepositedPowerBank(PowerBank deviceInfo, String address, 
			LocalDate depositDate, LocalDate returnDate, int lockerNumber,
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
