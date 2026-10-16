package GeneralPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.NameValueHolder;
import CustomGUI.PowerBankBox;
import Struct.DepositedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class DepositedDetailPage extends JPanel {

	private UserMainFrame userMainFrame;
	private Color backgroundColor, menuColor, mainColor;

	private PowerBankBox powerBankBox;

	// Deposit Detail Components
	private NameValueHolder depositAddressHolder, lockerNumberHolder, lockerPasswordHolder, depositDateHolder,
			returnDateHolder, depositPriceHolder, lateFeePerDayHolder, lateDayHolder, totalLateFeeHolder;

	public DepositedDetailPage(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor,
			DepositedPowerBank powerBank) {
		this(mainFrame, backgroundColor, mainColor, menuColor);
		updatePage(powerBank);
	}

	public DepositedDetailPage(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.userMainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;

		setBackground(backgroundColor);

		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setText("การฝาก");
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

		// Deposit Detail
		JPanel depositDetailBox = createDepositDetailBox();
		contentPanel.add(depositDetailBox);

		return contentPanel;
	}

	private JPanel createDepositDetailBox() {
		JPanel depositDetailPanel = new JPanel();
		depositDetailPanel.setLayout(new BoxLayout(depositDetailPanel, BoxLayout.Y_AXIS));
		depositDetailPanel.setBackground(null);
		depositDetailPanel.setAlignmentX(LEFT_ALIGNMENT);

		int bodyFontSize = 18;

		// create value detail holder

		// Deposit Address
		depositAddressHolder = new NameValueHolder("สถานที่ฝาก:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(depositAddressHolder);

		// Locker Number
		lockerNumberHolder = new NameValueHolder("หมายเลข Locker:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(lockerNumberHolder);

		// Locker Password
		lockerPasswordHolder = new NameValueHolder("รหัสปลดล็อก:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(lockerPasswordHolder);

		// Deposit Date
		depositDateHolder = new NameValueHolder("วันที่ฝาก:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(depositDateHolder);

		// Return Date
		returnDateHolder = new NameValueHolder("วันที่รับคืน:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(returnDateHolder);

		// Deposit Price
		depositPriceHolder = new NameValueHolder("ค่าฝากรวม:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(depositPriceHolder);

		// Late Fee Per Day
		lateFeePerDayHolder = new NameValueHolder("ค่าปรับมารับสาย:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(lateFeePerDayHolder);

		// Late Day
		lateDayHolder = new NameValueHolder("จำนวนวันที่สาย:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(lateDayHolder);

		// Total Fee
		totalLateFeeHolder = new NameValueHolder("รวมค่าปรับ:", bodyFontSize, bodyFontSize);
		depositDetailPanel.add(totalLateFeeHolder);

		// Inform user to get their power bank within the set address and date
		JPanel informWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
		informWrapper.setAlignmentX(LEFT_ALIGNMENT);
		informWrapper.setBackground(null);
		depositDetailPanel.add(informWrapper);

		JLabel informLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		informLabel.setText("รับคืน Power Bank ณ สถานที่และวันที่กำหนด");
		informWrapper.add(informLabel);

		// Back button
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.setBackground(null);
		depositDetailPanel.add(buttonPanel);

		Dimension buttonSize = new Dimension(150, 50);
		JButton backButton = Utils.createBorderButton("กลับ", menuColor, mainColor, bodyFontSize, 2);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		buttonPanel.add(backButton);

		backButton.addActionListener(e -> {
			if (userMainFrame != null)
				userMainFrame.changeCard(4, 4);
		});

		return depositDetailPanel;
	}

	public void updatePage(DepositedPowerBank powerBank) {
		powerBankBox.updatePowerbankBox(powerBank.deviceInfo);

		// update deposit detail information
		depositAddressHolder.setValue(powerBank.address);
		lockerNumberHolder.setValue(String.valueOf(powerBank.lockerNumber));
		lockerPasswordHolder.setValue(powerBank.lockerPassword);
		depositDateHolder.setValue(Utils.getDateFormat(powerBank.depositDate, "dd/MM/yyyy"));
		returnDateHolder.setValue(Utils.getDateFormat(powerBank.returnDate, "dd/MM/yyyy"));
		depositPriceHolder.setValue(String.valueOf(powerBank.depositPrice) + " บาท");
		lateFeePerDayHolder.setValue(String.valueOf(powerBank.lateFeePerDay) + " บาท/วัน");

		// Late days
		LocalDate today = LocalDate.now();
		int lateDay = Utils.countDays(powerBank.returnDate, today);
		lateDay = (lateDay < 0) ? 0 : lateDay;
		double totalLateFee = powerBank.lateFeePerDay * lateDay;

		lateDayHolder.setValue(String.valueOf(lateDay) + " วัน");
		totalLateFeeHolder.setValue(String.valueOf(totalLateFee) + " บาท");

		/*
		 * depositAddressHolder, lockerNumberHolder, lockerPasswordHolder,
		 * depositDateHolder, returnDateHolder, depositPriceHolder, lateFeePerDayHolder,
		 * lateDayHolder, totalLateFeeHolder
		 */
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);

		return pageLabel;
	}

}
