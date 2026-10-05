package Struct;

public class ForRentPowerBank {
	public PowerBank deviceInfo;
	
	// For rent attributes
	public String address;
	public int lockerNumber, maxDuration;
	public double lateFeePerDay, pricePerDay;
	
	public ForRentPowerBank() {
		deviceInfo = new PowerBank();
	}
}