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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Utils.Utils;
import UserApp.Pages.*;

public class UserMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String APP_NAME = "AirSafe Battery Service";
	private static final int APP_WIDTH = 1280;
	private static final int APP_HEIGHT = 720;

	private JPanel mainPanel;
	private JPanel menuPanel;
	private JPanel menuSelectPanel;
	private JPanel cardPanel;
	
	JPanel statusPanel;
	JLabel statusIconLabel, statusLabelTitle, statusLabelDetail, whLabel;

	// menus
	private JPanel logoMenuPanel;

	private JPanel[] menuPanels;
	private String[] cardNames = { "HomeCard", "CheckCard", "RentCard", "StorageCard", "ListCard", "HistoryCard",
			"NotificationCard", "CheckStatusCard" };

	// cards (app pages)
	private JPanel homePanel, checkPanel, rentPanel, storagePanel, listPanel, historyPanel, notificationPanel,
			checkStatusPanel;

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
		setTitle(APP_NAME);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(APP_WIDTH, APP_HEIGHT);
		setLocationRelativeTo(null);

		mainPanel = new JPanel(new BorderLayout());
		// mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setBackground(BACKGROUND_COLOR);
		setContentPane(mainPanel);

		// Menu Panel
		menuPanel = new JPanel(new BorderLayout());
		createLogo();
		menuSelectPanel = new JPanel();
		menuSelectPanel.setLayout(new BoxLayout(menuSelectPanel, BoxLayout.Y_AXIS));
		menuSelectPanel.setBackground(MENU_COLOR);
		createMenus();
		menuPanel.add(menuSelectPanel, BorderLayout.CENTER);
		mainPanel.add(menuPanel, BorderLayout.WEST);

		// Card Panel
		cardPanel = new JPanel(new CardLayout());
		cardPanel.setBackground(BACKGROUND_COLOR);
		createCardScreen();
		mainPanel.add(cardPanel, BorderLayout.CENTER);
		changeCard(0, 0);
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
		createMenuButton(3, "/icons/storage_icon.png", "Storage Icon", "ฝาก Power Bank", 3, 8);
		createMenuButton(4, "/icons/list_icon.png", "List Icon", "รายการของฉัน", 4, 8);
		createMenuButton(5, "/icons/history_icon.png", "History Icon", "ประวัติการใช้งาน", 5, 8);
		createMenuButton(6, "/icons/noti_white_icon.png", "Notification Icon", "แจ้งเตือน", 6, 8);
		createMenuButton(7, "/icons/user_icon.png", "User Icon", "ผู้ใช้", 0, 8);

	}

	private void createCardScreen() {
		// create card panel objects
		homePanel = new UserHome(this, BACKGROUND_COLOR, DETAIL_FONT_COLOR, APP_NAME);
		checkPanel = new UserCheck(this, BACKGROUND_COLOR);
		
		// add panels to card
		cardPanel.add(homePanel, cardNames[0]);
		cardPanel.add(checkPanel, cardNames[1]);
	}

	
	
	public void addCard(JPanel card, int cardIndex) {
		if (cardIndex < 0 || cardIndex >= cardNames.length)
		{
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
