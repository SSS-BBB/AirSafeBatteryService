package Struct;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PowerBank {
	// Power bank attributes
	public int powerBankId;
	public String brand, name, model;
	public double capacity, wh, width, length, height, weight;
	public BufferedImage image;
	public String imageFormat;
	public ArrayList<String> input, output;
	
	@Override
	public String toString() {
		String text = "Brand: " + brand + "\n" +
			   "Name: " + name + "\n" +
			   "Model: " + model + "\n" +
			   "Capacity: " + String.valueOf(capacity) + "\n" +
			   "Wh: " + String.valueOf(wh) + "\n" +
			   "Weight: " + String.valueOf(weight) + "\n" +
			   "Width: " + String.valueOf(width) + "\n" +
			   "Length: " + String.valueOf(length) + "\n" +
			   "Height: " + String.valueOf(height) + "\n" +
			   "Image Format: " + String.valueOf(imageFormat) + "\n";
		
		text += "Input: (";
		for (int i = 0; i < input.size(); i++) {
			if (i == input.size() - 1) {
				text += input.get(i);
			}
			else {
				text += input.get(i) + ", ";
			}
			
		}
		text += ")" + "\n";
		
		text += "Output: (";
		for (int i = 0; i < output.size(); i++) {
			if (i == output.size() - 1) {
				text += output.get(i);
			}
			else {
				text += output.get(i) + ", ";
			}
			
		}
		text += ")";
		
		return text;
	}
}
