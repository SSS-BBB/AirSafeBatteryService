package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.CustomScrollPane;
import CustomGUI.NameValueHolder;
import CustomGUI.PowerBankBox;
import Database.DatabaseConnector;
import Struct.DepositedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserDepositPayment extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;
	private DepositedPowerBank powerBank;

	private PowerBankBox powerBankBox;

	// Deposit Detail Value Holders
	private NameValueHolder userNameHolder, addressHolder, lockerNumberHolder, depositDateHolder, returnDateHolder,
			depositPriceHolder;

	// Payment detail value holders
	private NameValueHolder priceDisplayHolder;
	
	private UserProcessStatus depositStatusPage;

	public UserDepositPayment(UserMainFrame mainFrame, Color backgroundColor, Color menuColor, Color mainColor,
			DepositedPowerBank powerBank, UserProcessStatus depositStatusPage) {
		this(mainFrame, backgroundColor, menuColor, mainColor, depositStatusPage);
		updatePage(powerBank);
	}

	public UserDepositPayment(UserMainFrame mainFrame, Color backgroundColor, Color menuColor, Color mainColor, UserProcessStatus depositStatusPage) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		this.depositStatusPage = depositStatusPage;

		setBackground(backgroundColor);

		// Title
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		titlePanel.setBackground(null);
		this.add(titlePanel, BorderLayout.NORTH);

		JLabel titleLabel = createPageLabel("ชำระเงิน", Font.BOLD, 32);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titlePanel.add(titleLabel);

		JPanel summaryBox = Utils.createBoxWithLabelInside("สรุปรายการสั่งซื้อ", menuColor, Color.WHITE, 20);
		summaryBox.setAlignmentX(LEFT_ALIGNMENT);
		titlePanel.add(summaryBox);

		// Content
		JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		contentPanel.setBackground(backgroundColor);

		CustomScrollPane contentScrollPane = new CustomScrollPane(contentPanel, mainColor, backgroundColor);
		contentScrollPane.setBackground(backgroundColor);
		this.add(contentScrollPane, BorderLayout.CENTER);

		// Power Bank Box
		powerBankBox = new PowerBankBox(backgroundColor, 18, 16, 5);
		powerBankBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		contentPanel.add(powerBankBox);

		// Deposit Detail
		JPanel detailBox = createPaymentDetail();
		detailBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 75));
		contentPanel.add(detailBox);

		// Payment Box
		JPanel paymentBox = createUserPayPanel();
		paymentBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		contentPanel.add(paymentBox);
	}

	private JPanel createUserPayPanel() {
		JPanel userPayPanel = new JPanel();
		userPayPanel.setLayout(new BoxLayout(userPayPanel, BoxLayout.Y_AXIS));
		userPayPanel.setBackground(null);

		int bodyFontSize = 16;
		int headingFontSize = 18;
		int margin = 8;

		// create payment

		// Scan To Pay
		JLabel scanToPayLabel = createPageLabel("แสกนเพื่อชำระเงิน", Font.BOLD, headingFontSize);
		userPayPanel.add(scanToPayLabel);

		// QR Code
		ImageIcon qrIcon = Utils.createImageIcon("/qr_code.png", "Fake QR Code");
		JLabel qrIconLabel = new JLabel(qrIcon);
		qrIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(qrIconLabel);
		userPayPanel.add(Box.createVerticalStrut(margin));

		// Total Price
		JLabel totalPriceLabel = createPageLabel("ยอดชำระทั้งหมด", Font.BOLD, bodyFontSize);
		userPayPanel.add(totalPriceLabel);

		// Price Display
		priceDisplayHolder = new NameValueHolder("", bodyFontSize, bodyFontSize, new Dimension(200, 30));
		priceDisplayHolder.setAlignmentX(CENTER_ALIGNMENT);
		userPayPanel.add(priceDisplayHolder);
		userPayPanel.add(Box.createVerticalStrut(margin));

		// Payment Time Limit
		JLabel timeLimitLabel = createPageLabel("กรุณาชำระภายใน 15 นาที", Font.BOLD, bodyFontSize);
		timeLimitLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
		timeLimitLabel.setForeground(new Color(0, 100, 0));
		userPayPanel.add(timeLimitLabel);
		userPayPanel.add(Box.createVerticalStrut(margin));

		// Payment Confirm
		Dimension buttonSize = new Dimension(180, 40);
		JButton paymentConfirmButton = Utils.createColorBackgroundButton("ยืนยันการชำระเงิน", mainColor, Color.WHITE,
				bodyFontSize);
		paymentConfirmButton.setAlignmentX(CENTER_ALIGNMENT);
		paymentConfirmButton.setPreferredSize(buttonSize);
		paymentConfirmButton.setMaximumSize(buttonSize);
		userPayPanel.add(paymentConfirmButton);

		// When payment confirm button is clicked
		// add data to transaction
		// if power bank exists get power bank id
		// else add data to power bank and get the id
		// add data to deposited power bank
		// change to successful payment page

		paymentConfirmButton.addActionListener(e -> {
			// Transaction Process
			int paymentId = DatabaseConnector.insertIntoTransaction(powerBank.depositPrice, mainFrame.userDetail.userID);

			if (paymentId < 0) {
				System.err.println(
						"Something went wrong when trying to insert data into transaction table. Unable to proceed the deposit.");
				failDepositProcess();
				return;
			}
			
			// Add data to power bank table if not exists
			int powerBankId = -1;
			if (!DatabaseConnector.powerBankExist(powerBank.deviceInfo.brand, powerBank.deviceInfo.model)) {
				powerBankId = DatabaseConnector.insertIntoPowerbank(powerBank.deviceInfo);
				if (powerBankId < 0) {
					System.err.println(
							"Something went wrong when trying to insert data into power bank table. Unable to proceed the deposit.");
					failDepositProcess();
					return;
				}
			}
			else {
				powerBankId = DatabaseConnector.getPowerBank(powerBank.deviceInfo.brand, powerBank.deviceInfo.model, false).powerBankId;
				if (powerBankId < 0) {
					System.err.println(
							"Something went wrong when trying to get data from power bank table. Unable to proceed the deposit.");
					failDepositProcess();
					return;
				}
			}
			
			// DepositedPowerBank
			String lockerPassword = DatabaseConnector.insertIntoDepositedPowerBank(powerBank, powerBankId, paymentId);
			if (lockerPassword.isEmpty()) {
				System.err.println(
						"Something went wrong when trying to insert data into deposited power bank table. Unable to proceed the deposit.");
				failDepositProcess();
				return;
			}
			
			String lockerNumberText = "Locker หมายเลข " + String.valueOf(powerBank.lockerNumber);
			String lockerPassWordText = "รหัสสำหรับปลดล็อก " + lockerPassword;
			depositStatusPage.updateProcessStatusPage(true, "ฝากสำเร็จ", powerBank.address, lockerNumberText, lockerPassWordText, "โปรดบันทึกรหัสนี้ไว้สำหรับการปลดล็อกตู้");
			
			mainFrame.changeCard(3, 10);
		});

		return userPayPanel;
	}
	
	private void failDepositProcess() {
		depositStatusPage.updateProcessStatusPage(false, "ฝากไม่สำเร็จ", "มีบางอย่างผิดพลาดในกระบวนการฝาก", "โปรดลองอีกครั้งภายหลัง", "", "");
		mainFrame.changeCard(3, 10);
	}

	private JPanel createPaymentDetail() {
		JPanel detailPanel = new JPanel();
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
		detailPanel.setBackground(backgroundColor);
		detailPanel.setAlignmentX(CENTER_ALIGNMENT);

		// create holders
		int bodyFontSize = 16;
		int margin = 16;
		Dimension boxSize = new Dimension(180, 30);

		// Username
		userNameHolder = new NameValueHolder("ชื่อผู้ฝาก:", bodyFontSize, bodyFontSize, boxSize);
		userNameHolder.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(userNameHolder);

		// Address
		addressHolder = new NameValueHolder("สถานที่ฝาก:", bodyFontSize, bodyFontSize, boxSize);
		addressHolder.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(addressHolder);

		// Locker Number
		lockerNumberHolder = new NameValueHolder("หมายเลข Locker:", bodyFontSize, bodyFontSize, boxSize);
		lockerNumberHolder.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(lockerNumberHolder);

		// Deposit Date
		depositDateHolder = new NameValueHolder("วันที่ฝาก:", bodyFontSize, bodyFontSize, boxSize);
		depositDateHolder.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(depositDateHolder);

		// Return Date
		returnDateHolder = new NameValueHolder("วันที่มารับคืน:", bodyFontSize, bodyFontSize, boxSize);
		returnDateHolder.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(returnDateHolder);

		// Price
		depositPriceHolder = new NameValueHolder("สรุปยอดชำระ:", bodyFontSize, bodyFontSize, boxSize);
		depositPriceHolder.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		detailPanel.add(depositPriceHolder);

		// Back Button
		JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonWrapper.setBackground(null);
		buttonWrapper.setAlignmentX(LEFT_ALIGNMENT);
		detailPanel.add(buttonWrapper);

		Dimension buttonSize = new Dimension(140, 40);
		JButton backButton = Utils.createColorBackgroundButton("กลับ", menuColor, Color.WHITE, bodyFontSize);
		backButton.setAlignmentX(CENTER_ALIGNMENT);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		buttonWrapper.add(backButton);

		backButton.addActionListener(e -> {
			mainFrame.changeCard(3, 3);
		});

		detailPanel.add(Box.createVerticalStrut(160));

		return detailPanel;
	}

	public void updatePage(DepositedPowerBank powerBank) {
		if (powerBank == null) {
			System.err.println("Null Power Bank. Unable to update deposit payment page.");
			return;
		}

		this.powerBank = powerBank;
		powerBankBox.updatePowerbankBox(powerBank.deviceInfo);

		// update components
		String displayName = mainFrame.userDetail.firstName + " " + mainFrame.userDetail.lastName;
		userNameHolder.setValue(displayName);

		addressHolder.setValue(powerBank.address);

		lockerNumberHolder.setValue(String.valueOf(powerBank.lockerNumber));

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String depositDateText = dateFormatter.format(powerBank.depositDate.getTime());
		depositDateHolder.setValue(depositDateText);

		String returnDateText = dateFormatter.format(powerBank.returnDate.getTime());
		returnDateHolder.setValue(returnDateText);

		String priceText = String.valueOf(powerBank.depositPrice) + " บาท";
		depositPriceHolder.setValue(priceText);

		priceDisplayHolder.setValue(priceText);
	}

	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {

		return createPageLabel("", fontStyle, fontSize);
	}

}
