package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.CustomScrollPane;
import Database.DatabaseConnector;
import Struct.ForRentPowerBank;
import Struct.RentedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserRentPayment extends JPanel {
	
	public RentedPowerBank powerBank;
	
	private UserMainFrame mainFrame;
	private Color backgroundColor, mainColor, menuColor;
	
	// Power bank box
	private JLabel brandLabel;
	private JLabel nameLabel;
	private JLabel modelLabel;
	private JLabel whAndCapacityLabel;
	private JLabel sizeAndWeightLabel;
	private JLabel inputLabel;
	private JLabel outputLabel;
	private JLabel rentPriceLabel;
	private JLabel powerBankImageLabel;
	
	// Detail box
	private JLabel[] placeHolderLabels;
	
	public UserRentPayment(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());
		
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.mainColor = mainColor;
		this.menuColor = menuColor;
		
		this.placeHolderLabels = new JLabel[7];
		/* 
		 * 0 -> renter name
		 * 1 -> rent date
		 * 2 -> return date
		 * 3 -> pick up address
		 * 4 -> return address
		 * 5 -> payment amount   
		 * 6 -> total price display
		*/
		
		setBackground(backgroundColor);
		
		// Title
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titlePanel.setBackground(backgroundColor);
		this.add(titlePanel, BorderLayout.NORTH);
		
		JLabel titleLabel = new JLabel("ชำระเงิน");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBackground(backgroundColor);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		titlePanel.add(titleLabel);
		
		JPanel summaryBox = Utils.createBoxWithLabelInside("สรุปรายการสั่งซื้อ", mainColor, Color.WHITE, 18);
		summaryBox.setAlignmentX(LEFT_ALIGNMENT);
		titlePanel.add(summaryBox);
		
		// Content
		JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		contentPanel.setBackground(backgroundColor);
		
		CustomScrollPane contentScrollPane = new CustomScrollPane(contentPanel, menuColor, backgroundColor);
		this.add(contentScrollPane, BorderLayout.CENTER);
		
		JPanel powerBankBox = createPowerBankBoxPanel();
		powerBankBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		contentPanel.add(powerBankBox);
		
		JPanel detailBox = createPaymentDetail();
		detailBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 75));
		contentPanel.add(detailBox);
		
		JPanel paymentBox = createUserPayPanel();
		paymentBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		contentPanel.add(paymentBox);
	}
	
	// Initialize with power bank
	public UserRentPayment(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor, ForRentPowerBank powerBank) {
		this(mainFrame, backgroundColor, mainColor, menuColor);
		
		Calendar pickUpDate = Calendar.getInstance();
		Calendar returnDate = Calendar.getInstance();
		returnDate.add(Calendar.DATE, 10);
		
		this.powerBank = new RentedPowerBank(powerBank, "สนามบินสุวรรณภูมิ", pickUpDate, returnDate, 1000.0);
		updateUserRentPaymentPage();
	}
	
	public void updateUserRentPaymentPage() {
		updatePowerBankBox();
		updatePaymentDetail();
	}
	
	private JPanel createUserPayPanel() {
		JPanel userPayPanel = new JPanel();
		userPayPanel.setLayout(new BoxLayout(userPayPanel, BoxLayout.Y_AXIS));
		userPayPanel.setBackground(null);
		
		int fontSize = 16;
		int fontAdd = 4;
		int margin = 8;
		
		// Scan To Pay
		JLabel scanToPayLabel = new JLabel("แสกนเพื่อชำระเงิน");
		scanToPayLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		scanToPayLabel.setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(scanToPayLabel);
		userPayPanel.add(Box.createVerticalStrut(margin));
		
		// QR Code
		ImageIcon qrIcon = Utils.createImageIcon("/qr_code.png", "Fake QR Code");
		JLabel qrIconLabel = new JLabel(qrIcon);
		qrIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(qrIconLabel);
		userPayPanel.add(Box.createVerticalStrut(margin));
		
		// Total Price
		JLabel totalPriceLabel = new JLabel("ยอดชำระทั้งหมด");
		totalPriceLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		totalPriceLabel.setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(totalPriceLabel);
		userPayPanel.add(Box.createVerticalStrut(4));
		
		// Price Display
		JPanel priceDisplayPanel = createBorderBoxWithLabelInside(6, Color.BLACK, fontSize, 2, new Dimension(200, 30));
		priceDisplayPanel.setAlignmentX(CENTER_ALIGNMENT);
		placeHolderLabels[6].setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(priceDisplayPanel);
		userPayPanel.add(Box.createVerticalStrut(margin));
		
		// Payment Time Limit
		JLabel timeLimitLabel = new JLabel("กรุณาชำระภายใน 15 นาที");
		timeLimitLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		timeLimitLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
		timeLimitLabel.setForeground(new Color(0, 100, 0));
		timeLimitLabel.setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(timeLimitLabel);
		userPayPanel.add(Box.createVerticalStrut(margin));
		
		// Payment Confirm
		Dimension buttonSize = new Dimension(180, 40);
		JButton paymentConfirmButton = Utils.createColorBackgroundButton("ยืนยันการชำระเงิน", menuColor, Color.WHITE, fontSize);
		paymentConfirmButton.setAlignmentX(CENTER_ALIGNMENT);
		paymentConfirmButton.setPreferredSize(buttonSize);
		paymentConfirmButton.setMaximumSize(buttonSize);
		userPayPanel.add(paymentConfirmButton);
		
		// TODO: add data to transaction and rented power bank when payment confirm button is clicked.
		// also change to successful payment page
		
		paymentConfirmButton.addActionListener(e -> {
			// Transaction Process
			boolean successfulTransaction = DatabaseConnector.insertIntoTransaction(powerBank.rentPrice, mainFrame.userDetail.userID);
			
			if (!successfulTransaction) {
				System.err.println("Something went wrong when trying to insert data into transaction table. Unable to proceed the rent.");
				return;
			}
			
			// Add data to rented power bank
			
		});
		
		return userPayPanel;
	}
	
	private void updatePaymentDetail() {
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to update payment detail.");
			return;
		}
		
		if (mainFrame == null) {
			System.err.println("Null mainFrame. Unable to update payment detail.");
			return;
		}
		
		// update place holder labels
		/* 
		 * 0 -> renter name
		 * 1 -> rent date
		 * 2 -> return date
		 * 3 -> pick up address
		 * 4 -> return address
		 * 5 -> payment amount
		 * 6 -> total price display
		*/
		// Renter Name
		placeHolderLabels[0].setText(mainFrame.userDetail.firstName + " " + mainFrame.userDetail.lastName);
		
		// Rent Date
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String rentDate = dateFormatter.format(powerBank.rentDate.getTime());
		placeHolderLabels[1].setText(rentDate);
		
		// Return Date
		String returnDate = dateFormatter.format(powerBank.returnDate.getTime());
		placeHolderLabels[2].setText(returnDate);
		
		// Pick up address
		placeHolderLabels[3].setText(powerBank.pickUpAddress);
		
		// Return address
		placeHolderLabels[4].setText(powerBank.returnAddress);
		
		// Payment Amount
		placeHolderLabels[5].setText(String.valueOf(powerBank.rentPrice) + " บาท");
		
		// Total Price Display below qr code
		placeHolderLabels[6].setText(String.valueOf(powerBank.rentPrice) + " บาท");
	
	}
	
	private JPanel createPaymentDetail() {
		JPanel detailPanel = new JPanel();
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
		detailPanel.setBackground(backgroundColor);
		detailPanel.setAlignmentX(CENTER_ALIGNMENT);

		int margin = 8;
		int fontSize = 16;
		Dimension placeHolderSize = new Dimension(200, 30);
		
		// Renter Name
		JPanel renterNamePanel = createDetailItem(0, "ชื่อผู้เช่า:", Color.BLACK, fontSize, 2, placeHolderSize);
		renterNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(renterNamePanel);
		
		// Rent Date
		JPanel rentDatePanel = createDetailItem(1, "วันที่เช่า:", Color.BLACK, fontSize, 2, placeHolderSize);
		rentDatePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(rentDatePanel);
		
		// Return Date
		JPanel renturnDatePanel = createDetailItem(2, "วันที่คืน:", Color.BLACK, fontSize, 2, placeHolderSize);
		renturnDatePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(renturnDatePanel);
		
		// Pick up address
		JPanel pickUpAddressPanel = createDetailItem(3, "สถานที่รับ:", Color.BLACK, fontSize, 2, placeHolderSize);
		pickUpAddressPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(pickUpAddressPanel);
		
		// Return address
		JPanel returnAddressPanel = createDetailItem(4, "สถานที่คืน:", Color.BLACK, fontSize, 2, placeHolderSize);
		returnAddressPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(returnAddressPanel);
		
		// Payment Amount
		JPanel paymentAmountPanel = createDetailItem(5, "ยอดชำระ:", Color.BLACK, fontSize, 2, placeHolderSize);
		paymentAmountPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(paymentAmountPanel);
		
		// Back Button
		Dimension buttonSize = new Dimension(140, 40);
		JButton backButton = Utils.createColorBackgroundButton("กลับ", mainColor, Color.WHITE, fontSize);
		backButton.setAlignmentX(CENTER_ALIGNMENT);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		detailPanel.add(backButton);
		
		backButton.addActionListener(e -> {
			mainFrame.changeCard(2, 8);
		});
		
		detailPanel.add(Box.createVerticalStrut(160));
		
		return detailPanel;
	}
	
	private JPanel createDetailItem(int holderLabelIndex, String name, Color fontColor, int fontSize, int lineThickness, Dimension placeHolderSize) {
		JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		itemPanel.setBackground(null);

		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);
		itemPanel.add(nameLabel);
		
		JPanel renterNamePlaceHolder = createBorderBoxWithLabelInside(holderLabelIndex, Color.BLACK, fontSize, 2, placeHolderSize);
		itemPanel.add(renterNamePlaceHolder);
		
		return itemPanel;
	}
	
	private JPanel createBorderBoxWithLabelInside(int holderLabelIndex, Color fontColor, int fontSize, int lineThickness, Dimension placeHolderSize) {
		JPanel placeHolderPanel = new JPanel(new BorderLayout());
		placeHolderPanel.setBackground(null);
		placeHolderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		placeHolderPanel.setPreferredSize(placeHolderSize);
		
		placeHolderLabels[holderLabelIndex] = new JLabel();
		placeHolderLabels[holderLabelIndex].setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		placeHolderLabels[holderLabelIndex].setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		placeHolderLabels[holderLabelIndex].setAlignmentX(LEFT_ALIGNMENT);
		placeHolderLabels[holderLabelIndex].setAlignmentY(CENTER_ALIGNMENT);
		placeHolderPanel.add(placeHolderLabels[holderLabelIndex], BorderLayout.WEST);
		
		return placeHolderPanel;
	}
	
	private void updatePowerBankBox() {
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to update power bank box.");
			return;
		}
		
		ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(powerBank.deviceInfo.image, 200, 200);
		powerBankImageLabel.setIcon(scaledPowerBankImage);
		
		brandLabel.setText(powerBank.deviceInfo.brand);
		nameLabel.setText(powerBank.deviceInfo.name);
		modelLabel.setText(powerBank.deviceInfo.model);
		
		String whAndCapacity = String.valueOf(powerBank.deviceInfo.wh) + " Wh / " + String.valueOf(powerBank.deviceInfo.capacity) + " mAh";
		whAndCapacityLabel.setText(whAndCapacity);
		
		String sizeAndWeight = String.valueOf(powerBank.deviceInfo.width) + "x" + String.valueOf(powerBank.deviceInfo.length) + "x"
				+ String.valueOf(powerBank.deviceInfo.height) + " cm" + " " + String.valueOf(powerBank.deviceInfo.weight) + " kg";
		sizeAndWeightLabel.setText(sizeAndWeight);
		
		String inputText = Utils.insertArrayListToString(powerBank.deviceInfo.input, "Input: ", ", ");
		inputLabel.setText(inputText);
		
		String outputText = Utils.insertArrayListToString(powerBank.deviceInfo.output, "Output: ", ", ");
		outputLabel.setText(outputText);
	}
	
	private JPanel createPowerBankBoxPanel() {
		JPanel powerbankBoxPanel = new JPanel();
		powerbankBoxPanel.setLayout(new BoxLayout(powerbankBoxPanel, BoxLayout.Y_AXIS));
		powerbankBoxPanel.setBackground(backgroundColor);
		powerbankBoxPanel.add(Box.createVerticalGlue());

		int margin = 12;
		int fontSize = 16;
		int fontAdd = 2;
		float componentAlignment = CENTER_ALIGNMENT;

		
		powerBankImageLabel = new JLabel();
		powerBankImageLabel.setAlignmentX(componentAlignment);
		powerBankImageLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(powerBankImageLabel);

		brandLabel = new JLabel();
		brandLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		brandLabel.setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));
		brandLabel.setAlignmentX(componentAlignment);
		brandLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(brandLabel);

		nameLabel = new JLabel();
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		nameLabel.setAlignmentX(componentAlignment);
		nameLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(nameLabel);

		modelLabel = new JLabel();
		modelLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		modelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		modelLabel.setAlignmentX(componentAlignment);
		modelLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(modelLabel);

		
		whAndCapacityLabel = new JLabel();
		whAndCapacityLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		whAndCapacityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		whAndCapacityLabel.setAlignmentX(componentAlignment);
		whAndCapacityLabel.setAlignmentY(componentAlignment);
		whAndCapacityLabel.setForeground(Color.GRAY);
		powerbankBoxPanel.add(whAndCapacityLabel);

		sizeAndWeightLabel = new JLabel();
		sizeAndWeightLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		sizeAndWeightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		sizeAndWeightLabel.setAlignmentX(componentAlignment);
		sizeAndWeightLabel.setAlignmentY(componentAlignment);
		sizeAndWeightLabel.setForeground(Color.GRAY);
		powerbankBoxPanel.add(sizeAndWeightLabel);
		
		inputLabel = new JLabel();
		inputLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		inputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		inputLabel.setAlignmentX(componentAlignment);
		inputLabel.setAlignmentY(componentAlignment);
		inputLabel.setForeground(Color.BLACK);
		powerbankBoxPanel.add(inputLabel);
		
		outputLabel = new JLabel();
		outputLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		outputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		outputLabel.setAlignmentX(componentAlignment);
		outputLabel.setAlignmentY(componentAlignment);
		outputLabel.setForeground(Color.BLACK);
		powerbankBoxPanel.add(outputLabel);

		rentPriceLabel = new JLabel();
		rentPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		rentPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		rentPriceLabel.setAlignmentX(componentAlignment);
		rentPriceLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(rentPriceLabel);

		powerbankBoxPanel.add(Box.createVerticalGlue());

		return powerbankBoxPanel;
	}
}
