package GeneralPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.NameValueHolder;
import CustomGUI.PowerBankBox;
import Struct.RentedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class RentedDetailPage extends JPanel {

	private UserMainFrame userMainFrame;
	private Color backgroundColor, menuColor, mainColor;
	
	private PowerBankBox powerBankBox;
	
	// TODO: Rented attributes value holder
	private NameValueHolder rentDateHolder, returnDateHolder,
							pickUpAddressHolder, lockerNumberHolder, lockerPasswordHolder,
							returnAddressHolder, rentPriceHolder, lateFeePerDayHolder,
							lateDayHolder, totalLateFeeHolder;
	
	public RentedDetailPage(UserMainFrame mainFrame, 
			Color backgroundColor, Color mainColor, Color menuColor,
			RentedPowerBank powerBank) {
		this(mainFrame, backgroundColor, mainColor, menuColor);
		updatePage(powerBank);
	}
	
	public RentedDetailPage(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.userMainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		
		setBackground(backgroundColor);
		
		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setText("การเช่า");
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(titleLabel, BorderLayout.NORTH);
		
		// Content
		JPanel contentPanel = createContentPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(contentPanel, BorderLayout.CENTER);
	}
	
	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		contentPanel.setBackground(null);
		
		int headingFontSize = 18;
		int bodyFontSize = 16;
		
		// Power Bank Box
		powerBankBox = new PowerBankBox(backgroundColor, headingFontSize, bodyFontSize, 5);
		contentPanel.add(powerBankBox);
		
		// Rent Detail
		JPanel rentDetailBox = createRentDetailBox();
		contentPanel.add(rentDetailBox);
		
		return contentPanel;
	}
	
	private JPanel createRentDetailBox() {
		JPanel rentDetailPanel = new JPanel();
		rentDetailPanel.setLayout(new BoxLayout(rentDetailPanel, BoxLayout.Y_AXIS));
		rentDetailPanel.setBackground(null);
		rentDetailPanel.setAlignmentX(LEFT_ALIGNMENT);

		int bodyFontSize = 18;
		
		// create value detail holder
		
		// Deposit Date
		rentDateHolder = new NameValueHolder("วันที่เช่า:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(rentDateHolder);
		
		// Return Date
		returnDateHolder = new NameValueHolder("วันที่คืน:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(returnDateHolder);
		
		// Pick up address
		pickUpAddressHolder = new NameValueHolder("สถานที่รับ:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(pickUpAddressHolder);
		
		// Locker Number
		lockerNumberHolder = new NameValueHolder("หมายเลข Locker:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(lockerNumberHolder);
		
		// Locker Password
		lockerPasswordHolder = new NameValueHolder("รหัสปลดล็อก:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(lockerPasswordHolder);
		
		// Return Address
		returnAddressHolder = new NameValueHolder("สถานที่คืน:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(returnAddressHolder);
		
		// Rent Price
		rentPriceHolder = new NameValueHolder("ค่าเช่าทั้งหมด:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(rentPriceHolder);
		
		// Late Fee Per Day
		lateFeePerDayHolder = new NameValueHolder("ค่าปรับนำมาคืนสาย:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(lateFeePerDayHolder);
		
		// Late Day
		lateDayHolder = new NameValueHolder("จำนวนวันที่สาย:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(lateDayHolder);
		
		// Total Late Fee
		totalLateFeeHolder = new NameValueHolder("รวมค่าปรับ:", bodyFontSize, bodyFontSize);
		rentDetailPanel.add(totalLateFeeHolder);
		
		// Inform user to return the power bank within a set date
		JPanel informWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
		informWrapper.setAlignmentX(LEFT_ALIGNMENT);
		informWrapper.setBackground(null);
		rentDetailPanel.add(informWrapper);
		
		JLabel informLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		informLabel.setText("นำ Power Bank ไปคืนที่สถานที่และวันที่กำหนด");
		informWrapper.add(informLabel);
		
		// Back button
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.setBackground(null);
		rentDetailPanel.add(buttonPanel);
		
		Dimension buttonSize = new Dimension(150, 50);
		JButton backButton = Utils.createBorderButton("กลับ", menuColor, mainColor, bodyFontSize, 2);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		buttonPanel.add(backButton);
		
		backButton.addActionListener(e -> {
			if (userMainFrame != null) userMainFrame.changeCard(4, 4);
		});
		
		return rentDetailPanel;
	}
	
	public void updatePage(RentedPowerBank powerBank) {
		powerBankBox.updatePowerbankBox(powerBank.deviceInfo);
		
		// update rent details
		rentDateHolder.setValue(Utils.getCalendarDateFormat(powerBank.rentDate, "dd/MM/yyyy"));
		returnDateHolder.setValue(Utils.getCalendarDateFormat(powerBank.returnDate, "dd/MM/yyyy"));
		pickUpAddressHolder.setValue(powerBank.pickUpAddress);
		lockerNumberHolder.setValue(String.valueOf(powerBank.lockerNumber));
		lockerPasswordHolder.setValue(powerBank.lockerPassword);
		returnAddressHolder.setValue(powerBank.returnAddress);
		rentPriceHolder.setValue(String.valueOf(powerBank.rentPrice) + " บาท");
		lateFeePerDayHolder.setValue(String.valueOf(powerBank.lateFeePerDay) + " บาท/วัน");
		
		// Late Day
		Calendar today = Calendar.getInstance();
		int lateDay = Utils.countDays(powerBank.returnDate, today);
		lateDay = (lateDay < 0) ? 0 : lateDay;
		double totalLateFee = powerBank.lateFeePerDay * lateDay;
		
		lateDayHolder.setValue(String.valueOf(lateDay) + " วัน");
		totalLateFeeHolder.setValue(String.valueOf(totalLateFee) + " บาท");
		
		
		/*
		rentDateHolder, returnDateHolder,
		pickUpAddressHolder, lockerNumberHolder, lockerPasswordHolder,
		returnAddressHolder, rentPriceHolder, lateFeePerDayHolder,
		lateDayHolder, totalLateFeeHolder
		 */
		
	}
	
	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		// pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

		return pageLabel;
	}

}
