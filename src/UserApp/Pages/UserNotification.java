package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import CustomGUI.CustomScrollPane;
import Database.DataContainer;
import Struct.DepositedPowerBank;
import Struct.PowerBank;
import Struct.RentedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserNotification extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	private JPanel contentPanel;

	// Notification List
	private ArrayList<DepositedPowerBank> depositList;
	private ArrayList<RentedPowerBank> rentList;

	public UserNotification(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;

		setBackground(backgroundColor);

		// Title
		JLabel titleLabel = createPageLabel("แจ้งเตือน", Font.BOLD, 28);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(titleLabel, BorderLayout.NORTH);

		// Notification Content
		contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(backgroundColor);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		CustomScrollPane historyScrollPane = new CustomScrollPane(contentPanel, menuColor, backgroundColor);
		this.add(historyScrollPane, BorderLayout.CENTER);

		int row = 0;
		createNotificationTitle(row++);
		createPageSeparator(contentPanel, row++, 4);

		row = createNotificationDetail(row);

		createGridBagBottomGlue(row++);
	}
	
	public void refreshPage() {
		if (contentPanel == null) {
			System.err.println("Null content panel. Unable to refresh notification page.");
			return;
		}
		
		contentPanel.removeAll();
		int row = 0;
		createNotificationTitle(row++);
		createPageSeparator(contentPanel, row++, 4);

		row = createNotificationDetail(row);

		createGridBagBottomGlue(row++);
		
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	private int createNotificationDetail(int startingRow) {
		// depositList = DatabaseConnector.getDepositedPowerBank(mainFrame.userDetail.userID, false, true);
		// rentList = DatabaseConnector.getRentedPowerBank(mainFrame.userDetail.userID, false, true);
		
		depositList = DataContainer.getInProgressDepositList();
		rentList = DataContainer.getInProgressRentList();
		
		return updateNotificationDisplay(startingRow);
	}

	private int updateNotificationDisplay(int startingRow) {
		if (contentPanel == null) {
			System.err.println("Null content panel. Unable to update notification display.");
			return startingRow;
		}

		if (rentList == null || depositList == null) {
			System.err.println("Null notification list. Unable to update notification display.");
			return startingRow;
		}

		int currentRow = startingRow;

		for (RentedPowerBank powerBank : rentList) {
			addNotificationList(currentRow++, powerBank.deviceInfo, "การเช่า", powerBank.returnDate);
		}
		
		for (DepositedPowerBank powerBank : depositList) {
			addNotificationList(currentRow++, powerBank.deviceInfo, "การฝาก", powerBank.returnDate);
		}

		return currentRow;
	}

	private void addNotificationList(int row, PowerBank deviceInfo, String type, LocalDate dueDate) {
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridy = row;

		// Image
		ImageIcon powerBankIcon = Utils.scaleImageKeepRatio(deviceInfo.image, 100, 100);
		JLabel imageLabel = new JLabel(powerBankIcon);
		gbc.gridx = 0;
		contentPanel.add(imageLabel, gbc);

		int fontSize = 18;

		// Power Bank
		int nameMargin = 5;
		JPanel powerBankPanel = new JPanel();
		powerBankPanel.setLayout(new BoxLayout(powerBankPanel, BoxLayout.Y_AXIS));
		powerBankPanel.setBackground(null);
		gbc.gridx = 1;
		contentPanel.add(powerBankPanel, gbc);

		// Brand
		JLabel brandLabel = createPageLabel(deviceInfo.brand, Font.PLAIN, fontSize);
		powerBankPanel.add(brandLabel);
		powerBankPanel.add(Box.createVerticalStrut(nameMargin));

		// Name
		JLabel nameLabel = createPageLabel(deviceInfo.name, Font.PLAIN, fontSize);
		powerBankPanel.add(nameLabel);
		powerBankPanel.add(Box.createVerticalStrut(nameMargin));

		// Model
		JLabel modelLabel = createPageLabel(deviceInfo.model, Font.PLAIN, fontSize);
		powerBankPanel.add(modelLabel);
		powerBankPanel.add(Box.createVerticalStrut(nameMargin));
		
		// Type
		JLabel typeLabel = createPageLabel(type, Font.PLAIN, fontSize);
		gbc.gridx = 2;
		contentPanel.add(typeLabel, gbc);
		
		// Due
		int dayLeft = Utils.countDays(LocalDate.now(), dueDate);
		dayLeft = (dayLeft < 0) ? 0 : dayLeft;
		JLabel dueLabel = createPageLabel(String.valueOf(dayLeft) + " วัน", Font.PLAIN, fontSize);
		gbc.gridx = 3;
		contentPanel.add(dueLabel, gbc);
	}

	private void createNotificationTitle(int row) {
		// Image, Power Bank, Type, Days Left
		int fontSize = 20;

		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridy = row;

		// Image
		JLabel imageTitleLabel = createPageLabel("รูปภาพ", Font.BOLD, fontSize);
		gbc.gridx = 0;
		contentPanel.add(imageTitleLabel, gbc);

		// Power Bank
		JLabel powerBankTitleLabel = createPageLabel("Power Bank", Font.BOLD, fontSize);
		gbc.gridx = 1;
		contentPanel.add(powerBankTitleLabel, gbc);

		// Type
		JLabel typeTitleLabel = createPageLabel("ประเภท", Font.BOLD, fontSize);
		gbc.gridx = 2;
		contentPanel.add(typeTitleLabel, gbc);

		// Due
		JLabel dueTitleLabel = createPageLabel("ครบกำหนดในอีก", Font.BOLD, fontSize);
		gbc.gridx = 3;
		contentPanel.add(dueTitleLabel, gbc);
	}

	private void createGridBagBottomGlue(int row) {
		// make components go from top to bottom
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridy = row;
		gbc.weighty = 1.0;
		gbc.gridx = 0;

		contentPanel.add(Box.createGlue(), gbc);
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

	private void createPageSeparator(JPanel gridBagContainerPanel, int row, int columnSpan) {
		JSeparator separator = createPageSeparator();

		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = columnSpan;

		gridBagContainerPanel.add(separator, gbc);
	}

	private JSeparator createPageSeparator() {
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.HORIZONTAL);
		separator.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		return separator;
	}

	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		return createPageLabel("", fontStyle, fontSize);
	}

}
