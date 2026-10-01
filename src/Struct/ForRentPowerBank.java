package Struct;

import java.awt.image.BufferedImage;

public class ForRentPowerBank {
	// Power bank attributes
	public String brand, name, model;
	public double capacity, wh, width, length, height, weight;
	public BufferedImage image;
	
	// For rent attributes
	public String address;
	public int lockerNumber, maxDuration;
	public double lateFeePerDay, pricePerDay;
}