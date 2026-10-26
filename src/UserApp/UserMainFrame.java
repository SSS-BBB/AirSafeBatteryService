package UserApp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.Format;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.NumberFormatter;

import Database.InsertSample;
import GeneralPages.*;
import Database.DataContainer;
import Database.DatabaseConnector;
import Struct.DepositedPowerBank;
import Struct.ForRentPowerBank;
import Struct.PowerBank;
import Struct.RentedPowerBank;
import Struct.UserDetail;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Utils.Utils;
import UserApp.Pages.*;

public class UserMainFrame extends JFrame {

	private interface CardAction {
		public void onCardClicked();
	}

	private static final long serialVersionUID = 1L;
	private static final String APP_NAME = "AirSafe Battery Service";
	private static final int APP_WIDTH = 1280;
	private static final int APP_HEIGHT = 720;

	private JPanel mainPanel;
	private JPanel menuPanel;
	private JPanel menuSelectPanel;
	private JPanel cardPanel;

	private JPanel statusPanel;
	private JLabel statusIconLabel, statusLabelTitle, statusLabelDetail, whLabel;

	// menus
	private JPanel logoMenuPanel;

	private JPanel[] menuPanels;
	private String[] cardNames = { "HomeCard", "CheckCard", "RentCard", "DepositCard", "ListCard", "HistoryCard",
			"NotificationCard", "CheckStatusCard", "ForRentDetail", "ForRentPayment", "ProcessStatus",
			"PowerBankPicker", "PowerBankAdd", "DepositPayment", "RentedDetail", "DepositedDetail",
			"HistoryDetail", "UserInformation" };

	// cards (app pages)
	private JPanel checkPanel;

	private UserProcessStatus processStatusPanel;
	private PowerBankPickerPage powerBankPickerPanel;
	private PowerBankAddPage powerBankAddPanel;
	
	// Home page
	private UserHome homePanel;
	
	// Rent pages
	private UserRent rentPanel;
	private UserForRentDetail forRentDetailPanel;
	private UserRentPayment rentPaymentPanel;

	// Deposit pages
	private UserDeposit depositPanel;
	private UserDepositPayment depositPaymentPanel;

	// List page
	private UserList powerBankListPanel;

	// List Display pages
	private RentedDetailPage rentedDetailPanel;
	private DepositedDetailPage depositedDetailPanel;
	
	// History pages
	private UserHistory historyPanel;
	private UserHistoryDetail historyDetailPanel;
	
	// Notification page
	private UserNotification notificationPanel;
	
	// User Information page
	private UserInformation userInformationPanel;
	
	public UserDetail userDetail;

