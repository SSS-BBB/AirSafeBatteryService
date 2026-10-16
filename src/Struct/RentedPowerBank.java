package Struct;

import java.time.LocalDate;

public class RentedPowerBank {
	public PowerBank deviceInfo;
	
	public int rentID;
	public String pickUpAddress, returnAddress;
	public LocalDate rentDate, returnDate;
	public String status;
	public double lateFeePerDay, rentPrice;
	public int lockerNumber;
	public String lockerPassword;
	
	public RentedPowerBank() {
		deviceInfo = new PowerBank();
	}
	
	public RentedPowerBank(ForRentPowerBank forRent, String returnAddress, 
			LocalDate rentDate, LocalDate returnDate,
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