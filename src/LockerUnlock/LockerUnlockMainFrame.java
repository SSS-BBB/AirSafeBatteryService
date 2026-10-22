package LockerUnlock;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Database.DataContainer;
import Database.DatabaseConnector;
import Database.InsertSample;
import LockerUnlock.Pages.*;
import Struct.UserDetail;

public class LockerUnlockMainFrame extends JFrame {

	// Programs
	private static final String APP_NAME = "AirSafe Battery Service Locker Unlock";
	private static final int APP_WIDTH = 1280;
	private static final int APP_HEIGHT = 720;

	// Colors
	public static final Color BACKGROUND_COLOR = Color.decode("#0B226C");
	public static final Color PRIMARY_COLOR = Color.decode("#0AB6FB");
	public static final Color PRIMARY_FONT_COLOR = Color.WHITE;
	public static final Color FAILED_COLOR = new Color(255, 40, 40);

	// Page names
	public static final String HOME_PAGE = "Home";
	public static final String LOGIN_PAGE = "Login";
	public static final String PASSWORD_ENTER_PAGE = "Password Enter";
	
	// Locker types
	public static final int RENT_LOCKER = 0;
	public static final int DEPOSIT_LOCKER = 1;

	public static void main(String[] args) {
		LockerUnlockMainFrame frame = new LockerUnlockMainFrame("สนามบินดอนเมือง");
		frame.setVisible(true);
	}

	// Locker Unlock Main Frame
	private String address;
	public UserDetail userDetail;
	public int lockerType; // rent or deposit;
	public int lockerNumber;

	private JPanel mainPanel;

	// Home page
	private LockerHome homePage;
	
	// Login page
	private LockerLogin loginPage;
	
	// Password Enter page
	private LockerPasswordEnter passwordEnterPage;
	
	// Create the frame
	public LockerUnlockMainFrame(String address) {
		DatabaseConnector.connect();
		
		this.address = address;
		this.lockerType = -1;
		this.lockerNumber = -1;
		
		// Sample User Data
		userDetail = new UserDetail();
		userDetail.userID = 1;
		userDetail.firstName = "First";
		userDetail.lastName = "User";
		userDetail.email = "firstemail@something.cool";
		userDetail.phone = "0000000000";
		
		DataContainer.refreshAll(userDetail.userID);

		setTitle(APP_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(APP_WIDTH, APP_HEIGHT);
		setLocationRelativeTo(null);

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

		mainPanel = new JPanel(new CardLayout());
		mainPanel.setBackground(BACKGROUND_COLOR);
		setContentPane(mainPanel);

		addCard();
		
		this.lockerType = DEPOSIT_LOCKER;
		changeCard(PASSWORD_ENTER_PAGE);
	}

	private void addCard() {
		// Create Pages
		homePage = new LockerHome(this);
		loginPage = new LockerLogin(this);
		passwordEnterPage = new LockerPasswordEnter(this);

		// Add pages to card
		mainPanel.add(homePage, HOME_PAGE);
		mainPanel.add(loginPage, LOGIN_PAGE);
		mainPanel.add(passwordEnterPage, PASSWORD_ENTER_PAGE);
	}

	public void changeCard(String cardName) {
		// Change page
		CardLayout card = (CardLayout) mainPanel.getLayout();
		card.show(mainPanel, cardName);
	}
	
	public String getAddress() {
		return address;
	}
}