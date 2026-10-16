package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import CustomGUI.CustomDatePicker;
import CustomGUI.CustomDropDown;
import CustomGUI.DatePickerAction;
import CustomGUI.LockerChooser;
import CustomGUI.NameValueHolder;
import CustomGUI.PowerBankBox;
import Struct.DepositedPowerBank;
import Struct.PowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserDeposit extends JPanel {
	
	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;
	
	private PowerBankBox powerBankBox;
	
	// User Input Components
	private CustomDropDown<String> addressDropDown;
	
	private PowerBank deviceInfo;
	
	// Input Data
	private String depositAddress;
	private int depositLockerNumber;
	private LocalDate depositDate, returnDate;
	private double depositPrice, lateFeePerDay, depositPricePerDay;
	
	private UserDepositPayment paymentPage;
	
	public UserDeposit(UserMainFrame mainFrame, 
			Color backgroundColor, Color menuColor, Color mainColor,
			UserDepositPayment paymentPage) {
		
		super(new BorderLayout());
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		this.lateFeePerDay = 50.0;
		this.depositPricePerDay = 25.0;
		this.paymentPage = paymentPage;
		
		depositAddress = "";
		depositLockerNumber = -1;
		depositDate = null;
		returnDate = null;
		depositPrice = -1.0;
		
		setBackground(backgroundColor);
		
		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		titleLabel.setText("ฝาก Power Bank");
		this.add(titleLabel, BorderLayout.NORTH);
		
		// Content
		JPanel contentPanel = createContentPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(contentPanel, BorderLayout.CENTER);
		
	}
	
	public UserDeposit(UserMainFrame mainFrame, 
			Color backgroundColor, Color menuColor, Color mainColor,
			PowerBank tempPowerBank, UserDepositPayment paymentPage) {
		this(mainFrame, backgroundColor, menuColor, mainColor, paymentPage);
		updatePage(tempPowerBank);
	}
	
	public void updatePage(PowerBank powerBank) {
		if (powerBankBox == null) {
			System.err.println("Null Power Bank Box. Unable to update deposit page.");
			return;
		}
		
		this.deviceInfo = powerBank;
		powerBankBox.updatePowerbankBox(deviceInfo);
	}
	
	private JPanel createUserInputPanel() {
		JPanel userInputPanel = new JPanel();
		userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.Y_AXIS));
		userInputPanel.setBackground(null);
		userInputPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		int margin = 8;
		int headingFontSize = 20;
		int bodyFontSize = 18;
		float componentAlignment = LEFT_ALIGNMENT;
		
		// Address
		String[] addressList = new String[] {"สนามบินสุวรรณภูมิ", "สนามบินดอนเมือง", "สนามบินภูเก็ต", "สนามบินเชียงใหม่", "สนามบินแม่ฟ้าหลวง", "สนามบินหาดใหญ่", "สนามบินขอนแก่น"};
		JPanel addressInputPanel = createTextWithComponentToAdd("สถานที่ฝาก:", bodyFontSize);
		addressInputPanel.setAlignmentX(LEFT_ALIGNMENT);
		userInputPanel.add(addressInputPanel);
		
		addressDropDown = new CustomDropDown<String>(addressList, backgroundColor, menuColor, 2);
		depositAddress = addressDropDown.getSelectedItem().toString();
		addressInputPanel.add(addressDropDown);
		
		Dimension boxSize = new Dimension(addressDropDown.getMinimumSize().width, 30);
		
		// Locker Number
		NameValueHolder lockerHolder = new NameValueHolder("หมายเลข Locker:", bodyFontSize, bodyFontSize, boxSize);
		userInputPanel.add(lockerHolder);
		
		ImageIcon lockerImage = Utils.createImageIcon("/icons/locker_icon_24.png", "Locker Icon");
		JButton lockerSelectButton = Utils.createIconButton(lockerImage);
		lockerHolder.add(lockerSelectButton);
		
		lockerSelectButton.addActionListener(e -> {
			LockerChooser lockerPicker = new LockerChooser(mainFrame, backgroundColor, Color.BLACK, mainColor, Color.DARK_GRAY, Color.BLACK);
			lockerPicker.setLockerChooserAction(lockerNumber -> {
				depositLockerNumber = lockerNumber;
				lockerHolder.setValue(String.valueOf(lockerNumber));
				
			});
			lockerPicker.showLockerChooser(depositAddress, false);
		});
		
		// force user to pick locker number again if they select new address
		addressDropDown.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String selectedAddress = e.getItem().toString();
				if (!selectedAddress.equals(depositAddress)) {
					depositAddress = selectedAddress;
					depositLockerNumber = -1;
					lockerHolder.setValue("");
				}
			}
		});
		
		// Deposit Date
		NameValueHolder depositDateHolder = new NameValueHolder("วันที่ฝาก:", bodyFontSize, bodyFontSize, boxSize);
		userInputPanel.add(depositDateHolder);
		
		ImageIcon calendarIcon = Utils.createImageIcon("/icons/calendar_icon_24.png", "Calendar Icon");
		JButton calendarDepositButton = Utils.createIconButton(calendarIcon);
		depositDateHolder.add(calendarDepositButton);
		
		// Return Date
		NameValueHolder returnDateHolder = new NameValueHolder("วันที่คืน:", bodyFontSize, bodyFontSize, boxSize);
		userInputPanel.add(returnDateHolder);
				
		ImageIcon calendarReturnIcon = Utils.createImageIcon("/icons/calendar_icon_24.png", "Calendar Icon");
		JButton calendarReturnButton = Utils.createIconButton(calendarReturnIcon);
		returnDateHolder.add(calendarReturnButton);
		
		// Deposit Price
		NameValueHolder depositPriceHolder = new NameValueHolder("ค่าฝาก:", bodyFontSize, bodyFontSize, boxSize);
		userInputPanel.add(depositPriceHolder);
		
		// action listener
		calendarDepositButton.addActionListener(e -> {
			selectDate(false, depositDateHolder, returnDateHolder, depositPriceHolder);
		});
		
		calendarReturnButton.addActionListener(e -> {
			selectDate(true, depositDateHolder, returnDateHolder, depositPriceHolder);
		});
		
		// Note
		JLabel depositPricePerDayLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		String depositPricePerDayText = "หมายเหตุ: ค่าฝาก " + String.valueOf(depositPricePerDay) + " บาท/วัน";
		depositPricePerDayLabel.setText(depositPricePerDayText);
		depositPricePerDayLabel.setForeground(Color.RED);
		userInputPanel.add(depositPricePerDayLabel);
		
		JLabel lateFeePerDayLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		String lateFeePerDayText = "ค่าปรับกรณีมารับคืนเลยวันที่กำหนด " + String.valueOf(lateFeePerDay) + " บาท/วัน";
		lateFeePerDayLabel.setText(lateFeePerDayText);
		lateFeePerDayLabel.setForeground(Color.RED);
		userInputPanel.add(lateFeePerDayLabel);
		
		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(null);
		userInputPanel.add(buttonPanel);
		
		Dimension buttonSize = new Dimension(150, 50);
		JButton backButton = Utils.createBorderButton("กลับ", mainColor, menuColor, bodyFontSize, 2);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		buttonPanel.add(backButton);
		
		buttonPanel.add(Box.createHorizontalStrut(25));
		
		JButton nextButton = Utils.createColorBackgroundButton("ต่อไป", mainColor, Color.WHITE, bodyFontSize);
		nextButton.setPreferredSize(buttonSize);
		nextButton.setMaximumSize(buttonSize);
		buttonPanel.add(nextButton);
		
		// action listener
		backButton.addActionListener(e -> {
			mainFrame.changeCard(3, 11);
		});
		
		nextButton.addActionListener(e -> {
			if (deviceInfo == null) {
				System.err.println("Null Device Info. Unable to go to deposit payment page.");
				return;
			}
			
			// check empty values
			if (depositAddress.isEmpty()) return;
			if (depositDate == null || returnDate == null) return;
			if (depositLockerNumber <= 0) return;
			if (lateFeePerDay < 0) return;
			if (depositPrice < 0) return;
			
			DepositedPowerBank powerBank = new DepositedPowerBank(deviceInfo, depositAddress, depositDate, returnDate, 
					depositLockerNumber, lateFeePerDay, depositPrice);
			// TODO: go to deposit payment page
			if (paymentPage == null) {
				System.err.println("Null Payment Page. Unable to go to deposit payment page.");
				return;
			}
			paymentPage.updatePage(powerBank);
			mainFrame.changeCard(3, 13);
			
		});
		
		return userInputPanel;
	}
	
	private void updatePrice(NameValueHolder depositPriceHolder) {
		if (depositDate == null || returnDate == null) {
			System.err.println("Not enough date data. Unable to calculate the deposit price.");
		}
		
		int depositDayCount = Utils.countDays(depositDate, returnDate);
		if (depositDayCount == 0) depositDayCount = 1;
		
		depositPrice = depositPricePerDay * depositDayCount;
		
		if (depositPrice >= 0)
			depositPriceHolder.setValue(String.valueOf(depositPrice) + " บาท");
		else
			depositPriceHolder.setValue("");
	}
	
	private void selectDate(boolean isReturnDate, NameValueHolder depositDateHolder, 
			NameValueHolder returnDateHolder, NameValueHolder priceHolder) {
		
		if (isReturnDate && depositDate == null) {
			Utils.showDialog(mainFrame, "โปรดเลือกวันฝาก", "คุณจะต้องเลือกวันฝากก่อนจึงจะสามารถเลือกวันส่งคืนได้");
			return;
		}
		
		CustomDatePicker datePicker = new CustomDatePicker(SwingUtilities.getWindowAncestor(this), backgroundColor,
				Color.LIGHT_GRAY, Color.BLACK);
		
		datePicker.addDatePickerAction(new DatePickerAction() {
			@Override
			public void onDatePicked() {
				if (Utils.isDatePast(datePicker.selectedDate)) {
					Utils.showDialog(mainFrame, "เลือกวันในอดีตไม่ได้!", "คุณไม่สามารถเลือกวันในอดีตได้ โปรดเลือกวันนี้หรือวันข้างหน้า");
					return;
				}
				
				if (!isReturnDate) {
					// Deposit Date
					returnDate = null;
					returnDateHolder.setValue("");
					depositPrice = -1.0;
					priceHolder.setValue("");
					
					depositDate = datePicker.selectedDate;

					depositDateHolder.setValue(Utils.getDateFormat(depositDate, "dd/MM/yyyy"));
				}
				
				else {
					// Return Date
					int countDay = Utils.countDays(depositDate, datePicker.selectedDate);
					
					if (countDay < 0) {
						Utils.showDialog(mainFrame, "วันที่คืนก่อนหน้าวันที่ฝาก!", "คุณไม่สามารถย้อนอดีตเพื่อคืน Powerbank ได้ โปรดเลือกวันที่คืนหลังจากวันที่ฝาก");
						return;
					}
					
					returnDate = datePicker.selectedDate;

					returnDateHolder.setValue(Utils.getDateFormat(returnDate, "dd/MM/yyyy"));
					
					updatePrice(priceHolder);
				}
				
			}
			
		});
		
		datePicker.createDatePicker();

		datePicker.setSize(600, 400);
		datePicker.setLocationRelativeTo(null);
		datePicker.setVisible(true);
	}
	
	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		contentPanel.setBackground(null);
		
		int headingFontSize = 18;
		int bodyFontSize = 16;
		
		// Power Bank Box
		powerBankBox = new PowerBankBox(backgroundColor, headingFontSize, bodyFontSize, 5);
		contentPanel.add(powerBankBox);
		
		// User Input
		JPanel userInputPanel = createUserInputPanel();
		contentPanel.add(userInputPanel);
		
		return contentPanel;
	}
	
	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		
		return pageLabel;
	}
	
	private JPanel createTextWithComponentToAdd(String text, int fontSize) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panel.setBackground(null);
		
		JLabel textLabel = createPageLabel(Font.BOLD, fontSize);
		textLabel.setText(text);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		panel.add(textLabel);
		
		return panel;
	}
	
}
