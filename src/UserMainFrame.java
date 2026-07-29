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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserMainFrame extends JFrame {

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
		appTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
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
		aboutLabel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
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
		notiIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeCard(6, 6);
			}
		});
		notiAndDateTimePanel.add(notiIconLabel);
		
		// Date And Time
		JPanel dateAndTimePanel = new JPanel();
		dateAndTimePanel.setBackground(BACKGROUND_COLOR);
		dateAndTimePanel.setBorder(createPaddingBorder(DETAIL_FONT_COLOR, 5));
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
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(BACKGROUND_COLOR);
		homePanel.add(centerPanel, BorderLayout.CENTER);
		
		// Power Bank Check
		JPanel powerBankCheckPanel = new JPanel();
		powerBankCheckPanel.setLayout(new BoxLayout(powerBankCheckPanel, BoxLayout.Y_AXIS));
		powerBankCheckPanel.setBackground(Color.decode("#ECF3FE"));
		powerBankCheckPanel.setBorder(createPaddingBorder(new Color(202, 207, 217), 15));
		GridBagConstraints checkConstraints = new GridBagConstraints();
		checkConstraints.gridx = 0;
		checkConstraints.gridy = 0;
		centerPanel.add(powerBankCheckPanel, checkConstraints);
		
		JLabel checkTitleLabel = new JLabel("ตรวจสอบ Power Bank");
		checkTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		checkTitleLabel.setForeground(Color.decode("#4E5986"));
		checkTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		powerBankCheckPanel.add(checkTitleLabel);
		
		JLabel checkDetailLabel1 = new JLabel("ตรวจสอบว่า Power Bank ของคุณ");
		checkDetailLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkDetailLabel1.setForeground(new Color(92, 105, 156));
		powerBankCheckPanel.add(checkDetailLabel1);
		
		JLabel checkDetailLabel2 = new JLabel("สามารถนำขึ้นเครื่องได้หรือไม่");
		checkDetailLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkDetailLabel2.setForeground(new Color(92, 105, 156));
		checkDetailLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		powerBankCheckPanel.add(checkDetailLabel2);
		
		JButton checkButton = new JButton("ตรวจสอบเลย");
		checkButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkButton.setBackground(Color.decode("#0A5DF5"));
		checkButton.setForeground(Color.WHITE);
		checkButton.setFocusPainted(false);
		checkButton.setMargin(new Insets(10, 10, 10, 10));
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCard(1, 1);
			}
			
		});
		powerBankCheckPanel.add(checkButton);
		
		// Rent Buy
		JPanel rentBuyPanel = new JPanel();
		rentBuyPanel.setLayout(new BoxLayout(rentBuyPanel, BoxLayout.Y_AXIS));
		rentBuyPanel.setBackground(Color.decode("#EFF8F5"));
		rentBuyPanel.setBorder(createPaddingBorder(new Color(176, 184, 181), 15));
		GridBagConstraints rentBuyConstraints = new GridBagConstraints();
		rentBuyConstraints.gridx = 1;
		rentBuyConstraints.gridy = 0;
		rentBuyConstraints.insets = new Insets(0, 15, 0, 0);
		centerPanel.add(rentBuyPanel, rentBuyConstraints);
		
		JLabel rentBuyTitleLabel = new JLabel("เช่า / ซื้อ Power Bank");
		rentBuyTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		rentBuyTitleLabel.setForeground(Color.decode("#3B6045"));
		rentBuyTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		rentBuyPanel.add(rentBuyTitleLabel);
		
		JLabel rentBuyDetailLabel1 = new JLabel("เลือกเช่า Power Bank");
		rentBuyDetailLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rentBuyDetailLabel1.setForeground(Color.decode("#7A8688"));
		rentBuyPanel.add(rentBuyDetailLabel1);
		
		JLabel rentBuyDetailLabel2 = new JLabel("ที่ได้มาตรฐานการบิน");
		rentBuyDetailLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rentBuyDetailLabel2.setForeground(Color.decode("#7A8688"));
		rentBuyDetailLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		rentBuyPanel.add(rentBuyDetailLabel2);
		
		JButton rentBuyButton = new JButton("เลือก Power Bank");
		rentBuyButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rentBuyButton.setBackground(Color.decode("#3B6045"));
		rentBuyButton.setForeground(Color.WHITE);
		rentBuyButton.setFocusPainted(false);
		rentBuyButton.setMargin(new Insets(10, 10, 10, 10));
		rentBuyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCard(2, 2);
			}
			
		});
		rentBuyPanel.add(rentBuyButton);
		
		// Storage
		JPanel storagePanel = new JPanel();
		storagePanel.setLayout(new BoxLayout(storagePanel, BoxLayout.Y_AXIS));
		storagePanel.setBackground(Color.decode("#FFDDB0"));
		storagePanel.setBorder(createPaddingBorder(new Color(219, 191, 154), 15));
		GridBagConstraints storageConstraints = new GridBagConstraints();
		storageConstraints.gridx = 2;
		storageConstraints.gridy = 0;
		storageConstraints.insets = new Insets(0, 15, 0, 0);
		centerPanel.add(storagePanel, storageConstraints);
		
		JLabel storageTitleLabel = new JLabel("ฝาก Power Bank");
		storageTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		storageTitleLabel.setForeground(new Color(158, 90, 0));
		storageTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		storagePanel.add(storageTitleLabel);
		
		JLabel storageDetailLabel1 = new JLabel("ฝาก Power Bank ไว้ก่อน");
		storageDetailLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageDetailLabel1.setForeground(Color.decode("#7A8688"));
		storagePanel.add(storageDetailLabel1);
		
		JLabel storageDetailLabel2 = new JLabel("แล้วกลับมารับ");
		storageDetailLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageDetailLabel2.setForeground(Color.decode("#7A8688"));
		storageDetailLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		storagePanel.add(storageDetailLabel2);
		
		JButton storageButton = new JButton("ฝาก Power Bank");
		storageButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageButton.setBackground(new Color(158, 90, 0));
		storageButton.setForeground(Color.WHITE);
		storageButton.setFocusPainted(false);
		storageButton.setMargin(new Insets(10, 10, 10, 10));
		storageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCard(3, 3);
			}
		});
		storagePanel.add(storageButton);
		
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
	
	private Border createPaddingBorder(Color color, int padding) {
		Border lineBorder = BorderFactory.createLineBorder(color, 1);
		Border paddingBorder = BorderFactory.createEmptyBorder(padding, padding, padding, padding);
		Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);
		return compoundBorder;
	}

}
