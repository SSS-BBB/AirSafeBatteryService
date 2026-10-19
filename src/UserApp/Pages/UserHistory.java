package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import CustomGUI.CustomScrollPane;
import Database.DatabaseConnector;
import Struct.CheckHistory;
import Struct.DepositedPowerBank;
import Struct.RentedPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserHistory extends JPanel {

	private class HistoryDisplay {
		public LocalDateTime transactionDateTime;
		public CheckHistory checkHistory;
		public RentedPowerBank rentedPowerBank;
		public DepositedPowerBank depositedPowerBank;

		public HistoryDisplay(CheckHistory checkHistory) {
			this.checkHistory = checkHistory;
			this.rentedPowerBank = null;
			this.depositedPowerBank = null;

			this.transactionDateTime = checkHistory.checkTime;
		}

		public HistoryDisplay(RentedPowerBank rentedPowerBank) {
			this.checkHistory = null;
			this.rentedPowerBank = rentedPowerBank;
			this.depositedPowerBank = null;

			this.transactionDateTime = rentedPowerBank.transactionTime;
		}

		public HistoryDisplay(DepositedPowerBank depositedPowerBank) {
			this.checkHistory = null;
			this.rentedPowerBank = null;
			this.depositedPowerBank = depositedPowerBank;

			this.transactionDateTime = depositedPowerBank.transactionTime;
		}
	}

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	private UserHistoryDetail detailPage;

	// Filter components
	private JButton allFilterButton, rentFilterButton, depositFilterButton, checkFilterButton;

	// History list
	private JPanel historyContentPanel;

	private ArrayList<CheckHistory> checkHistoryList;
	private ArrayList<DepositedPowerBank> depositList;
	private ArrayList<RentedPowerBank> rentList;
	private ArrayList<HistoryDisplay> historyDisplayList;

	public UserHistory(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor,
			UserHistoryDetail detailPage) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		this.detailPage = detailPage;

		setBackground(backgroundColor);

		// Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(topPanel, BorderLayout.NORTH);

		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setText("ประวัติการใช้งาน");
		topPanel.add(titleLabel);
		topPanel.add(Box.createVerticalStrut(10));

		// Filter Panel
		JPanel filterPanel = createFilterPanel();
		topPanel.add(filterPanel);
		topPanel.add(Box.createVerticalStrut(3));

		// Separator to separate filter and history content
		JSeparator filterSeparator = new JSeparator();
		filterSeparator.setOrientation(SwingConstants.HORIZONTAL);
		filterSeparator.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		topPanel.add(filterSeparator);

		// History content
		createHistoryContentPanel();
		CustomScrollPane historyScrollPane = new CustomScrollPane(historyContentPanel, menuColor, backgroundColor);
		this.add(historyScrollPane, BorderLayout.CENTER);
	}

	public void refreshPage() {
		if (historyDisplayList == null) {
			System.err.println("Null history display list. Unable to refresh history page.");
			return;
		}

		// get history data
		checkHistoryList = DatabaseConnector.getCheckHistory(mainFrame.userDetail.userID);

		// get rented and deposited data
		depositList = DatabaseConnector.getDepositedPowerBank(mainFrame.userDetail.userID, false);
		rentList = DatabaseConnector.getRentedPowerBank(mainFrame.userDetail.userID, false);
		
		historyDisplayList.clear();
		addAndSortHistoryDisplayList();
		
		addHistoryList("all");
	}
	
	private void addAndSortHistoryDisplayList() {
		if (checkHistoryList == null || rentList == null || depositList == null) {
			System.err.println("Null history list. Unable to add data to history display list.");
			return;
		}
		
		if (historyDisplayList == null) {
			System.err.println("Null history display list. Unable to add data to history display list.");
			return;
		}
		
		for (CheckHistory checkHistory : checkHistoryList) {
			addAndSortHistoryDisplayList(new HistoryDisplay(checkHistory));
		}
		
		for (RentedPowerBank powerBank : rentList) {
			addAndSortHistoryDisplayList(new HistoryDisplay(powerBank));
		}
		
		for (DepositedPowerBank powerBank : depositList) {
			addAndSortHistoryDisplayList(new HistoryDisplay(powerBank));
		}
	}
	
	private void addAndSortHistoryDisplayList(HistoryDisplay history) {
		int indexToInsert = historyDisplayList.size();
		
		for (int i = 0; i < historyDisplayList.size(); i++) {
			HistoryDisplay compare = historyDisplayList.get(i);
			if (history.transactionDateTime.isAfter(compare.transactionDateTime)) {
				indexToInsert = i;
				break;
			}
		}
		
		historyDisplayList.add(indexToInsert, history);
	}
	
	private void addHistoryList(String filter) {
		if (checkHistoryList == null || rentList == null || depositList == null) {
			System.err.println("Null history list. Unable to update history page.");
			return;
		}

		if (historyContentPanel == null) {
			System.err.println("Null history content panel. Unable to update history page.");
			return;
		}

		if (historyDisplayList == null) {
			System.err.println("Null history display list. Unable to update history page.");
			return;
		}

		historyContentPanel.removeAll();

		// add data from history display list
		int row = 0;
		for (HistoryDisplay historyDisplay : historyDisplayList) {
			if (historyDisplay.checkHistory != null) {
				if (filter.equals("check") || filter.equals("all")) {
					createHistoryBox(historyContentPanel, row++, historyDisplay.checkHistory);
					createPageSeparator(historyContentPanel, row++, 5);
				}
			}
			else if (historyDisplay.rentedPowerBank != null) {
				if (filter.equals("rent") || filter.equals("all")) {
					createHistoryBox(historyContentPanel, row++, historyDisplay.rentedPowerBank);
					createPageSeparator(historyContentPanel, row++, 5);
				}
			}
			else if (historyDisplay.depositedPowerBank != null) {
				if (filter.equals("deposit") || filter.equals("all")) {
					createHistoryBox(historyContentPanel, row++, historyDisplay.depositedPowerBank);
					createPageSeparator(historyContentPanel, row++, 5);
				}
			}
		}
		
		// filter history list
		// filter values: all, rent, deposit, check
		
		/*
		if (filter.equals("check") || filter.equals("all")) {
			for (CheckHistory checkHistory : checkHistoryList) {
				createHistoryBox(historyContentPanel, row, checkHistory);
				row++;
				createPageSeparator(historyContentPanel, row, 5);
				row++;
			}
		}

		if (filter.equals("rent") || filter.equals("all")) {
			for (RentedPowerBank powerBank : rentList) {
				createHistoryBox(historyContentPanel, row, powerBank);
				row++;
				createPageSeparator(historyContentPanel, row, 5);
				row++;
			}
		}

		if (filter.equals("deposit") || filter.equals("all")) {
			for (DepositedPowerBank powerBank : depositList) {
				createHistoryBox(historyContentPanel, row, powerBank);
				row++;
				createPageSeparator(historyContentPanel, row, 5);
				row++;
			}
		}
		*/

		// make components go from top to bottom
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = row;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(15, 0, 15, 0);

		gbc.gridx = 0;
		historyContentPanel.add(Box.createGlue(), gbc);

		historyContentPanel.setMaximumSize(new Dimension(historyContentPanel.getMaximumSize().width,
				historyContentPanel.getPreferredSize().height));

		historyContentPanel.revalidate();
		historyContentPanel.repaint();
	}

	private void createHistoryContentPanel() {
		historyContentPanel = new JPanel(new GridBagLayout());
		historyContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		historyContentPanel.setAlignmentX(LEFT_ALIGNMENT);
		historyContentPanel.setBackground(backgroundColor);

		// get history data
		checkHistoryList = DatabaseConnector.getCheckHistory(mainFrame.userDetail.userID);

		// get rented and deposited data
		depositList = DatabaseConnector.getDepositedPowerBank(mainFrame.userDetail.userID, false);
		rentList = DatabaseConnector.getRentedPowerBank(mainFrame.userDetail.userID, false);

		// initialize history display list
		historyDisplayList = new ArrayList<HistoryDisplay>();
		addAndSortHistoryDisplayList();

		addHistoryList("all");
	}

	private void createHistoryBox(JPanel gridBagContainerPanel, int row, RentedPowerBank powerBank) {
		String imagePath = "/icons/package_icon_56.png";

		String[] c2Content = new String[3];
		c2Content[0] = "เช่า Power Bank";
		c2Content[1] = powerBank.deviceInfo.brand + " " + powerBank.deviceInfo.model + " ("
				+ String.valueOf(powerBank.deviceInfo.capacity) + " mAh)";
		c2Content[2] = "ระยะเวลาเช่า: " + Utils.getDateFormat(powerBank.rentDate, "dd/MM/yyyy") + " - "
				+ Utils.getDateFormat(powerBank.returnDate, "dd/MM/yyyy");

		LocalDate toShowDate = powerBank.transactionTime.toLocalDate();

		String status = powerBank.status;
		Color statusColor = new Color(255, 165, 0);

		ActionListener onNextClicked = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// go to rent history detail page
				detailPage.updatePage(powerBank);
				mainFrame.changeCard(5, 16);
			}

		};

		createHistoryBox(gridBagContainerPanel, row, imagePath, c2Content, toShowDate, status, statusColor,
				onNextClicked);

	}

	private void createHistoryBox(JPanel gridBagContainerPanel, int row, DepositedPowerBank powerBank) {
		String imagePath = "/icons/download_icon_56.png";

		String[] c2Content = new String[3];
		c2Content[0] = "ฝาก Power Bank";
		c2Content[1] = powerBank.address + " Locker: " + String.valueOf(powerBank.lockerNumber);
		c2Content[2] = "ระยะเวลาฝาก: " + Utils.getDateFormat(powerBank.depositDate, "dd/MM/yyyy") + " - "
				+ Utils.getDateFormat(powerBank.returnDate, "dd/MM/yyyy");

		LocalDate toShowDate = powerBank.transactionTime.toLocalDate();

		String status = powerBank.depositStatus;
		Color statusColor = new Color(128, 0, 128);

		ActionListener onNextClicked = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// go to deposit history detail page
				detailPage.updatePage(powerBank);
				mainFrame.changeCard(5, 16);
			}

		};

		createHistoryBox(gridBagContainerPanel, row, imagePath, c2Content, toShowDate, status, statusColor,
				onNextClicked);

	}

	private void createHistoryBox(JPanel gridBagContainerPanel, int row, CheckHistory checkHistory) {
		String imagePath = (checkHistory.approve) ? "/icons/search_icon_56.png" : "/icons/warning_icon_56.png";

		String[] c2Content = new String[3];
		c2Content[0] = "ตรวจสอบ Power Bank";
		c2Content[1] = String.valueOf(checkHistory.wh) + " wh";
		c2Content[2] = "ผลการตรวจสอบ: " + ((checkHistory.approve) ? "นำขึ้นเครื่องได้" : "ไม่สามารถนำขึ้นเครื่องได้");

		LocalDate toShowDate = checkHistory.checkTime.toLocalDate();

		String status = (checkHistory.approve) ? "ผ่าน" : "ไม่ผ่าน";
		Color statusColor = (checkHistory.approve) ? new Color(45, 172, 51) : new Color(255, 51, 51);

		ActionListener onNextClicked = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// go to check detail page
				detailPage.updatePage(checkHistory);
				mainFrame.changeCard(5, 16);
			}

		};

		createHistoryBox(gridBagContainerPanel, row, imagePath, c2Content, toShowDate, status, statusColor,
				onNextClicked);

	}

	private void createHistoryBox(JPanel gridBagContainerPanel, int row, String imagePath, String[] c2Content,
			LocalDate toShowDate, String status, Color statusColor, ActionListener onNextClicked) {

		int headingFontSize = 18;
		int bodyFontSize = 16;

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = row;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 0, 15, 0);

		// Image
		ImageIcon icon = Utils.createImageIcon(imagePath, "");
		JLabel iconLabel = new JLabel(icon);
		gbc.gridx = 0;
		gridBagContainerPanel.add(iconLabel, gbc);

		// Content for column 2
		JPanel c2Panel = new JPanel();
		c2Panel.setLayout(new BoxLayout(c2Panel, BoxLayout.Y_AXIS));
		c2Panel.setBackground(null);
		gbc.gridx = 1;
		gridBagContainerPanel.add(c2Panel, gbc);

		int c2Margin = 2;
		if (c2Content.length > 0) {
			// Title
			JLabel titleLabel = createPageLabel(c2Content[0], Font.BOLD, headingFontSize);
			titleLabel.setAlignmentX(CENTER_ALIGNMENT);
			c2Panel.add(titleLabel);
			c2Panel.add(Box.createVerticalStrut(c2Margin));
		}

		for (int i = 1; i < c2Content.length; i++) {
			JLabel detailLabel = createPageLabel(c2Content[i], Font.PLAIN, bodyFontSize);
			detailLabel.setAlignmentX(CENTER_ALIGNMENT);
			c2Panel.add(detailLabel);
			c2Panel.add(Box.createVerticalStrut(c2Margin));
		}

		// Date
		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
		datePanel.setBackground(null);
		gbc.gridx = 2;
		gridBagContainerPanel.add(datePanel, gbc);

		JLabel transactionLabel = createPageLabel("วันที่ทำรายการ", Font.BOLD, bodyFontSize);
		transactionLabel.setAlignmentX(CENTER_ALIGNMENT);
		datePanel.add(transactionLabel);
		datePanel.add(Box.createVerticalStrut(2));

		String dateText = Utils.getDateFormat(toShowDate, "dd/MM/yyyy");
		JLabel dateLabel = createPageLabel(dateText, Font.PLAIN, bodyFontSize);
		dateLabel.setAlignmentX(CENTER_ALIGNMENT);
		datePanel.add(dateLabel);

		// Status
		JLabel statusLabel = createPageLabel(status, Font.BOLD, headingFontSize);
		statusLabel.setForeground(statusColor);
		gbc.gridx = 3;
		gridBagContainerPanel.add(statusLabel, gbc);

		// Next
		ImageIcon nextIcon = Utils.createImageIcon("/icons/next_icon_36.png", "Next Icon");
		JButton nextButton = Utils.createIconButton(nextIcon);
		gbc.gridx = 4;
		gridBagContainerPanel.add(nextButton, gbc);

		nextButton.addActionListener(onNextClicked);
	}

	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.setAlignmentX(LEFT_ALIGNMENT);
		filterPanel.setBackground(null);

		int fontSize = 20;
		int padding = 8;
		int margin = 5;

		allFilterButton = Utils.createColorBackgroundButton("ทั้งหมด", mainColor, Color.WHITE, fontSize, padding);
		filterPanel.add(allFilterButton);
		filterPanel.add(Box.createHorizontalStrut(margin));

		rentFilterButton = Utils.createColorBackgroundButton("การเช่า", null, Color.BLACK, fontSize, padding);
		filterPanel.add(rentFilterButton);
		filterPanel.add(Box.createHorizontalStrut(margin));

		depositFilterButton = Utils.createColorBackgroundButton("การฝาก", null, Color.BLACK, fontSize, padding);
		filterPanel.add(depositFilterButton);
		filterPanel.add(Box.createHorizontalStrut(margin));

		checkFilterButton = Utils.createColorBackgroundButton("ตรวจสอบ", null, Color.BLACK, fontSize, padding);
		filterPanel.add(checkFilterButton);
		filterPanel.add(Box.createHorizontalStrut(margin));

		allFilterButton.addActionListener(e -> {
			onFilterButtonClicked("all");
		});

		rentFilterButton.addActionListener(e -> {
			onFilterButtonClicked("rent");
		});

		depositFilterButton.addActionListener(e -> {
			onFilterButtonClicked("deposit");
		});

		checkFilterButton.addActionListener(e -> {
			onFilterButtonClicked("check");
		});

		return filterPanel;
	}

	private void onFilterButtonClicked(String filter) {
		// filter values: all, rent, deposit, check

		// reset every button to unclicked state
		allFilterButton.setBackground(null);
		rentFilterButton.setBackground(null);
		depositFilterButton.setBackground(null);
		checkFilterButton.setBackground(null);

		allFilterButton.setForeground(Color.BLACK);
		rentFilterButton.setForeground(Color.BLACK);
		depositFilterButton.setForeground(Color.BLACK);
		checkFilterButton.setForeground(Color.BLACK);

		// set button that's been clicked
		if (filter.equals("all")) {
			allFilterButton.setBackground(mainColor);
			allFilterButton.setForeground(Color.WHITE);
		} else if (filter.equals("rent")) {
			rentFilterButton.setBackground(mainColor);
			rentFilterButton.setForeground(Color.WHITE);
		} else if (filter.equals("deposit")) {
			depositFilterButton.setBackground(mainColor);
			depositFilterButton.setForeground(Color.WHITE);
		} else if (filter.equals("check")) {
			checkFilterButton.setBackground(mainColor);
			checkFilterButton.setForeground(Color.WHITE);
		}

		addHistoryList(filter);
	}

	private void createPageSeparator(JPanel gridBagContainerPanel, int row, int columnSpan) {
		JSeparator separator = createPageSeparator();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = columnSpan;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;

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

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		return createPageLabel("", fontStyle, fontSize);
	}

}
