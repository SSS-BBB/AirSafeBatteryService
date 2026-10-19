package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.NameValueHolder;
import Struct.UserDetail;
import Utils.Utils;

import UserApp.UserMainFrame;

public class UserInformation extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	public UserInformation(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;

		setBackground(backgroundColor);

		JPanel topPanel = createTopPanel();
		this.add(topPanel, BorderLayout.NORTH);

		JPanel userInformationPanel = createUserInformationPanel();
		this.add(userInformationPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = createBottomPanel();
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		bottomPanel.setBackground(null);
		
		int fontSize = 20;
		
		// Log out
		JPanel logOutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		logOutPanel.setBackground(null);
		bottomPanel.add(logOutPanel, BorderLayout.WEST);

		ImageIcon logOutIcon = Utils.createImageIcon("/icons/logout_icon_56.png", "Log Out Icon");
		JButton logOutButton = Utils.createIconButton(logOutIcon);
		logOutPanel.add(logOutButton);

		JLabel logOutLabel = createPageLabel("Log Out", Font.PLAIN, fontSize);
		logOutLabel.setForeground(new Color(178, 59, 59));
		logOutPanel.add(logOutLabel);

		logOutButton.addActionListener(e -> {
			// TODO: Log Out
		});
		
		// Employee Contact
		int contactMargin = 2;
		
		JPanel contactPanel = new JPanel();
		contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
		contactPanel.setBackground(null);
		bottomPanel.add(contactPanel, BorderLayout.EAST);
		
		JLabel contactLabel = createPageLabel("ติดต่อพนักงาน", Font.BOLD, fontSize);
		contactLabel.setAlignmentX(RIGHT_ALIGNMENT);
		contactPanel.add(contactLabel);
		contactPanel.add(Box.createVerticalStrut(contactMargin));
		
		// TODO: get email from admin table
		JLabel contactEmail = createPageLabel("Admin@airsafe.ac.th", Font.PLAIN, fontSize);
		contactEmail.setAlignmentX(RIGHT_ALIGNMENT);
		contactPanel.add(contactEmail);

		return bottomPanel;
	}

	private JPanel createUserInformationPanel() {
		UserDetail userDetail = mainFrame.userDetail;

		JPanel userInformationPanel = new JPanel(new GridBagLayout());
		userInformationPanel.setBackground(null);

		int fontSize = 20;
		int margin = 15;
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridx = 0;
		gbc.insets = new Insets(0, 0, margin, 0);
		int row = 0;

		// User Icon
		ImageIcon userIcon = Utils.createImageIcon("/icons/user_icon_120.png", "User Icon");
		JLabel userIconLabel = new JLabel(userIcon);
		gbc.gridy = row++;
		userInformationPanel.add(userIconLabel, gbc);

		// ID
		NameValueHolder idHolder = new NameValueHolder("ID:", String.valueOf(userDetail.userID), fontSize, fontSize);
		gbc.gridy = row++;
		userInformationPanel.add(idHolder, gbc);

		// Name
		NameValueHolder nameHolder = new NameValueHolder("ชื่อ:", userDetail.firstName + " " + userDetail.lastName,
				fontSize, fontSize);
		gbc.gridy = row++;
		userInformationPanel.add(nameHolder, gbc);

		// Email
		NameValueHolder emailHolder = new NameValueHolder("Email:", userDetail.email, fontSize, fontSize);
		gbc.gridy = row++;
		userInformationPanel.add(emailHolder, gbc);

		// Phone
		NameValueHolder phoneHolder = new NameValueHolder("โทรศัพท์:", userDetail.phone, fontSize, fontSize);
		gbc.gridy = row++;
		userInformationPanel.add(phoneHolder, gbc);

		// My List
		JButton listButton = Utils.createNoBackgroundButton("รายการของฉัน", new Color(36, 160, 237), fontSize);
		gbc.gridy = row++;
		userInformationPanel.add(listButton, gbc);

		listButton.addActionListener(e -> {
			mainFrame.changeCard(4, 4);
		});

		/*
		 * // Log out JPanel logOutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,
		 * 0)); logOutPanel.setBackground(null); gbc.gridy = row++;
		 * userInformationPanel.add(logOutPanel, gbc);
		 * 
		 * ImageIcon logOutIcon = Utils.createImageIcon("/icons/logout_icon_56.png",
		 * "Log Out Icon"); JButton logOutButton = Utils.createIconButton(logOutIcon);
		 * logOutPanel.add(logOutButton);
		 * 
		 * JLabel logOutLabel = createPageLabel("Log Out", Font.PLAIN, fontSize);
		 * logOutLabel.setForeground(new Color(178, 59, 59));
		 * logOutPanel.add(logOutLabel);
		 * 
		 * logOutButton.addActionListener(e -> { // TODO: Log Out });
		 */

		createGridBagBottomGlue(userInformationPanel, row);

		return userInformationPanel;
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridy = 0;

		// Title
		JLabel titleLabel = createPageLabel("ผู้ใช้", Font.BOLD, 28);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		topPanel.add(titleLabel, gbc);

		/*
		 * // User Icon ImageIcon userIcon =
		 * Utils.createImageIcon("/icons/user_icon_120.png", "User Icon"); JLabel
		 * userIconLabel = new JLabel(userIcon); gbc.gridx = 1; gbc.anchor =
		 * GridBagConstraints.NORTHEAST; topPanel.add(userIconLabel, gbc);
		 */

		return topPanel;
	}

	private void createGridBagBottomGlue(JPanel gridBagPanel, int row) {
		// make components go from top to bottom
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridy = row;
		gbc.weighty = 1.0;
		gbc.gridx = 0;

		gridBagPanel.add(Box.createGlue(), gbc);
	}

	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15, 0, 15, 0);

		return gbc;
	}

	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setOpaque(true);

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		return createPageLabel("", fontStyle, fontSize);
	}

}
