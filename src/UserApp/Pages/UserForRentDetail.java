package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import CustomGUI.CustomDatePicker;
import CustomGUI.DatePickerAction;
import Struct.ForRentPowerBank;
import Struct.PowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserForRentDetail extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	public ForRentPowerBank powerBank;

	private JLabel rentDayPlaceHolderLabel, returnDayPlaceHolderLabel;
	private JComboBox returnAddressDropdown;
	
	private JLabel brandLabel;
	private JLabel nameLabel;
	private JLabel modelLabel;
	private JLabel whAndCapacityLabel;
	private JLabel sizeAndWeightLabel;
	private JLabel inputLabel;
	private JLabel outputLabel;
	private JLabel rentPriceLabel;
	private JLabel maxRentDayLabel;
	private JLabel powerBankImageLabel;
	private JLabel pickUpAddressPlaceHolderLabel;
	private JLabel paymetPlaceHolderLabel;
	private JLabel lateFeeLabel;
	
	private Calendar rentDate, returnDate;
	private int rentDuration;
	private double totalPrice;

	public UserForRentDetail(UserMainFrame mainFrame, Color backgroundColor, Color menuColor, Color mainColor) {
		super(new BorderLayout());
		
		// 342
		
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		
		createForRentDetailPage();
	}

	public void createForRentDetailPage() {
		this.setBackground(backgroundColor);

		// Title
		JLabel titleLabel = new JLabel("รายละเอียดการเช่า", SwingConstants.LEFT);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setBackground(backgroundColor);
		this.add(titleLabel, BorderLayout.NORTH);

		JPanel detailContainerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		detailContainerPanel.setBackground(backgroundColor);
		detailContainerPanel.setAlignmentX(LEFT_ALIGNMENT);
		detailContainerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		detailContainerPanel.add(createPowerbankBox());
		detailContainerPanel.add(createDetailInput());

		this.add(detailContainerPanel, BorderLayout.CENTER);
	}
	
	public void updateForRentDetailPage() {
		updatePowerbankBox();
		updateInput();
	}

	private JPanel createDetailInput() {
		JPanel detailInputPanel = new JPanel();
		detailInputPanel.setLayout(new BoxLayout(detailInputPanel, BoxLayout.Y_AXIS));
		detailInputPanel.setBackground(backgroundColor);
		detailInputPanel.setAlignmentX(LEFT_ALIGNMENT);

		int margin = 8;
		int fontSize = 16;
		int fontAdd = 4;
		float componentAlignment = LEFT_ALIGNMENT;
		
		// Make everything closer together
		detailInputPanel.add(Box.createVerticalStrut(50));
		
		// Rent Day
		JPanel rentDayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rentDayPanel.setBackground(backgroundColor);
		detailInputPanel.add(rentDayPanel);

		JLabel rentDayLabel = new JLabel("วันที่เช่า:");
		rentDayLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		rentDayLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		rentDayLabel.setAlignmentX(componentAlignment);
		rentDayPanel.add(rentDayLabel);

		JPanel rentDayPlaceHolder = new JPanel(new BorderLayout());
		rentDayPlaceHolder.setBackground(backgroundColor);
		rentDayPlaceHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		rentDayPlaceHolder.setPreferredSize(new Dimension(180, 30));

		rentDayPlaceHolderLabel = new JLabel("");
		rentDayPlaceHolderLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		rentDayPlaceHolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		rentDayPlaceHolderLabel.setAlignmentX(componentAlignment);
		rentDayPlaceHolderLabel.setAlignmentY(CENTER_ALIGNMENT);
		rentDayPlaceHolder.add(rentDayPlaceHolderLabel, BorderLayout.WEST);
		rentDayPanel.add(rentDayPlaceHolder);

		ImageIcon calendarIcon = Utils.createImageIcon("/icons/calendar_icon_24.png", "Calendar Icon");
		JButton calendarButton = Utils.createIconButton(calendarIcon);
		rentDayPanel.add(calendarButton);

		calendarButton.addActionListener(e -> {
			CustomDatePicker datePicker = new CustomDatePicker(SwingUtilities.getWindowAncestor(this), backgroundColor,
					Color.LIGHT_GRAY, Color.BLACK);

			datePicker.addDatePickerAction(new DatePickerAction() {
				@Override
				public void onDatePicked() {
					if (Utils.isDatePast(datePicker.selectedDate)) {
						Utils.showDialog(mainFrame, "เลือกวันในอดีตไม่ได้!", "คุณไม่สามารถเลือกวันในอดีตได้ โปรดเลือกวันนี้หรือวันข้างหน้า");
						return;
					}
					
					returnDate = null;
					rentDuration = -1;
					totalPrice = 0.0;
					paymetPlaceHolderLabel.setText("");
					
					if (returnDayPlaceHolderLabel != null) returnDayPlaceHolderLabel.setText("");
					
					rentDate = datePicker.selectedDate;
					SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
					String formattedDate = dateFormatter.format(rentDate.getTime());
					rentDayPlaceHolderLabel.setText(formattedDate);
				}

			});
			
			datePicker.createDatePicker();

			datePicker.setSize(600, 400);
			datePicker.setLocationRelativeTo(null);
			datePicker.setVisible(true);

		});
		
		// Return Date
		JPanel returnDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		returnDatePanel.setBackground(backgroundColor);
		detailInputPanel.add(returnDatePanel);

		JLabel returnDateLabel = new JLabel("วันที่คืน:");
		returnDateLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		returnDateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		returnDateLabel.setAlignmentX(componentAlignment);
		returnDatePanel.add(returnDateLabel);

		JPanel returnDatePlaceHolder = new JPanel(new BorderLayout());
		returnDatePlaceHolder.setBackground(backgroundColor);
		returnDatePlaceHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		returnDatePlaceHolder.setPreferredSize(new Dimension(180, 30));

		returnDayPlaceHolderLabel = new JLabel("");
		returnDayPlaceHolderLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		returnDayPlaceHolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		returnDayPlaceHolderLabel.setAlignmentX(componentAlignment);
		returnDayPlaceHolderLabel.setAlignmentY(CENTER_ALIGNMENT);
		returnDatePlaceHolder.add(returnDayPlaceHolderLabel, BorderLayout.WEST);
		returnDatePanel.add(returnDatePlaceHolder);

		ImageIcon calendarReturnDateIcon = Utils.createImageIcon("/icons/calendar_icon_24.png", "Calendar Icon");
		JButton calendarReturnDateButton = Utils.createIconButton(calendarReturnDateIcon);
		returnDatePanel.add(calendarReturnDateButton);

		calendarReturnDateButton.addActionListener(e -> {
			if (rentDate == null) {
				Utils.showDialog(mainFrame, "โปรดเลือกวันเช่า", "คุณจะต้องเลือกวันเช่าก่อนจึงจะสามารถเลือกวันส่งคืนได้");
				return;
			}
			
			CustomDatePicker datePicker = new CustomDatePicker(SwingUtilities.getWindowAncestor(this), backgroundColor,
					Color.LIGHT_GRAY, Color.BLACK);

			datePicker.addDatePickerAction(new DatePickerAction() {
				@Override
				public void onDatePicked() {
					int countDay = Utils.countDays(rentDate, datePicker.selectedDate);
					
					if (countDay < 0) {
						Utils.showDialog(mainFrame, "วันที่คืนก่อนหน้าวันที่เช่า!", "คุณไม่สามารถย้อนอดีตเพื่อคืน Powerbank ได้ โปรดเลือกวันที่คืนหลังจากวันที่เช่า");
						return;
					}
					
					if (countDay > powerBank.maxDuration) {
						Utils.showDialog(mainFrame, "จำนวนวันเกินวันที่อนุญาตให้เช่าสูงสุด!", "Powerbank เครื่องนี้อนุญาตให้เช่าได้สูงสุด " + String.valueOf(powerBank.maxDuration) + " วัน โปรดเลือกใหม่อีกครั้ง");
						return;
					}
					
					returnDate = datePicker.selectedDate;
					rentDuration = countDay;
					SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
					String formattedDate = dateFormatter.format(returnDate.getTime());
					returnDayPlaceHolderLabel.setText(formattedDate);
					
					updatePayment();
				}

			});
			
			datePicker.createDatePicker();

			datePicker.setSize(600, 400);
			datePicker.setLocationRelativeTo(null);
			datePicker.setVisible(true);

		});
		
		// Pick up address
		JPanel pickUpAddressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pickUpAddressPanel.setBackground(backgroundColor);
		detailInputPanel.add(pickUpAddressPanel);

		JLabel pickUpAddressLabel = new JLabel("สถานที่รับ:");
		pickUpAddressLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		pickUpAddressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		pickUpAddressLabel.setAlignmentX(componentAlignment);
		pickUpAddressPanel.add(pickUpAddressLabel);

		JPanel pickUpAddressPlaceHolder = new JPanel(new BorderLayout());
		pickUpAddressPlaceHolder.setBackground(backgroundColor);
		pickUpAddressPlaceHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		pickUpAddressPlaceHolder.setPreferredSize(new Dimension(300, 30));
		
		pickUpAddressPlaceHolderLabel = new JLabel();
		pickUpAddressPlaceHolderLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		pickUpAddressPlaceHolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		pickUpAddressPlaceHolderLabel.setAlignmentX(componentAlignment);
		pickUpAddressPlaceHolderLabel.setAlignmentY(CENTER_ALIGNMENT);
		pickUpAddressPlaceHolder.add(pickUpAddressPlaceHolderLabel, BorderLayout.WEST);
		pickUpAddressPanel.add(pickUpAddressPlaceHolder);
		
		// Return Address
		JPanel returnAddressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		returnAddressPanel.setBackground(backgroundColor);
		detailInputPanel.add(returnAddressPanel);
		
		JLabel returnAddressLabel = new JLabel("สถานที่คืน:");
		returnAddressLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		returnAddressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		returnAddressLabel.setAlignmentX(componentAlignment);
		returnAddressPanel.add(returnAddressLabel);
		
		String[] returnList = new String[] {"สนามบินสุวรรณภูมิ", "สนามบินดอนเมือง", "สนามบินภูเก็ต", "สนามบินเชียงใหม่", "สนามบินแม่ฟ้าหลวง", "สนามบินหาดใหญ่", "สนามบินขอนแก่น"};
		returnAddressDropdown = Utils.createAppDropdown(returnList, backgroundColor, menuColor, new Dimension(300, 30));
		returnAddressPanel.add(returnAddressDropdown);
		
		// Payment
		JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		paymentPanel.setBackground(backgroundColor);
		detailInputPanel.add(paymentPanel);
		
		JLabel paymentLabel = new JLabel("ยอดชำระ:");
		paymentLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		paymentLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		paymentLabel.setAlignmentX(componentAlignment);
		paymentPanel.add(paymentLabel);
		
		JPanel paymetPlaceHolder = new JPanel(new BorderLayout());
		paymetPlaceHolder.setBackground(backgroundColor);
		paymetPlaceHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		paymetPlaceHolder.setPreferredSize(new Dimension(150, 30));
		paymentPanel.add(paymetPlaceHolder);
		
		paymetPlaceHolderLabel = new JLabel();
		paymetPlaceHolderLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		paymetPlaceHolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		paymetPlaceHolderLabel.setAlignmentX(componentAlignment);
		paymetPlaceHolderLabel.setAlignmentY(CENTER_ALIGNMENT);
		paymetPlaceHolder.add(paymetPlaceHolderLabel, BorderLayout.WEST);
		paymentPanel.add(paymetPlaceHolder);
		
		// Late fee per day
		JPanel lateFeePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lateFeePanel.setBackground(backgroundColor);
		detailInputPanel.add(lateFeePanel);
		
		lateFeeLabel = new JLabel();
		lateFeeLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		lateFeeLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		lateFeeLabel.setForeground(Color.RED);
		lateFeeLabel.setAlignmentX(componentAlignment);
		lateFeeLabel.setAlignmentY(CENTER_ALIGNMENT);
		lateFeePanel.add(lateFeeLabel);
		
		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(backgroundColor);
		detailInputPanel.add(buttonPanel);
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		// Back button
		JButton backButton = new JButton("กลับ");
		backButton.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		backButton.setBackground(mainColor);
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.setBorder(null);
		backButton.setMaximumSize(new Dimension(150, 50));
		backButton.setPreferredSize(new Dimension(150, 50));
		backButton.setAlignmentX(CENTER_ALIGNMENT);
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonPanel.add(backButton);
		backButton.addActionListener(e -> {
			mainFrame.changeCard(2, 2);
		});
		
		buttonPanel.add(Box.createHorizontalStrut(50));
		
		// Payment button
		JButton payButton = new JButton("ชำระเงิน");
		payButton.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		payButton.setBackground(menuColor);
		payButton.setForeground(Color.WHITE);
		payButton.setFocusPainted(false);
		payButton.setBorder(null);
		payButton.setMaximumSize(new Dimension(150, 50));
		payButton.setPreferredSize(new Dimension(150, 50));
		payButton.setAlignmentX(CENTER_ALIGNMENT);
		payButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonPanel.add(payButton);
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		// Make everything closer together
		detailInputPanel.add(Box.createVerticalStrut(30));
		
		return detailInputPanel;
	}
	
	private void updateInput() {
		rentDayPlaceHolderLabel.setText("");
		returnDayPlaceHolderLabel.setText("");
		
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to update input.");
			return;
		}
		
		String pickUpAddress = powerBank.address + " Locker หมายเลข " + String.valueOf(powerBank.lockerNumber);
		pickUpAddressPlaceHolderLabel.setText(pickUpAddress);
		
		rentDate = null;
		returnDate = null;
		rentDuration = -1;
		totalPrice = 0.0;
		paymetPlaceHolderLabel.setText("");
		
		String lateFee = "หมายเหตุ: ค่าปรับส่งคืน powerbank ล่าช้า " + String.valueOf(powerBank.lateFeePerDay) + " บาท/วัน";
		lateFeeLabel.setText(lateFee);
	}
	
	private void updatePayment() {
		if (paymetPlaceHolderLabel == null) {
			System.err.println("Null payment place holder label. Unable to update payment amount.");
			return;
		}
		
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to update payment amount.");
			return;
		}
		
		if (rentDuration > powerBank.maxDuration || rentDuration < 0) {
			System.err.println("Rent Duration is not supposed to be more than max duration or less than 0. Unable to update payment amount.");
			return;
		}
		
		// A Whole Day Rent
		if (rentDuration == 0) rentDuration = 1;
		
		totalPrice = powerBank.pricePerDay * rentDuration;
		String payment = String.valueOf(totalPrice) + " บาท";
		paymetPlaceHolderLabel.setText(payment);
	}
	
	private void updatePowerbankBox() {
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
		
		String rentPrice = String.valueOf(powerBank.pricePerDay) + " บาท/วัน";
		rentPriceLabel.setText(rentPrice);
		
		String maxRentDay = "เช่าได้สูงสุด " + String.valueOf(powerBank.maxDuration) + " วัน";
		maxRentDayLabel.setText(maxRentDay);
	}
	
	private JPanel createPowerbankBox() {
		JPanel powerbankBoxPanel = new JPanel();
		powerbankBoxPanel.setLayout(new BoxLayout(powerbankBoxPanel, BoxLayout.Y_AXIS));
		powerbankBoxPanel.setBackground(backgroundColor);
		powerbankBoxPanel.add(Box.createVerticalGlue());

		int margin = 12;
		int fontSize = 20;
		int fontAdd = 4;
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

		/*
		 * String locker = "Locker หมายเลข " + String.valueOf(powerBank.lockerNumber);
		 * JLabel lockerLabel = new JLabel(locker); lockerLabel.setFont(new
		 * Font("Tahoma", Font.PLAIN, fontSize));
		 * lockerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		 * lockerLabel.setAlignmentX(componentAlignment);
		 * powerbankBoxPanel.add(lockerLabel);
		 */

		rentPriceLabel = new JLabel();
		rentPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		rentPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		rentPriceLabel.setAlignmentX(componentAlignment);
		rentPriceLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(rentPriceLabel);

		maxRentDayLabel = new JLabel();
		maxRentDayLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		maxRentDayLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		maxRentDayLabel.setAlignmentX(componentAlignment);
		maxRentDayLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(maxRentDayLabel);

		/*
		 * String lateFee = "ค่าปรับส่งคืนสาย " +
		 * String.valueOf(powerBank.lateFeePerDay) + " บาท/วัน"; JLabel lateFeeLabel =
		 * new JLabel(lateFee); lateFeeLabel.setFont(new Font("Tahoma", Font.PLAIN,
		 * fontSize)); lateFeeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0,
		 * margin, 0)); lateFeeLabel.setAlignmentX(componentAlignment);
		 * lateFeeLabel.setForeground(new Color(255, 95, 21)); // Warning Color
		 * powerbankBoxPanel.add(lateFeeLabel);
		 */

		powerbankBoxPanel.add(Box.createVerticalGlue());

		return powerbankBoxPanel;
	}

}