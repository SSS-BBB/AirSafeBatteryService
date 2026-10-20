package LockerUnlock.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CustomGUI.NameTextFieldHolder;
import LockerUnlock.LockerUnlockMainFrame;
import Utils.Utils;

public class LockerLogin extends JPanel {

	private LockerUnlockMainFrame mainFrame;

	// Input components
	private JTextField userIdentifierTextField, passwordTextField;
	private JLabel failLogin;

	public LockerLogin(LockerUnlockMainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(LockerUnlockMainFrame.BACKGROUND_COLOR);

		int margin = 30;

		JPanel titlePanel = createTitlePanel();
		this.add(titlePanel);
		this.add(Box.createVerticalStrut(margin));

		JPanel userInputPanel = createUserInputPanel();
		this.add(userInputPanel);
		this.add(Box.createVerticalStrut(margin));
		
		JPanel buttonPanel = createButtonPanel();
		this.add(buttonPanel);
		this.add(Box.createVerticalStrut(margin));
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(0, 5));
		buttonPanel.setBackground(null);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		int fontSize = 28;
		int lineThickness = 2;
		Dimension buttonSize = new Dimension(200, 60);
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		// Back
		JButton backButton = Utils.createBorderBackgroundButton("กลับ", 
				LockerUnlockMainFrame.PRIMARY_FONT_COLOR, Color.BLACK, LockerUnlockMainFrame.PRIMARY_COLOR, 
				fontSize, lineThickness, buttonSize);
		buttonPanel.add(backButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		backButton.addActionListener(e -> {
			mainFrame.changeCard(LockerUnlockMainFrame.HOME_PAGE);
		});
		
		// Unlock
		JButton unlockButton = Utils.createBorderBackgroundButton("ปลดล็อก", 
				LockerUnlockMainFrame.PRIMARY_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, 
				fontSize, lineThickness, buttonSize);
		
		buttonPanel.add(unlockButton);
		buttonPanel.add(Box.createHorizontalGlue());
		
		unlockButton.addActionListener(e -> {
			loginCheck();
		});
		
		return buttonPanel;
	}
	
	private void loginCheck() {
		
	}
	
	private JPanel createUserInputPanel() {
		JPanel inputPanel = new JPanel(new GridBagLayout());
		inputPanel.setBackground(null);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		inputPanel.setAlignmentX(CENTER_ALIGNMENT);

		int fontSize = 28;
		int textFieldSize = 14;
		int verticleMargin = 30;
		int horizontalMargin = 30;
		
		GridBagConstraints gbc = getDefaultGridBagConstraints(verticleMargin, horizontalMargin);
		int row = 0;
		
		// User Identifier (Email or Phone)
		JLabel userIdentifierLabel = createPageLabel("Email/เบอร์โทรศัพท์", Font.BOLD, fontSize);
		userIdentifierLabel.setAlignmentX(LEFT_ALIGNMENT);
		gbc.gridx = 0;
		gbc.gridy = row;
		inputPanel.add(userIdentifierLabel, gbc);
		
		userIdentifierTextField = Utils.createAppTextField(LockerUnlockMainFrame.PRIMARY_FONT_COLOR, textFieldSize, fontSize);
		gbc.gridx = 1;
		gbc.gridy = row++;
		inputPanel.add(userIdentifierTextField, gbc);
		
		// Password
		JLabel passwordLabel = createPageLabel("รหัสผ่าน", Font.BOLD, fontSize);
		passwordLabel.setAlignmentX(LEFT_ALIGNMENT);
		gbc.gridx = 0;
		gbc.gridy = row;
		inputPanel.add(passwordLabel, gbc);
		
		passwordTextField = Utils.createAppTextField(LockerUnlockMainFrame.PRIMARY_FONT_COLOR, textFieldSize, fontSize);
		gbc.gridx = 1;
		gbc.gridy = row++;
		inputPanel.add(passwordTextField, gbc);
		
		// Fail login
		failLogin = createPageLabel("กรอก Email/เบอร์โทรหรือรหัสผ่านผิดพลาด โปรดลองอีกครั้ง", Font.PLAIN, fontSize - 2);
		failLogin.setForeground(LockerUnlockMainFrame.FAILED_COLOR);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = row++;
		inputPanel.add(failLogin, gbc);
		
		createGridBagBottomGlue(inputPanel, row++, verticleMargin, horizontalMargin);
		
		return inputPanel;
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBackground(null);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		titlePanel.setAlignmentX(CENTER_ALIGNMENT);

		int fontSize = 45;
		int margin = 15;

		// Title
		JLabel titleLabel = createPageLabel("เข้าสู่ระบบ", Font.BOLD, fontSize);
		titlePanel.add(titleLabel);
		titlePanel.add(Box.createVerticalStrut(margin));

		// User Icon
		ImageIcon userIcon = Utils.createImageIcon("/icons/light_user_icon_120.png", "User Icon");
		JLabel userIconLabel = new JLabel(userIcon);
		userIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		titlePanel.add(userIconLabel);

		return titlePanel;
	}

	private void createGridBagBottomGlue(JPanel gridBagPanel, int row, int verticleMargin, int horizontalMargin) {
		// make components go from top to bottom
		GridBagConstraints gbc = getDefaultGridBagConstraints(verticleMargin, horizontalMargin);
		gbc.gridy = row;
		gbc.weighty = 1.0;
		gbc.gridx = 0;

		gridBagPanel.add(Box.createGlue(), gbc);
	}

	private GridBagConstraints getDefaultGridBagConstraints(int verticleMargin, int horizontalMargin) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
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
