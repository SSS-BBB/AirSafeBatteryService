package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import Struct.CheckHistory;
import Struct.DepositedPowerBank;
import Struct.RentedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserHistoryDetail extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	// North Components
	private JLabel typeIconLabel, typeTitleLabel, transactionDateLabel;
	private JLabel statusIconLabel, statusLabel;
	
	// Detail Components (CENTER)
	private JLabel detailTitleLabel;
	private JLabel[] columnNameLabel, detailValueLabel;

	public UserHistoryDetail(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;

		setBackground(backgroundColor);

		// Top Panel
		JPanel topPanel = createTopPanel();
		this.add(topPanel, BorderLayout.NORTH);
		
		// Center Panel
		JPanel detailPanel = createDetailPanel();
		this.add(detailPanel, BorderLayout.CENTER);

	}
	
	private JPanel createDetailPanel() {
		JPanel detailPanel = new JPanel();
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
		detailPanel.setBackground(null);
		detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		int fontSize = 18;
		int margin = 10;
		
		// Detail Title
		detailTitleLabel = createPageLabel(Font.BOLD, fontSize);
		detailPanel.add(detailTitleLabel);
		detailPanel.add(Box.createVerticalStrut(20));
		
		// add history content
		// 6 columns
		int columnSize = 6;
		
		JPanel historyDetailPanel = new JPanel(new GridBagLayout());
		historyDetailPanel.setBackground(null);
		historyDetailPanel.setAlignmentX(LEFT_ALIGNMENT);
		detailPanel.add(historyDetailPanel);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(margin / 2, margin / 2, margin / 2, margin / 2);
		
		// Column Name
		gbc.gridy = 0;
		columnNameLabel = new JLabel[columnSize];
		for (int i = 0; i < columnSize; i++) {
			columnNameLabel[i] = createPageLabel("a", Font.BOLD, fontSize);
			gbc.gridx = i;
			historyDetailPanel.add(columnNameLabel[i], gbc);
		}
		
		// Detail Value
		gbc.gridy = 1;
		detailValueLabel = new JLabel[columnSize];
		for (int i = 0; i < columnSize; i++) {
			detailValueLabel[i] = createPageLabel("a", Font.PLAIN, fontSize);
			gbc.gridx = i;
			historyDetailPanel.add(detailValueLabel[i], gbc);
		}
		
		
		historyDetailPanel.setMaximumSize(new Dimension(
				historyDetailPanel.getMaximumSize().width,
				historyDetailPanel.getPreferredSize().height
		));
		
		
		return detailPanel;
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		int headingFontSize = 24;
		int bodyFontSize = 18;
		int margin = 10;
		
		// Title
		JLabel titleLabel = createPageLabel("ประวัติการใช้งาน", Font.BOLD, 28);
		topPanel.add(titleLabel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		// Back Button
		ImageIcon backIcon = Utils.createImageIcon("/icons/back_icon_36.png", "Back Icon");
		JButton backButton = Utils.createIconButton(backIcon);
		topPanel.add(backButton);
		topPanel.add(Box.createVerticalStrut(margin));
		
		backButton.addActionListener(e -> {
			mainFrame.changeCard(5, 5);
		});
		
		// Use Detail
		int useDetailPadding = 5;
		JLabel useDetailLabel = createPageLabel("รายละเอียดการใช้งาน", Font.BOLD, headingFontSize);
		useDetailLabel.setForeground(Color.WHITE);
		useDetailLabel.setBackground(mainColor);
		useDetailLabel.setBorder(BorderFactory.createEmptyBorder(useDetailPadding, useDetailPadding, useDetailPadding, useDetailPadding));
		topPanel.add(useDetailLabel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		// History Type and Status
		JPanel typeStatusPanel = new JPanel(new BorderLayout());
		typeStatusPanel.setBackground(null);
		typeStatusPanel.setAlignmentX(LEFT_ALIGNMENT);
		topPanel.add(typeStatusPanel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		int detailMargin = 5;
		
		// History Type (check, rent, deposit)
		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, detailMargin, 0));
		typePanel.setBackground(null);
		typeStatusPanel.add(typePanel, BorderLayout.WEST);

		typeIconLabel = new JLabel();
		typePanel.add(typeIconLabel);
		
		JPanel typeDetailPanel = new JPanel();
		typeDetailPanel.setLayout(new BoxLayout(typeDetailPanel, BoxLayout.Y_AXIS));
		typeDetailPanel.setBackground(null);
		typePanel.add(typeDetailPanel);
		
		typeTitleLabel = createPageLabel(Font.BOLD, bodyFontSize);
		typeTitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		typeDetailPanel.add(typeTitleLabel);
		typeDetailPanel.add(Box.createVerticalStrut(detailMargin));
		
		transactionDateLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		transactionDateLabel.setAlignmentX(CENTER_ALIGNMENT);
		typeDetailPanel.add(transactionDateLabel);
		
		// Status
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, detailMargin, 0));
		statusPanel.setBackground(null);
		typeStatusPanel.add(statusPanel, BorderLayout.CENTER);
		
		statusPanel.add(Box.createHorizontalStrut(50));
		
		statusIconLabel = new JLabel();
		statusPanel.add(statusIconLabel);
		
		statusLabel = createPageLabel(Font.BOLD, headingFontSize);
		statusPanel.add(statusLabel);
		
		// Line separator
		JSeparator lineSeparator = createPageSeparator();
		topPanel.add(lineSeparator);
		
		return topPanel;
	}
	
	public void updatePage(DepositedPowerBank powerBank) {
		resetPage();
		
		// update top panel
		String imagePath = "/icons/download_icon_56.png";
		ImageIcon depositIcon = Utils.createImageIcon(imagePath, "Deposit Icon");
		typeIconLabel.setIcon(depositIcon);
		
		typeTitleLabel.setText("ฝาก Power Bank");
		transactionDateLabel.setText(Utils.getDateFormat(powerBank.transactionTime.toLocalDate(), "dd/MM/yyyy"));
		
		String statusText = powerBank.depositStatus;
		Color statusColor = new Color(128, 0, 128);
		statusLabel.setText(statusText);
		statusLabel.setForeground(statusColor);
		
		// update center panel
		detailTitleLabel.setText("รายละเอียดการฝาก");
		
		if (columnNameLabel.length >= 6) {
			columnNameLabel[0].setText("สถานที่ฝาก");
			columnNameLabel[1].setText("หมายเลข Locker");
			columnNameLabel[2].setText("Power Bank");
			columnNameLabel[3].setText("วันที่ฝาก");
			columnNameLabel[4].setText("วันที่รับคืน");
			columnNameLabel[5].setText("สถานะ");
		}
		else {
			System.err.println("Column Name Label is too small. Unable to create column name label.");
		}
		
		if (detailValueLabel.length >= 6) {
			detailValueLabel[0].setText(powerBank.address);
			detailValueLabel[1].setText(String.valueOf(powerBank.lockerNumber));
			detailValueLabel[2].setText(powerBank.deviceInfo.brand + " " + powerBank.deviceInfo.model);
			detailValueLabel[3].setText(Utils.getDateFormat(powerBank.depositDate, "dd/MM/yyyy"));
			detailValueLabel[4].setText(Utils.getDateFormat(powerBank.returnDate, "dd/MM/yyyy"));
			detailValueLabel[4].setForeground(Color.BLACK);
			detailValueLabel[5].setText(statusText);
			detailValueLabel[5].setForeground(statusColor);
		}
		else {
			System.err.println("Detail Value Label is too small. Unable to create detail value label.");
		}
	}
	
	public void updatePage(RentedPowerBank powerBank) {
		resetPage();
		
		// update top panel
		String imagePath = "/icons/package_icon_56.png";
		ImageIcon rentIcon = Utils.createImageIcon(imagePath, "Rent Icon");
		typeIconLabel.setIcon(rentIcon);
		
		typeTitleLabel.setText("เช่า Power Bank");
		transactionDateLabel.setText(Utils.getDateFormat(powerBank.transactionTime.toLocalDate(), "dd/MM/yyyy"));
		
		String statusText = powerBank.status;
		Color statusColor = new Color(255, 165, 0);
		statusLabel.setText(statusText);
		statusLabel.setForeground(statusColor);
		
		// update center panel
		detailTitleLabel.setText("รายละเอียดการเช่า");
		
		if (columnNameLabel.length >= 6) {
			columnNameLabel[0].setText("สถานที่รับเครื่อง");
			columnNameLabel[1].setText("สถานที่คืนเครื่อง");
			columnNameLabel[2].setText("Power Bank");
			columnNameLabel[3].setText("วันที่เช่า");
			columnNameLabel[4].setText("วันที่คืน");
			columnNameLabel[5].setText("สถานะ");
		}
		else {
			System.err.println("Column Name Label is too small. Unable to create column name label.");
		}
		
		if (detailValueLabel.length >= 6) {
			detailValueLabel[0].setText(powerBank.pickUpAddress);
			detailValueLabel[1].setText(powerBank.returnAddress);
			detailValueLabel[2].setText(powerBank.deviceInfo.brand + " " + powerBank.deviceInfo.model);
			detailValueLabel[3].setText(Utils.getDateFormat(powerBank.rentDate, "dd/MM/yyyy"));
			detailValueLabel[4].setText(Utils.getDateFormat(powerBank.returnDate, "dd/MM/yyyy"));
			detailValueLabel[4].setForeground(Color.BLACK);
			detailValueLabel[5].setText(statusText);
			detailValueLabel[5].setForeground(statusColor);
		}
		else {
			System.err.println("Detail Value Label is too small. Unable to create detail value label.");
		}
	}
	
	public void updatePage(CheckHistory checkHistory) {
		resetPage();
		
		// update top panel
		String imagePath = (checkHistory.approve) ? "/icons/search_icon_56.png" : "/icons/warning_icon_56.png";
		ImageIcon checkIcon = Utils.createImageIcon(imagePath, "Check Icon");
		typeIconLabel.setIcon(checkIcon);
		
		typeTitleLabel.setText("ตรวจสอบ Power Bank");
		transactionDateLabel.setText(Utils.getDateFormat(checkHistory.checkTime.toLocalDate(), "dd/MM/yyyy"));
		
		String statusImagePath = (checkHistory.approve) ? "/icons/check_icon_56.png" : "/icons/cancel_icon_56.png";
		ImageIcon statusIcon = Utils.createImageIcon(statusImagePath, "Status Icon");
		statusIconLabel.setIcon(statusIcon);
		
		String statusText = (checkHistory.approve) ? "ตรวจสอบแล้วผ่าน" : "ตรวจสอบแล้วไม่ผ่าน";
		Color statusColor = (checkHistory.approve) ? new Color(45, 172, 51) : new Color(255, 51, 51);
		statusLabel.setText(statusText);
		statusLabel.setForeground(statusColor);
		
		// update center panel
		detailTitleLabel.setText("ข้อมูลการตรวจสอบ Power Bank");
		
		if (columnNameLabel.length >= 6) {
			columnNameLabel[0].setText("เวลาที่ตรวจสอบ");
			columnNameLabel[1].setText("Capacity");
			columnNameLabel[2].setText("Wh สูงสุดไม่เกิน");
			columnNameLabel[3].setText("Wh");
			columnNameLabel[4].setText("ผลการตรวจสอบ");
			columnNameLabel[5].setText("กฎมีผลถึงวันที่");
		}
		else {
			System.err.println("Column Name Label is too small. Unable to create column name label.");
		}
		
		if (detailValueLabel.length >= 6) {
			detailValueLabel[0].setText(Utils.getTimeFormat(checkHistory.checkTime.toLocalTime(), "HH:mm:ss"));
			
			String capacityText = (checkHistory.capacity < 0) ? "-" : String.valueOf(checkHistory.capacity);
			detailValueLabel[1].setText(capacityText);
			detailValueLabel[2].setText(String.valueOf(checkHistory.maxWh));
			detailValueLabel[3].setText(String.valueOf(checkHistory.wh));
			detailValueLabel[4].setText((checkHistory.approve) ? "ผ่าน" : "ไม่ผ่าน");
			detailValueLabel[4].setForeground(statusColor);
			detailValueLabel[5].setText(Utils.getDateFormat(checkHistory.endDate, "dd/MM/yyyy"));
			detailValueLabel[5].setForeground(Color.BLACK);
		}
		else {
			System.err.println("Detail Value Label is too small. Unable to create detail value label.");
		}
	}
	
	private void resetPage() {
		statusIconLabel.setIcon(null);
		
		if (detailValueLabel.length >= 6) {
			detailValueLabel[4].setForeground(Color.BLACK);
			detailValueLabel[5].setForeground(Color.BLACK);
		}
		 
		
		/*
		// North Components
		typeIconLabel, typeTitleLabel, transactionDateLabel
		statusIconLabel, statusLabel
		
		// Detail Components (CENTER)
		JLabel detailTitleLabel
		JLabel[] columnNameLabel, detailValueLabel
		*/
	}
	
	private void createPageSeparator(JPanel gridBagContainerPanel, int row, int columnSpan) {
		JSeparator separator = createPageSeparator();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = columnSpan;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
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
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		pageLabel.setOpaque(true);

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		return createPageLabel("", fontStyle, fontSize);
	}

}
