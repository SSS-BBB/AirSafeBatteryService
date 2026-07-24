import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.border.Border;
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
	private String[] cardNames = {"HomeCard", "CheckCard", "RentBuyCard", "StorageCard",
			"ListCard", "HistoryCard", "NotificationCard"};

	// cards (app pages)
	private JPanel homePanel, checkPanel, rentBuyPanel, storagePanel, listPanel, historyPanel, notificationPanel;

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
		changeCard(0, 0);
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

	private void createMenuButton(int panelIndex, String iconPath, String iconDescription, String menuStr, int cardIndex,
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
				changeCard(panelIndex, cardIndex);
			}
		});

		menuSelectPanel.add(menuPanels[panelIndex]);
	}

	private void createMenus() {

		menuPanels = new JPanel[8];

		createMenuButton(0, "icons/home_icon.png", "Home Icon", "หน้าหลัก", 0, 8);
		createMenuButton(1, "icons/home_icon.png", "Home Icon", "ตรวจสอบ Power Bank", 1, 8);
		createMenuButton(2, "icons/home_icon.png", "Home Icon", "เช่า/ซื้อ Power Bank", 2, 8);
		createMenuButton(3, "icons/home_icon.png", "Home Icon", "ฝาก Power Bank", 3, 8);
		createMenuButton(4, "icons/home_icon.png", "Home Icon", "รายการของฉัน", 4, 8);
		createMenuButton(5, "icons/home_icon.png", "Home Icon", "ประวัติการใช้งาน", 5, 8);
		createMenuButton(6, "icons/home_icon.png", "Home Icon", "แจ้งเตือน", 6, 8);
		createMenuButton(7, "icons/home_icon.png", "Home Icon", "ผู้ใช้", 0, 8);

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
		homePanel = new JPanel(new BorderLayout());
		homePanel.setBackground(BACKGROUND_COLOR);
		
		// Top Panel (About This App + Notification and Date-Time)
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(BACKGROUND_COLOR);
		homePanel.add(topPanel, BorderLayout.NORTH);
		
		// About This App
		JPanel aboutPanel = new JPanel();
		aboutPanel.setBackground(BACKGROUND_COLOR);
		aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
		aboutPanel.setBorder(new EmptyBorder(15, 15, 0, 0));
		topPanel.add(aboutPanel, BorderLayout.WEST);
		JLabel appTitleLabel = new JLabel(APP_NAME);
		appTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		aboutPanel.add(appTitleLabel);
		JLabel aboutLabel1 = new JLabel("โปรแกรมนี้คือโปรแกรมสำหรับตรวจสอบ Power Bank ก่อนนำขึ้นเครื่องตามข้อกำหนด IATA");
		aboutLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aboutLabel1.setForeground(DETAIL_FONT_COLOR);
		aboutPanel.add(aboutLabel1);
		JLabel aboutLabel2 = new JLabel("มีบริการเช่า/ซื้อ Power Bank ที่ตรงตามมาตรฐานในการขึ้นเครื่อง");
		aboutLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aboutLabel2.setForeground(DETAIL_FONT_COLOR);
		aboutPanel.add(aboutLabel2);
		JLabel aboutLabel3 = new JLabel("และยังมีบริการรับฝาก Power Bank ก่อนขึ้นเครื่อง");
		aboutLabel3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aboutLabel3.setForeground(DETAIL_FONT_COLOR);
		aboutPanel.add(aboutLabel3);
		JLabel noteLabel1 = new JLabel("หมายเหตุ: โปรแกรมนี้ถูกจัดทำขึ้นสำหรับการเรียนรู้รายวิชา Database System Concepts");
		noteLabel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		aboutPanel.add(noteLabel1);
		JLabel noteLabel2 = new JLabel("ไม่ได้ถูกจัดทำขึ้นโดยหน่วยงาน IATA หรือองค์กรใดๆที่เกี่ยวข้อง");
		noteLabel2.setFont(new Font("Tahoma", Font.BOLD, 14));
		aboutPanel.add(noteLabel2);
		JLabel noteLabel3 = new JLabel("โปรดตรวจสอบข้อมูลเพิ่มเติมจากแหล่งที่น่าเชื่อถือ");
		noteLabel3.setFont(new Font("Tahoma", Font.BOLD, 14));
		aboutPanel.add(noteLabel3);
		
		// Notification
		JPanel notiAndDateTimePanel = new JPanel();
		notiAndDateTimePanel.setBackground(BACKGROUND_COLOR);
		notiAndDateTimePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		topPanel.add(notiAndDateTimePanel, BorderLayout.EAST);
		ImageIcon notiImageIcon = createImageIcon("icons/noti_icon_32.png", "Notification Icon");
		JLabel notiIconLabel = new JLabel(notiImageIcon);
		notiIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		notiAndDateTimePanel.add(notiIconLabel);
		
		// Date And Time
		JPanel dateAndTimePanel = new JPanel();
		dateAndTimePanel.setBackground(BACKGROUND_COLOR);
		Border lineBorder = BorderFactory.createLineBorder(DETAIL_FONT_COLOR, 1);
		Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);
		dateAndTimePanel.setBorder(compoundBorder);
		dateAndTimePanel.setLayout(new BoxLayout(dateAndTimePanel, BoxLayout.Y_AXIS));
		notiAndDateTimePanel.add(dateAndTimePanel);
		JLabel dateLabel = new JLabel("24 กรกฎาคม 2569");
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateLabel.setForeground(DETAIL_FONT_COLOR);
		dateAndTimePanel.add(dateLabel);
		JLabel timeLabel = new JLabel("05:18 PM");
		timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		timeLabel.setForeground(DETAIL_FONT_COLOR);
		dateAndTimePanel.add(timeLabel);
		
		// starts date and time update
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		
		Timer timer = new Timer(60000, e -> {
			LocalDateTime now = LocalDateTime.now();
			
			dateLabel.setText(now.format(dateFormatter));
			timeLabel.setText(now.format(timeFormatter));
		});
		
		timer.setInitialDelay(0);
		timer.start();
		
		// Center Panel (Pages Navigation + Status)
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(BACKGROUND_COLOR);
		homePanel.add(centerPanel, BorderLayout.CENTER);
		
		cardPanel.add(homePanel, cardNames[0]);
	}

	private void createCheckCard() {
		checkPanel = new JPanel();
		checkPanel.setBackground(BACKGROUND_COLOR);
		JLabel checkLabel = new JLabel("This is check page");
		checkPanel.add(checkLabel);
		cardPanel.add(checkPanel, cardNames[1]);
	}

	private void createRentBuyCard() {
		rentBuyPanel = new JPanel();
		rentBuyPanel.setBackground(BACKGROUND_COLOR);
		JLabel rentBuyLabel = new JLabel("This is rent buy page");
		rentBuyPanel.add(rentBuyLabel);
		cardPanel.add(rentBuyPanel, cardNames[2]);
	}

	private void createStorageCard() {
		storagePanel = new JPanel();
		storagePanel.setBackground(BACKGROUND_COLOR);
		JLabel storageLabel = new JLabel("This is storage page");
		storagePanel.add(storageLabel);
		cardPanel.add(storagePanel, cardNames[3]);
	}

	private void createListCard() {
		listPanel = new JPanel();
		listPanel.setBackground(BACKGROUND_COLOR);
		JLabel listLabel = new JLabel("This is list page");
		listPanel.add(listLabel);
		cardPanel.add(listPanel, cardNames[4]);
	}

	private void createHistoryCard() {
		historyPanel = new JPanel();
		historyPanel.setBackground(BACKGROUND_COLOR);
		JLabel historyLabel = new JLabel("This is history page");
		historyPanel.add(historyLabel);
		cardPanel.add(historyPanel, cardNames[5]);
	}

	private void createNotificationCard() {
		notificationPanel = new JPanel();
		notificationPanel.setBackground(BACKGROUND_COLOR);
		JLabel notificationLabel = new JLabel("This is notification page");
		notificationPanel.add(notificationLabel);
		cardPanel.add(notificationPanel, cardNames[6]);
	}

	private void changeCard(int selectedMenuIndex, int cardIndex) {
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
		card.show(cardPanel, cardNames[cardIndex]);
	}

}
