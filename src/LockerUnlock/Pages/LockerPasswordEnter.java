package LockerUnlock.Pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Database.DatabaseConnector;
import LockerUnlock.LockerUnlockMainFrame;
import Struct.DepositedPowerBank;
import Utils.Utils;

public class LockerPasswordEnter extends JPanel {

	private LockerUnlockMainFrame mainFrame;

	// Components
	private JTextField passwordTextField;
	private JLabel failLabel;

	public LockerPasswordEnter(LockerUnlockMainFrame mainFrame) {
		super(new GridBagLayout());
		this.mainFrame = mainFrame;

		setBackground(LockerUnlockMainFrame.BACKGROUND_COLOR);

		createLockerEnterPage();
	}

	private void createLockerEnterPage() {
		GridBagConstraints gbc = getDefaultGridBagConstraints(50, 50);
		gbc.gridwidth = 2;
		int row = 0;

		int headingFontSize = 36;
		int bodyFontSize = 24;
		int textFieldSize = 25;
		int textFieldFontSize = 22;

		// Title
		JLabel titleLabel = createPageLabel("กรอกรหัส Locker", Font.BOLD, headingFontSize);
		gbc.gridy = row++;
		this.add(titleLabel, gbc);

		// Locker Password
		passwordTextField = new JTextField(textFieldSize);
		passwordTextField.setFont(new Font("Tahoma", Font.PLAIN, textFieldFontSize));
		gbc.gridy = row++;
		this.add(passwordTextField, gbc);

		// Fail
		failLabel = createPageLabel("", Font.PLAIN, bodyFontSize);
		failLabel.setForeground(LockerUnlockMainFrame.FAILED_COLOR);
		gbc.gridy = row++;
		this.add(failLabel, gbc);

		// Buttons
		Dimension buttonSize = new Dimension(200, 60);
		gbc.gridwidth = 1;

		// Back Button
		JButton backButton = Utils.createBorderBackgroundButton("กลับ", LockerUnlockMainFrame.PRIMARY_FONT_COLOR,
				Color.BLACK, LockerUnlockMainFrame.PRIMARY_COLOR, bodyFontSize, 2, buttonSize);
		gbc.gridx = 0;
		gbc.gridy = row;
		this.add(backButton, gbc);

		// Next Button
		JButton nextButton = Utils.createBorderBackgroundButton("ต่อไป", LockerUnlockMainFrame.PRIMARY_COLOR,
				LockerUnlockMainFrame.PRIMARY_FONT_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, bodyFontSize, 2,
				buttonSize);
		gbc.gridx = 1;
		gbc.gridy = row++;
		this.add(nextButton, gbc);

		// Action Listener
		backButton.addActionListener(e -> {
			mainFrame.changeCard(LockerUnlockMainFrame.LOGIN_PAGE);
		});

		nextButton.addActionListener(e -> {
			checkLocker();
		});
	}

	private void checkLocker() {
		if (mainFrame.userDetail == null) {
			System.err.println("Null user detail. Unable to check and unlock the locker.");
			return;
		}

		if (passwordTextField == null) {
			System.err.println("Null locker password field. Unable to check and unlock the locker.");
			return;
		}

		// TODO: get locker number from user id and locker password
		// make sure today (the day that user unlock the locker is greater than or equal
		// to rent date / deposit date
		// also make sure today is less than or equal to the return date
		
		int userId = mainFrame.userDetail.userID;
		String enteredPassword = passwordTextField.getText();
		String address = mainFrame.getAddress();
		
		if (mainFrame.lockerType == LockerUnlockMainFrame.RENT_LOCKER) {
			int lockerNumber = DatabaseConnector.getRentLockerNumber(userId, enteredPassword, address);
			if (lockerNumber < 0) {
				showFailLabel();
				return;
			}
			
			failLabel.setText("");
			mainFrame.lockerNumber = lockerNumber;
			System.out.println("Rent Locker: " + String.valueOf(mainFrame.lockerNumber));
			
			// TODO: update locker status page and change to that page
		}

		else if (mainFrame.lockerType == LockerUnlockMainFrame.DEPOSIT_LOCKER) {
			// only locker number and deposit status is returned
			DepositedPowerBank depositInfo = DatabaseConnector.getDepositLockerNumberAndStatus(userId, enteredPassword, address);
			if (depositInfo == null) {
				showFailLabel();
				return;
			}
			
			failLabel.setText("");
			mainFrame.lockerNumber = depositInfo.lockerNumber;
			System.out.println("Deposit Locker: " + String.valueOf(depositInfo.lockerNumber));
			
			// TODO: if the status is 'รับคืนสาย' change to fee payment page
			// else update locker status page and change to that page
		}
		
		else {
			System.err.println("Invalid locker type.");
		}

	}

	private void showFailLabel() {
		if (failLabel != null) {
			String failMessage = "กรอกรหัส Locker ผิดพลาด โปรดลองอีกครั้ง";
			failLabel.setText(failMessage);
		} else {
			System.err.println("Null fail login label. Unable to show fail locker unlock message.");
		}
	}

	private GridBagConstraints getDefaultGridBagConstraints(int verticleMargin, int horizontalMargin) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(verticleMargin / 2, horizontalMargin / 2, verticleMargin / 2, horizontalMargin / 2);

		return gbc;
	}

	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setForeground(LockerUnlockMainFrame.PRIMARY_FONT_COLOR);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setOpaque(true);

		return pageLabel;
	}

}