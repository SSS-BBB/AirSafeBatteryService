package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.CustomScrollPane;
import CustomGUI.NameDropDownHolder;
import Database.DatabaseConnector;
import Struct.DepositedPowerBank;
import Struct.PowerBank;
import Struct.RentedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserList extends JPanel {

	private class PowerBankDisplay {
		// either rented or deposited power bank can have value. both of them can't have
		// value at the same object.
		public RentedPowerBank rentedPowerBank;
		public DepositedPowerBank depositedPowerBank;

		public String type;
		public String status;
		public String outboundAddress;
		public String returnAddress;
		public Calendar outBoundDate;
		public Calendar returnDate;

		public PowerBankDisplay(RentedPowerBank rentedPowerBank) {
			depositedPowerBank = null;
			this.rentedPowerBank = rentedPowerBank;

			type = "การเช่า";
			status = rentedPowerBank.status;
			outboundAddress = rentedPowerBank.pickUpAddress;
			returnAddress = rentedPowerBank.returnAddress;
			outBoundDate = rentedPowerBank.rentDate;
			returnDate = rentedPowerBank.returnDate;
		}

		public PowerBankDisplay(DepositedPowerBank depositedPowerBank) {
			rentedPowerBank = null;
			this.depositedPowerBank = depositedPowerBank;

			type = "การฝาก";
			status = depositedPowerBank.depositStatus;
			outboundAddress = depositedPowerBank.address;
			returnAddress = depositedPowerBank.address;
			outBoundDate = depositedPowerBank.depositDate;
			returnDate = depositedPowerBank.returnDate;
		}

		public PowerBank getDeviceInfo() {
			if (rentedPowerBank != null)
				return rentedPowerBank.deviceInfo;
			else if (depositedPowerBank != null)
				return depositedPowerBank.deviceInfo;
			return null;
		}

		public Color getStatusColor() {
			if (depositedPowerBank != null) {
				switch (status) {
				case "จองที่ฝากแล้ว":
					return Color.GRAY;
				case "กำลังฝาก":
					return new Color(0, 100, 0);
				default:
					return Color.BLACK;
				}
			}

			else if (rentedPowerBank != null) {
				switch (status) {
				case "ยังไม่รับ":
					return Color.GRAY;
				case "สามารถใช้งานได้":
					return new Color(0, 100, 0);
				default:
					return Color.BLACK;
				}
			}

			return Color.BLACK;
		}
	}

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	private ArrayList<PowerBankDisplay> powerBankDisplayList;

	// Filter components
	private ImageIcon unCheckedIcon, checkedIcon;
	private NameDropDownHolder typeDropDownHolder, orderByDropDownHolder;
	private JButton pastDueButton, ascButton, descButton;

	private boolean showPastDue;
	private boolean orderAsc, orderDesc;

	// Power Bank Display components
	private JPanel listDisplayPanel;

	public UserList(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor,
			DepositedPowerBank depositedPowerBank) {
		this(mainFrame, backgroundColor, mainColor, menuColor);
		powerBankDisplayList.add(new PowerBankDisplay(depositedPowerBank));
		updateListDisplayPanel();
	}

	public UserList(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		this.powerBankDisplayList = new ArrayList<PowerBankDisplay>();

		setBackground(backgroundColor);

		// Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(topPanel, BorderLayout.NORTH);

		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setText("รายการของฉัน");
		topPanel.add(titleLabel);

		topPanel.add(Box.createVerticalStrut(10));

		// Filter Panel
		JPanel filterPanel = createFilterPanel();
		topPanel.add(filterPanel);

		// List Display Panel
		JPanel listDisplayPanel = createListDisplayPanel();
		CustomScrollPane listScrollPane = new CustomScrollPane(listDisplayPanel, menuColor, backgroundColor);
		this.add(listScrollPane, BorderLayout.CENTER);
		
		updateListDisplayPanel();
	}

	public void updateListDisplayPanel() {
		powerBankDisplayList.clear();
		
		// get data from rented power bank and deposited power bank table
		ArrayList<RentedPowerBank> rentedPowerBankList = DatabaseConnector.getRentedPowerBank(mainFrame.userDetail.userID);
		for (RentedPowerBank powerBank : rentedPowerBankList)
			powerBankDisplayList.add(new PowerBankDisplay(powerBank));
		
		ArrayList<DepositedPowerBank> depositedPowerBankList = DatabaseConnector.getDepositedPowerBank(mainFrame.userDetail.userID);
		for (DepositedPowerBank powerBank : depositedPowerBankList)
			powerBankDisplayList.add(new PowerBankDisplay(powerBank));
		
		listDisplayPanel.removeAll();

		for (PowerBankDisplay powerBank : powerBankDisplayList)
			listDisplayPanel.add(createPowerBankDisplay(powerBank));
	}

	private JPanel createListDisplayPanel() {
		listDisplayPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		listDisplayPanel.setBackground(backgroundColor);
		listDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		return listDisplayPanel;
	}

	private JPanel createPowerBankDisplay(PowerBankDisplay powerBank) {
		JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		wrapperPanel.setBackground(null);

		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		displayPanel.setBackground(null);
		displayPanel.setBorder(Utils.createPaddingBorder(Color.BLACK, 8));
		wrapperPanel.add(displayPanel);

		int margin = 5;
		int fontSize = 15;
		float componentAlignment = CENTER_ALIGNMENT;

		PowerBank deviceInfo = powerBank.getDeviceInfo();

		// Power Bank Image
		ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(deviceInfo.image, 120, 120);
		JLabel powerBankImageLabel = new JLabel(scaledPowerBankImage);
		powerBankImageLabel.setAlignmentX(componentAlignment);
		displayPanel.add(powerBankImageLabel);

		displayPanel.add(Box.createVerticalStrut(margin));

		// Brand
		JLabel brandLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		brandLabel.setText(deviceInfo.brand);
		displayPanel.add(brandLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// Name
		JLabel nameLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		nameLabel.setText(deviceInfo.name);
		displayPanel.add(nameLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// Model
		JLabel modelLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		modelLabel.setText(deviceInfo.model);
		displayPanel.add(modelLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// add display data
		// Type
		String type = powerBank.type;
		JLabel typeLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		typeLabel.setText(type);
		Color typeColor = (type.equals("การเช่า")) ? new Color(10, 34, 71) : new Color(74, 64, 47);
		typeLabel.setForeground(typeColor);
		displayPanel.add(typeLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// Status
		String status = powerBank.status;
		JLabel statusLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		statusLabel.setText(status);
		statusLabel.setForeground(powerBank.getStatusColor());
		displayPanel.add(statusLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// OutBound Address
		JLabel outBoundAddressLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		outBoundAddressLabel.setText("สถานที่ขาไป: " + powerBank.outboundAddress);
		displayPanel.add(outBoundAddressLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// Return Address
		JLabel returnAddressLabel = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		returnAddressLabel.setText("สถานที่ขากลับ: " + powerBank.returnAddress);
		displayPanel.add(returnAddressLabel);
		displayPanel.add(Box.createVerticalStrut(margin));

		// OutBound Date
		JLabel outBoundDate = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		outBoundDate.setText("วันที่ขาไป: " + Utils.getCalendarDateFormat(powerBank.outBoundDate, "dd/MM/yyyy"));
		displayPanel.add(outBoundDate);
		displayPanel.add(Box.createVerticalStrut(margin));

		// Return Date
		JLabel returnDate = createPageLabel(Font.BOLD, fontSize, componentAlignment);
		returnDate.setText("วันที่ขากลับ: " + Utils.getCalendarDateFormat(powerBank.returnDate, "dd/MM/yyyy"));
		displayPanel.add(returnDate);
		displayPanel.add(Box.createVerticalStrut(margin));
		
		// Detail Button
		JButton detailButton = Utils.createNoBackgroundButton("ดูรายละเอียด", new Color(36, 160, 237), fontSize);
		detailButton.setAlignmentX(componentAlignment);
		displayPanel.add(detailButton);
		displayPanel.add(Box.createVerticalStrut(margin));

		return wrapperPanel;
	}

	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel(new GridBagLayout());
		filterPanel.setAlignmentX(LEFT_ALIGNMENT);
		filterPanel.setAlignmentY(TOP_ALIGNMENT);
		filterPanel.setBackground(null);

		int fontSize = 16;
		int margin = 10;

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(margin, margin, margin, margin);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		// Type (Rented or Deposited)
		String[] typeList = new String[] { "ทั้งหมด", "การเช่า", "การฝาก" };
		typeDropDownHolder = new NameDropDownHolder(typeList, "ประเภท:", fontSize, fontSize, backgroundColor,
				mainColor);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		filterPanel.add(typeDropDownHolder, gbc);

		// Show Past Due
		JPanel pastDuePanel = createTextWithComponentToAdd("เลยกำหนด", fontSize);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 2.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		filterPanel.add(pastDuePanel, gbc);

		checkedIcon = Utils.createImageIcon("/icons/checked_icon_24.png", "Checked Icon");
		unCheckedIcon = Utils.createImageIcon("/icons/uncheck_icon_24.png", "Uncheck Icon");
		pastDueButton = Utils.createIconButton(unCheckedIcon);
		pastDuePanel.add(pastDueButton);

		showPastDue = false;
		pastDueButton.addActionListener(e -> {
			showPastDue = !showPastDue;
			if (showPastDue)
				pastDueButton.setIcon(checkedIcon);
			else
				pastDueButton.setIcon(unCheckedIcon);
		});

		// Order By
		String[] orderByList = new String[] { "ไม่มี", "ประเภท", "สถานะ", "สถานที่ขาไป", "สถานที่ขากลับ", "วันที่ขาไป",
				"วันที่ขากลับ" };
		orderByDropDownHolder = new NameDropDownHolder(orderByList, "จัดเรียงตาม:", fontSize, fontSize, backgroundColor,
				mainColor);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		filterPanel.add(orderByDropDownHolder, gbc);

		// Order type
		JPanel ordertypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		ordertypePanel.setBackground(backgroundColor);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		filterPanel.add(ordertypePanel, gbc);

		ascButton = Utils.createBorderButton("น้อยไปมาก", Color.BLACK, Color.BLACK, fontSize);
		ordertypePanel.add(ascButton);

		descButton = Utils.createBorderButton("มากไปน้อย", Color.BLACK, Color.BLACK, fontSize);
		ordertypePanel.add(descButton);

		ascButton.addActionListener(e -> {
			ascButton.setBackground(mainColor);
			descButton.setBackground(null);

			ascButton.setForeground(Color.WHITE);
			descButton.setForeground(Color.BLACK);

			orderAsc = true;
			orderDesc = false;
		});

		descButton.addActionListener(e -> {
			descButton.setBackground(mainColor);
			ascButton.setBackground(null);

			descButton.setForeground(Color.WHITE);
			ascButton.setForeground(Color.BLACK);

			orderAsc = false;
			orderDesc = true;
		});

		// Apply Filter
		JButton applyFilterButton = Utils.createNoBackgroundButton("ยืนยัน", mainColor);
		applyFilterButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		gbc.gridx = 2;
		gbc.gridy = 1;
		filterPanel.add(applyFilterButton, gbc);

		applyFilterButton.addActionListener(e -> {
			applyFilter();
		});

		// Clear Filter
		JButton clearFilterButton = Utils.createNoBackgroundButton("ล้าง filter", new Color(36, 160, 237));
		clearFilterButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		filterPanel.add(clearFilterButton, gbc);

		clearFilterButton.addActionListener(e -> {
			clearFilter();
			applyFilter();
		});

		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.weightx = 2.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		filterPanel.add(Box.createHorizontalGlue(), gbc);

		return filterPanel;
	}

	private void clearFilter() {
		// Clear Filter

		// Clear Type
		typeDropDownHolder.setSelectedValue(0);

		// Clear Past Due Date
		showPastDue = false;
		pastDueButton.setIcon(unCheckedIcon);

		// Clear Order By
		orderByDropDownHolder.setSelectedValue(0);

		ascButton.setBackground(null);
		descButton.setBackground(null);

		ascButton.setForeground(Color.BLACK);
		descButton.setForeground(Color.BLACK);

		orderAsc = false;
		orderDesc = false;
	}

	private void applyFilter() {

	}

	private JLabel createPageLabel(int fontStyle, int fontSize, float alignment) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(alignment);

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		// pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

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