	// colors
	private static final Color BACKGROUND_COLOR = Color.decode("#F8FAFC");
	private static final Color MAIN_COLOR = Color.decode("#2563EB");
	private static final Color MENU_COLOR = Color.decode("#001631");
	private static final Color FONT_COLOR = Color.decode("#1F2937");
	private static final Color DETAIL_FONT_COLOR = new Color(93, 92, 91);
	private static final Color RENT_BUY_COLOR = Color.decode("#F97316");
	private static final Color GREEN_STATUS_COLOR = Color.decode("#22C55E");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserMainFrame frame = new UserMainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserMainFrame() {
		// Change Default UI Color
		try {
			UIManager.put("ComboBox.selectionBackground", new ColorUIResource(BACKGROUND_COLOR));
			// UIManager.put("Button.select", new ColorUIResource(BACKGROUND_COLOR));
		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseConnector.userConnect();
		
		// Insert Sample Data
		// InsertSample.insertPowerBank();
		// InsertSample.insertUser();

		// Sample User Data
		userDetail = new UserDetail();
		userDetail.userID = 4;
		userDetail.firstName = "Iam";
		userDetail.lastName = "Late";
		userDetail.email = "iamlate@always.late";
		userDetail.phone = "111222";
		
		DataContainer.refreshAll(userDetail.userID);

		setTitle(APP_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(APP_WIDTH, APP_HEIGHT);
		setLocationRelativeTo(null);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(BACKGROUND_COLOR);
		setContentPane(mainPanel);

		// Card Panel
		cardPanel = new JPanel(new CardLayout());
		cardPanel.setBackground(BACKGROUND_COLOR);
		createCardScreen();
		mainPanel.add(cardPanel, BorderLayout.CENTER);

		// Menu Panel
		menuPanel = new JPanel(new BorderLayout());
		createLogo();
		menuSelectPanel = new JPanel();
		menuSelectPanel.setLayout(new BoxLayout(menuSelectPanel, BoxLayout.Y_AXIS));
		menuSelectPanel.setBackground(MENU_COLOR);
		createMenus();
		menuPanel.add(menuSelectPanel, BorderLayout.CENTER);
		mainPanel.add(menuPanel, BorderLayout.WEST);

		changeCard(0, 0);

		// On App Close
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// close database connection before closing the program
				DatabaseConnector.closeConnection();
				dispose();
				System.exit(0);
			}
		});
	}

	private void createLogo() {
		// logo
		logoMenuPanel = new JPanel();
		logoMenuPanel.setBackground(MENU_COLOR);
		ImageIcon logoIcon = Utils.createImageIcon("/icons/airplane_icon.png", "Airplane Icon");
		JLabel logoIconLabel = new JLabel(logoIcon);
		logoMenuPanel.add(logoIconLabel);
		// TODO: fix app name label
		JLabel appNameLabel = new JLabel(APP_NAME);
		appNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		appNameLabel.setForeground(Color.WHITE);
		logoMenuPanel.add(appNameLabel);
		logoMenuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		menuPanel.add(logoMenuPanel, BorderLayout.NORTH);
	}

	private void createMenuButton(int panelIndex, String iconPath, String iconDescription, String menuStr,
			int cardIndex, int margin) {
		createMenuButton(panelIndex, iconPath, iconDescription, menuStr, cardIndex, margin, null);
	}

	private void createMenuButton(int panelIndex, String iconPath, String iconDescription, String menuStr,
			int cardIndex, int margin, CardAction cardAction) {

		menuPanels[panelIndex] = new JPanel(new BorderLayout());
		menuPanels[panelIndex].setBackground(MENU_COLOR);

		// menu icon
		ImageIcon menuIcon = Utils.createImageIcon(iconPath, iconDescription);
		JLabel iconLabel = new JLabel(menuIcon);
		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		menuPanels[panelIndex].add(iconLabel, BorderLayout.WEST);

		// menu text
		JLabel label = new JLabel(menuStr);
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setForeground(Color.WHITE);
		label.setAlignmentX(LEFT_ALIGNMENT);
		menuPanels[panelIndex].add(label, BorderLayout.CENTER);

		// margin
		menuPanels[panelIndex].setBorder(BorderFactory.createEmptyBorder(margin, 20, margin, 20));

		// change card when clicked
		menuPanels[panelIndex].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (cardAction != null)
					cardAction.onCardClicked();
				changeCard(panelIndex, cardIndex);
			}
		});

		menuSelectPanel.add(menuPanels[panelIndex]);
	}

	private void createMenus() {

		menuPanels = new JPanel[8];

		createMenuButton(0, "/icons/home_icon.png", "Home Icon", "หน้าหลัก", 0, 8);
		createMenuButton(1, "/icons/check_icon.png", "Check Icon", "ตรวจสอบ Power Bank", 1, 8);
		createMenuButton(2, "/icons/purchase_icon.png", "Purchase Icon", "เช่า Power Bank", 2, 8);
		createMenuButton(3, "/icons/storage_icon.png", "Storage Icon", "ฝาก Power Bank", 11, 8);
		createMenuButton(4, "/icons/list_icon.png", "List Icon", "รายการของฉัน", 4, 8);
		createMenuButton(5, "/icons/history_icon.png", "History Icon", "ประวัติการใช้งาน", 5, 8);
		createMenuButton(6, "/icons/noti_white_icon.png", "Notification Icon", "แจ้งเตือน", 6, 8);
		createMenuButton(7, "/icons/user_icon.png", "User Icon", "ผู้ใช้", 17, 8);

	}

	private void createCardScreen() {
		// create card panel objects
		homePanel = new UserHome(this, BACKGROUND_COLOR, DETAIL_FONT_COLOR, APP_NAME);

		checkPanel = new UserCheck(this, BACKGROUND_COLOR);

		processStatusPanel = new UserProcessStatus(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR);

		// Rent pages
		rentPaymentPanel = new UserRentPayment(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, processStatusPanel);
		forRentDetailPanel = new UserForRentDetail(this, BACKGROUND_COLOR, MENU_COLOR, MAIN_COLOR, rentPaymentPanel);
		rentPanel = new UserRent(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, forRentDetailPanel);

		// Deposit pages
		depositPaymentPanel = new UserDepositPayment(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR,
				processStatusPanel);
		depositPanel = new UserDeposit(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, depositPaymentPanel);

		powerBankPickerPanel = new PowerBankPickerPage(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, depositPanel);
		powerBankPickerPanel.updateTitleLabel("ฝาก Power Bank");
		powerBankAddPanel = new PowerBankAddPage(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, depositPanel);

		// List Detail pages
		rentedDetailPanel = new RentedDetailPage(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR);
		depositedDetailPanel = new DepositedDetailPage(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR);

		// List page
		PowerBank tempPowerBank = DatabaseConnector.getPowerBank(2, true).get(0);
		DepositedPowerBank tempDepositedPowerBank = new DepositedPowerBank(tempPowerBank, "สนามบินสุวรรณภูมิ",
				LocalDate.now(), LocalDate.now(), 1, 50.0, 25.0);
		tempDepositedPowerBank.depositStatus = "จองที่ฝากแล้ว";
		powerBankListPanel = new UserList(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, rentedDetailPanel,
				depositedDetailPanel);

		// Rented Detail page
		RentedPowerBank tempRentedPowerBank = new RentedPowerBank();
		tempRentedPowerBank.deviceInfo = tempPowerBank;
		tempRentedPowerBank.pickUpAddress = "สนามบินสุวรรณภูมิ";
		tempRentedPowerBank.returnAddress = "สนามบินดอนเมือง";
		tempRentedPowerBank.rentDate = LocalDate.now();
		tempRentedPowerBank.returnDate = LocalDate.now().plusDays(10);
		tempRentedPowerBank.status = "ยังไม่รับ";
		tempRentedPowerBank.lateFeePerDay = 50.0;
		tempRentedPowerBank.rentPrice = 1000.0;
		tempRentedPowerBank.lockerNumber = 35;
		tempRentedPowerBank.lockerPassword = "ABC123";

		// Deposited Detail Page
		tempDepositedPowerBank.lockerPassword = "BCD987";
		
		// History Page
		historyDetailPanel = new UserHistoryDetail(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR);
		historyPanel = new UserHistory(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR, historyDetailPanel);
		
		// Notification Page
		notificationPanel = new UserNotification(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR);
		
		// User Information Page
		userInformationPanel = new UserInformation(this, BACKGROUND_COLOR, MAIN_COLOR, MENU_COLOR);
		
		// add panels to card
		cardPanel.add(homePanel, cardNames[0]);
		cardPanel.add(checkPanel, cardNames[1]);
		cardPanel.add(rentPanel, cardNames[2]);
		cardPanel.add(depositPanel, cardNames[3]);
		cardPanel.add(forRentDetailPanel, cardNames[8]);
		cardPanel.add(rentPaymentPanel, cardNames[9]);
		cardPanel.add(processStatusPanel, cardNames[10]);
		cardPanel.add(powerBankPickerPanel, cardNames[11]);
		cardPanel.add(powerBankAddPanel, cardNames[12]);
		cardPanel.add(depositPaymentPanel, cardNames[13]);
		cardPanel.add(powerBankListPanel, cardNames[4]);
		cardPanel.add(rentedDetailPanel, cardNames[14]);
		cardPanel.add(depositedDetailPanel, cardNames[15]);
		cardPanel.add(historyPanel, cardNames[5]);
		cardPanel.add(historyDetailPanel, cardNames[16]);
		cardPanel.add(notificationPanel, cardNames[6]);
		cardPanel.add(userInformationPanel, cardNames[17]);
	}
	
	public void refreshDepositPage() {
		if (depositPanel == null) {
			System.err.println("Null Deposit Panel. Unable to refresh deposit page");
			return;
		}
		
		depositPanel.refreshUserInput();
	}
	
	public void refreshHomePage() {
		if (homePanel == null) {
			System.err.println("Null Home Panel. Unable to refresh home page");
			return;
		}
		
		homePanel.refreshPage();
	}
	
	public void refreshNotificationPage() {
		if (historyPanel == null) {
			System.err.println("Null Notification Panel. Unable to refresh notification page");
			return;
		}
		
		notificationPanel.refreshPage();
	}
	
	public void refreshHistoryPage() {
		if (historyPanel == null) {
			System.err.println("Null History Panel. Unable to refresh history page");
			return;
		}
		
		historyPanel.refreshPage();
	}
	
	public void refreshListPage() {
		if (powerBankListPanel == null) {
			System.err.println("Null Power Bank List Panel. Unable to refresh list page");
			return;
		}
		
		powerBankListPanel.updateListDisplayPanel();
	}
	
	public void refreshRentPage() {
		if (rentPanel == null) {
			System.err.println("Null Rent Panel. Unable to refresh rent page");
			return;
		}

		rentPanel.refreshPage();

	}

	public void addCard(JPanel card, int cardIndex) {
		if (cardIndex < 0 || cardIndex >= cardNames.length) {
			System.err.println("Card Index out of bounds, unable to add card at " + String.valueOf(cardIndex));
			return;
		}

		cardPanel.add(card, cardNames[cardIndex]);
	}

	public void changeCard(int selectedMenuIndex, int cardIndex) {
		// check menu index out of bounds
		if (selectedMenuIndex < 0 || selectedMenuIndex >= menuPanels.length) {
			System.err.println(
					"Selected Menu Index out of bounds, unable to color the menu " + String.valueOf(selectedMenuIndex));
		} else {
			// change other menus color
			for (int i = 0; i < menuPanels.length; i++) {
				if (i != selectedMenuIndex)
					menuPanels[i].setBackground(MENU_COLOR);
			}

			// change selected menu color
			JPanel selectedMenu = menuPanels[selectedMenuIndex];
			if (selectedMenu != null)
				selectedMenu.setBackground(MAIN_COLOR);
		}

		// check card index out of bounds
		if (cardIndex < 0 || cardIndex >= cardNames.length) {
			System.err.println("Card Index out of bounds, unable to change to page " + String.valueOf(cardIndex));
		} else {
			// change page
			CardLayout card = (CardLayout) cardPanel.getLayout();
			card.show(cardPanel, cardNames[cardIndex]);
		}
	}

}
