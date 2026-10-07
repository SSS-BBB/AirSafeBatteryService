package Struct;

import java.util.Calendar;

public class RentedPowerBank {
	public PowerBank deviceInfo;
	
	public int rentID;
	public String pickUpAddress, returnAddress;
	public Calendar rentDate, returnDate;
	public String status;
	public double lateFeePerDay, rentPrice;
	public int lockerNumber;
	
	public RentedPowerBank() {
		deviceInfo = new PowerBank();
	}
	
	public RentedPowerBank(ForRentPowerBank forRent, String returnAddress, 
			Calendar rentDate, Calendar returnDate,
			double rentPrice) {
		
		// Take some attributes from For Rent Power bank
		pickUpAddress = forRent.address;
		lockerNumber = forRent.lockerNumber;
		lateFeePerDay = forRent.lateFeePerDay;
		this.deviceInfo = forRent.deviceInfo;
		
		// Exclusive RentedPowerBank attributes
		this.returnAddress = returnAddress;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
		this.rentPrice = rentPrice;
	}
}