import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String APP_NAME = "AirSafe Battery Service";
	private static final int APP_WIDTH = 1280;
	private static final int APP_HEIGHT = 720;

	private JPanel mainPanel;
	private JPanel menuPanel;
	private JPanel menuSelectPanel;
	private JPanel cardPanel;

	// menus
	private JPanel logoMenuPanel;

	private JPanel[] menuPanels;

	// cards (app pages)
	private JPanel homePanel, checkPanel, rentBuyPanel, storagePanel, listPanel, historyPanel, notificationPanel;

	// colors
	private static final Color BACKGROUND_COLOR = Color.decode("#F8FAFC");
	private static final Color MAIN_COLOR = Color.decode("#2563EB");
	private static final Color MENU_COLOR = Color.decode("#001631");
	private static final Color FONT_COLOR = Color.decode("#1F2937");
	private static final Color RENT_BUY_COLOR = Color.decode("#F97316");
	private static final Color GREEN_STATUS_COLOR = Color.decode("#22C55E");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
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
		changeCard(0, "HomeCard");
	}

	private void createLogo() {
		// logo
		logoMenuPanel = new JPanel();
		logoMenuPanel.setBackground(MENU_COLOR);
		ImageIcon logoIcon = createImageIcon("icons/airplane_icon.png", "Airplane Icon");
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

	private void createMenuButton(int panelIndex, String iconPath, String iconDescription, String menuStr, String card,
			int margin) {

		menuPanels[panelIndex] = new JPanel(new BorderLayout());
		menuPanels[panelIndex].setBackground(MENU_COLOR);

		// menu icon
		ImageIcon menuIcon = createImageIcon(iconPath, iconDescription);
		JLabel iconLabel = new JLabel(menuIcon);
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
				changeCard(panelIndex, card);
			}
		});

		menuSelectPanel.add(menuPanels[panelIndex]);
	}

	private void createMenus() {

		menuPanels = new JPanel[8];

		createMenuButton(0, "icons/home_icon.png", "Home Icon", "หน้าหลัก", "HomeCard", 8);
		createMenuButton(1, "icons/home_icon.png", "Home Icon", "ตรวจสอบ Power Bank", "CheckCard", 8);
		createMenuButton(2, "icons/home_icon.png", "Home Icon", "เช่า/ซื้อ Power Bank", "RentBuyCard", 8);
		createMenuButton(3, "icons/home_icon.png", "Home Icon", "ฝาก Power Bank", "StorageCard", 8);
		createMenuButton(4, "icons/home_icon.png", "Home Icon", "รายการของฉัน", "ListCard", 8);
		createMenuButton(5, "icons/home_icon.png", "Home Icon", "ประวัติการใช้งาน", "HistoryCard", 8);
		createMenuButton(6, "icons/home_icon.png", "Home Icon", "แจ้งเตือน", "NotificationCard", 8);
		createMenuButton(7, "icons/home_icon.png", "Home Icon", "ผู้ใช้", "HomeCard", 8);

	}

	private ImageIcon createImageIcon(String path, String description) {
		InputStream inputStream = getClass().getResourceAsStream(path);

		if (inputStream == null) {
			System.err.println("Couldn't find file: " + path);
			return null;
		}

		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(inputStream);
			if (bufferedImage == null) {
				System.err.println("Couldn't decide the image from: " + path);
				return null;
			}

			return new ImageIcon(bufferedImage, description);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void createCardScreen() {
		createHomeCard();
		createCheckCard();
		createRentBuyCard();
		createStorageCard();
		createListCard();
		createHistoryCard();
		createNotificationCard();
	}

	private void createHomeCard() {
		homePanel = new JPanel();
		homePanel.setBackground(BACKGROUND_COLOR);
		JLabel homeLabel = new JLabel("This is home page");
		homePanel.add(homeLabel);
		cardPanel.add(homePanel, "HomeCard");
	}

	private void createCheckCard() {
		checkPanel = new JPanel();
		checkPanel.setBackground(BACKGROUND_COLOR);
		JLabel checkLabel = new JLabel("This is check page");
		checkPanel.add(checkLabel);
		cardPanel.add(checkPanel, "CheckCard");
	}

	private void createRentBuyCard() {
		rentBuyPanel = new JPanel();
		rentBuyPanel.setBackground(BACKGROUND_COLOR);
		JLabel rentBuyLabel = new JLabel("This is rent buy page");
		rentBuyPanel.add(rentBuyLabel);
		cardPanel.add(rentBuyPanel, "RentBuyCard");
	}

	private void createStorageCard() {
		storagePanel = new JPanel();
		storagePanel.setBackground(BACKGROUND_COLOR);
		JLabel storageLabel = new JLabel("This is storage page");
		storagePanel.add(storageLabel);
		cardPanel.add(storagePanel, "StorageCard");
	}

	private void createListCard() {
		listPanel = new JPanel();
		listPanel.setBackground(BACKGROUND_COLOR);
		JLabel listLabel = new JLabel("This is list page");
		listPanel.add(listLabel);
		cardPanel.add(listPanel, "ListCard");
	}

	private void createHistoryCard() {
		historyPanel = new JPanel();
		historyPanel.setBackground(BACKGROUND_COLOR);
		JLabel historyLabel = new JLabel("This is history page");
		historyPanel.add(historyLabel);
		cardPanel.add(historyPanel, "HistoryCard");
	}

	private void createNotificationCard() {
		notificationPanel = new JPanel();
		notificationPanel.setBackground(BACKGROUND_COLOR);
		JLabel notificationLabel = new JLabel("This is notification page");
		notificationPanel.add(notificationLabel);
		cardPanel.add(notificationPanel, "NotificationCard");
	}

	private void changeCard(int selectedMenuIndex, String cardToChange) {
		// change other menus color
		for (int i = 0; i < menuPanels.length; i++) {
			if (i != selectedMenuIndex)
				menuPanels[i].setBackground(MENU_COLOR);
		}

		// change selected menu color
		JPanel selectedMenu = menuPanels[selectedMenuIndex];
		if (selectedMenu != null)
			selectedMenu.setBackground(MAIN_COLOR);

		// change page
		CardLayout card = (CardLayout) cardPanel.getLayout();
		card.show(cardPanel, cardToChange);
	}

}
