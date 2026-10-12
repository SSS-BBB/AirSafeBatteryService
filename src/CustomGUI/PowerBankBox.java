package CustomGUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Struct.PowerBank;
import Utils.Utils;

public class PowerBankBox extends JPanel {
	
	private Color backgroundColor;
	
	private int headingFontSize, bodyFontSize, margin;
	
	// Box Components
	private JLabel brandLabel;
	private JLabel nameLabel;
	private JLabel modelLabel;
	private JLabel whAndCapacityLabel;
	private JLabel sizeAndWeightLabel;
	private JLabel inputLabel;
	private JLabel outputLabel;
	private JLabel powerBankImageLabel;
	
	public PowerBankBox(Color backgroundColor, int headingFontSize, int bodyFontSize, int margin) {
		this.backgroundColor = backgroundColor;
		this.headingFontSize = headingFontSize;
		this.bodyFontSize = bodyFontSize;
		this.margin = margin;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(backgroundColor);
		
		createPowerbankBox();
	}
	
	public void updatePowerbankBox(PowerBank powerBank) {
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to update power bank box.");
			return;
		}
		
		ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(powerBank.image, 200, 200);
		powerBankImageLabel.setIcon(scaledPowerBankImage);
		
		brandLabel.setText(powerBank.brand);
		nameLabel.setText(powerBank.name);
		modelLabel.setText(powerBank.model);
		
		String whAndCapacity = String.valueOf(powerBank.wh) + " Wh / " + String.valueOf(powerBank.capacity) + " mAh";
		whAndCapacityLabel.setText(whAndCapacity);
		
		String sizeAndWeight = String.valueOf(powerBank.width) + "x" + String.valueOf(powerBank.length) + "x"
				+ String.valueOf(powerBank.height) + " cm" + " " + String.valueOf(powerBank.weight) + " kg";
		sizeAndWeightLabel.setText(sizeAndWeight);
		
		String inputText = Utils.insertArrayListToString(powerBank.input, "Input: ", ", ");
		inputLabel.setText(inputText);
		
		String outputText = Utils.insertArrayListToString(powerBank.output, "Output: ", ", ");
		outputLabel.setText(outputText);
	}
	
	private void createPowerbankBox() {
		this.add(Box.createVerticalGlue());

		float componentAlignment = CENTER_ALIGNMENT;

		
		powerBankImageLabel = new JLabel();
		powerBankImageLabel.setAlignmentX(componentAlignment);
		powerBankImageLabel.setAlignmentY(componentAlignment);
		this.add(powerBankImageLabel);

		brandLabel = new JLabel();
		brandLabel.setFont(new Font("Tahoma", Font.BOLD, headingFontSize));
		brandLabel.setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));
		brandLabel.setAlignmentX(componentAlignment);
		brandLabel.setAlignmentY(componentAlignment);
		this.add(brandLabel);

		nameLabel = new JLabel();
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, headingFontSize));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		nameLabel.setAlignmentX(componentAlignment);
		nameLabel.setAlignmentY(componentAlignment);
		this.add(nameLabel);

		modelLabel = new JLabel();
		modelLabel.setFont(new Font("Tahoma", Font.BOLD, headingFontSize));
		modelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		modelLabel.setAlignmentX(componentAlignment);
		modelLabel.setAlignmentY(componentAlignment);
		this.add(modelLabel);

		
		whAndCapacityLabel = new JLabel();
		whAndCapacityLabel.setFont(new Font("Tahoma", Font.PLAIN, bodyFontSize));
		whAndCapacityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		whAndCapacityLabel.setAlignmentX(componentAlignment);
		whAndCapacityLabel.setAlignmentY(componentAlignment);
		whAndCapacityLabel.setForeground(Color.GRAY);
		this.add(whAndCapacityLabel);

		sizeAndWeightLabel = new JLabel();
		sizeAndWeightLabel.setFont(new Font("Tahoma", Font.PLAIN, bodyFontSize));
		sizeAndWeightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		sizeAndWeightLabel.setAlignmentX(componentAlignment);
		sizeAndWeightLabel.setAlignmentY(componentAlignment);
		sizeAndWeightLabel.setForeground(Color.GRAY);
		this.add(sizeAndWeightLabel);
		
		inputLabel = new JLabel();
		inputLabel.setFont(new Font("Tahoma", Font.PLAIN, bodyFontSize));
		inputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		inputLabel.setAlignmentX(componentAlignment);
		inputLabel.setAlignmentY(componentAlignment);
		inputLabel.setForeground(Color.BLACK);
		this.add(inputLabel);
		
		outputLabel = new JLabel();
		outputLabel.setFont(new Font("Tahoma", Font.PLAIN, bodyFontSize));
		outputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		outputLabel.setAlignmentX(componentAlignment);
		outputLabel.setAlignmentY(componentAlignment);
		outputLabel.setForeground(Color.BLACK);
		this.add(outputLabel);

		this.add(Box.createVerticalGlue());
	}
}
