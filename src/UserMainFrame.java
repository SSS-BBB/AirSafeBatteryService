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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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

	private void createMenuButton(int panelIndex, String iconPath, String iconDescription, String menuStr,
			int cardIndex, int margin) {

		menuPanels[panelIndex] = new JPanel(new BorderLayout());
		menuPanels[panelIndex].setBackground(MENU_COLOR);

		// menu icon
		ImageIcon menuIcon = createImageIcon(iconPath, iconDescription);
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

		createMenuButton(0, "icons/home_icon.png", "Home Icon", "หน้าหลัก", 0, 8);
		createMenuButton(1, "icons/check_icon.png", "Check Icon", "ตรวจสอบ Power Bank", 1, 8);
		createMenuButton(2, "icons/purchase_icon.png", "Purchase Icon", "เช่า Power Bank", 2, 8);
		createMenuButton(3, "icons/storage_icon.png", "Storage Icon", "ฝาก Power Bank", 3, 8);
		createMenuButton(4, "icons/list_icon.png", "List Icon", "รายการของฉัน", 4, 8);
		createMenuButton(5, "icons/history_icon.png", "History Icon", "ประวัติการใช้งาน", 5, 8);
		createMenuButton(6, "icons/noti_white_icon.png", "Notification Icon", "แจ้งเตือน", 6, 8);
		createMenuButton(7, "icons/user_icon.png", "User Icon", "ผู้ใช้", 0, 8);

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
		createRentCard();
		createStorageCard();
		createListCard();
		createHistoryCard();
		createNotificationCard();
		createCheckStatusCard(false);
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
		JLabel aboutLabel1 = new JLabel(
				"โปรแกรมนี้คือโปรแกรมสำหรับตรวจสอบ Power Bank ก่อนนำขึ้นเครื่องตามข้อกำหนด IATA");
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
		JLabel noteLabel1 = new JLabel(
				"หมายเหตุ: โปรแกรมนี้ถูกจัดทำขึ้นสำหรับการเรียนรู้รายวิชา Database System Concepts");
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

		// Row 2
		Color borderColorRow2 = new Color(211, 211, 211);
		Color detailButtonColor = new Color(36, 160, 237);

		// Current Rent List
		JPanel currentRentPanel = new JPanel();
		currentRentPanel.setBackground(BACKGROUND_COLOR);
		currentRentPanel.setBorder(createPaddingBorder(borderColorRow2, 15));
		GridBagConstraints currentRentPanelConstraints = new GridBagConstraints();
		currentRentPanelConstraints.gridx = 1;
		currentRentPanelConstraints.gridy = 1;
		currentRentPanelConstraints.anchor = GridBagConstraints.CENTER;
		currentRentPanelConstraints.insets = new Insets(15, 0, 0, 0);
		centerPanel.add(currentRentPanel, currentRentPanelConstraints);

		ImageIcon currentRentImageIcon = createImageIcon("icons/purchase_black_icon.png", "Currnet Rent Icon");
		JLabel currentRentIconLabel = new JLabel(currentRentImageIcon);
		currentRentPanel.add(currentRentIconLabel);

		JPanel currentRentTextPanel = new JPanel();
		currentRentTextPanel.setLayout(new BoxLayout(currentRentTextPanel, BoxLayout.Y_AXIS));
		currentRentTextPanel.setBackground(BACKGROUND_COLOR);
		currentRentPanel.add(currentRentTextPanel);

		JLabel currentRentTitleLabel = new JLabel("การเช่าในปัจจุบัน");
		currentRentTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentRentTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		currentRentTextPanel.add(currentRentTitleLabel);

		int rentNumber = 1;
		JPanel rentNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		rentNumberPanel.setBackground(BACKGROUND_COLOR);
		rentNumberPanel.setAlignmentX(LEFT_ALIGNMENT);
		currentRentTextPanel.add(rentNumberPanel);
		JLabel rentNumberLabel = new JLabel(String.valueOf(rentNumber));
		rentNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		rentNumberPanel.add(rentNumberLabel);
		JLabel listLabel = new JLabel(" รายการ");
		listLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		listLabel.setForeground(Color.DARK_GRAY);
		rentNumberPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		rentNumberPanel.add(listLabel);

		JButton rentDetailButton = createNoBackgroundButton("ดูรายละเอียด", detailButtonColor);
		rentDetailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCard(4, 4);
			}
		});

		currentRentTextPanel.add(rentDetailButton);

		// Current Storage List
		JPanel currentStoragePanel = new JPanel();
		currentStoragePanel.setBackground(BACKGROUND_COLOR);
		currentStoragePanel.setBorder(createPaddingBorder(borderColorRow2, 15));
		GridBagConstraints currentStoragePanelConstraints = new GridBagConstraints();
		currentStoragePanelConstraints.gridx = 2;
		currentStoragePanelConstraints.gridy = 1;
		currentStoragePanelConstraints.anchor = GridBagConstraints.CENTER;
		currentStoragePanelConstraints.insets = new Insets(15, 0, 0, 0);
		centerPanel.add(currentStoragePanel, currentStoragePanelConstraints);

		ImageIcon currentStorageImageIcon = createImageIcon("icons/storage_black_icon.png", "Currnet Storage Icon");
		JLabel currentStorageIconLabel = new JLabel(currentStorageImageIcon);
		currentStoragePanel.add(currentStorageIconLabel);

		JPanel currentStorageTextPanel = new JPanel();
		currentStorageTextPanel.setLayout(new BoxLayout(currentStorageTextPanel, BoxLayout.Y_AXIS));
		currentStorageTextPanel.setBackground(BACKGROUND_COLOR);
		currentStoragePanel.add(currentStorageTextPanel);

		JLabel currentStorageTitleLabel = new JLabel("การฝากในปัจจุบัน");
		currentStorageTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentStorageTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		currentStorageTextPanel.add(currentStorageTitleLabel);

		int storageNumber = 1;
		JPanel storageNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		storageNumberPanel.setBackground(BACKGROUND_COLOR);
		storageNumberPanel.setAlignmentX(LEFT_ALIGNMENT);
		currentStorageTextPanel.add(storageNumberPanel);
		JLabel storageNumberLabel = new JLabel(String.valueOf(storageNumber));
		storageNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		storageNumberPanel.add(storageNumberLabel);
		JLabel storageListLabel = new JLabel(" รายการ");
		storageListLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageListLabel.setForeground(Color.DARK_GRAY);
		storageNumberPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		storageNumberPanel.add(storageListLabel);

		JButton storageDetailButton = createNoBackgroundButton("ดูรายละเอียด", detailButtonColor);
		storageDetailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCard(4, 4);
			}
		});
		currentStorageTextPanel.add(storageDetailButton);

		// Bottom (link to IATA)
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.setBackground(BACKGROUND_COLOR);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 0));
		homePanel.add(bottomPanel, BorderLayout.SOUTH);

		// Title
		JLabel bottomTitleLabel = new JLabel("แหล่งอ้างอิง");
		bottomTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		bottomTitleLabel.setForeground(Color.BLACK);
		bottomTitleLabel.setAlignmentX(LEFT_ALIGNMENT);
		bottomPanel.add(bottomTitleLabel);

		// Detail (link to IATA)
		JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		detailPanel.setBackground(BACKGROUND_COLOR);
		detailPanel.setAlignmentX(LEFT_ALIGNMENT);
		bottomPanel.add(detailPanel);

		JLabel seeMoreLabel = new JLabel("สามารถศึกษาเพิ่มเติมได้ที่ ");
		seeMoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		seeMoreLabel.setForeground(Color.BLACK);
		detailPanel.add(seeMoreLabel);

		JLabel linkLabel = new JLabel("<html><a href=''><u>IATA</u></a></html>");
		linkLabel.setFont(new Font("Tahoma", Font.ITALIC, 18));
		linkLabel.setForeground(Color.BLUE);

		linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		linkLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().browse(new URI(
								"https://www.iata.org/contentassets/90f8038b0eea42069554b2f4530f49ea/guidance-to-operators---power-banks.pdf"));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		detailPanel.add(linkLabel);

		cardPanel.add(homePanel, cardNames[0]);
	}

	private JButton createNoBackgroundButton(String text, Color color) {
		JButton noBgButton = new JButton(text);
		noBgButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		noBgButton.setForeground(color);
		noBgButton.setBackground(null);
		noBgButton.setFocusPainted(false);
		noBgButton.setBorder(null);
		noBgButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return noBgButton;
	}

	private void createCheckCard() {
		checkPanel = new JPanel();
		checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
		checkPanel.setBackground(BACKGROUND_COLOR);

		JLabel checkTitleLabel = new JLabel("ตรวจสอบ Power Bank");
		checkTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		checkTitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 30, 0));
		checkPanel.add(checkTitleLabel);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(BACKGROUND_COLOR);
		infoPanel.setBorder(createPaddingBorder(Color.BLACK, 1, new Insets(25, 25, 25, 25), new Insets(20, 25, 30, 0)));
		checkPanel.add(infoPanel);

		JLabel infoTitleLabel = new JLabel("กรอกข้อมูล Power Bank ของคุณ");
		infoTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		infoTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		infoPanel.add(infoTitleLabel);

		// Capacity
		JLabel capacityTitleLabel = new JLabel("ความจุ (mAh)");
		capacityTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		capacityTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		infoPanel.add(capacityTitleLabel);

		JTextField capacityTextField = createAppTextField();
		infoPanel.add(capacityTextField);

		// Voltage
		JLabel voltageTitleLabel = new JLabel("แรงดันไฟฟ้า (V)");
		voltageTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		voltageTitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 8, 0));
		infoPanel.add(voltageTitleLabel);

		JTextField voltageTextField = createAppTextField();
		infoPanel.add(voltageTextField);

		// Or
		JLabel alternativeLabel = new JLabel("หรือ กรอก Wh โดยตรง");
		alternativeLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		alternativeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		infoPanel.add(alternativeLabel);

		// Energy
		JLabel energyTitleLabel = new JLabel("พลังงาน (Wh)");
		energyTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		energyTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		infoPanel.add(energyTitleLabel);

		JTextField energyTextField = createAppTextField();
		infoPanel.add(energyTextField);
		infoPanel.add(Box.createRigidArea(new Dimension(0, 25)));

		// Check Button
		JButton checkButton = new JButton("ตรวจสอบ");
		checkButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		checkButton.setBackground(Color.BLUE);
		checkButton.setForeground(Color.WHITE);
		checkButton.setFocusPainted(false);
		checkButton.setMaximumSize(new Dimension(350, 50));
		checkButton.setPreferredSize(new Dimension(350, 50));
		checkButton.setAlignmentX(LEFT_ALIGNMENT);
		checkButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkButton.addActionListener(e -> {
			// TODO: check conditions from database
			
			double wh = Double.parseDouble(energyTextField.getText());
			
			if (wh > 100) {
				updateStatusPanel(wh, false);
			}
			
			else {
				updateStatusPanel(wh, true);
			}
			
			changeCard(1, 7);
		});
		infoPanel.add(checkButton);

		cardPanel.add(checkPanel, cardNames[1]);
	}

	private JTextField createAppTextField() {
		JTextField textField = new JTextField();
		textField.setBackground(BACKGROUND_COLOR);
		textField.setPreferredSize(new Dimension(350, 35));
		textField.setMaximumSize(new Dimension(350, 35));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setAlignmentX(LEFT_ALIGNMENT);
		return textField;
	}

	private void createRentCard() {
		rentPanel = new JPanel();
		rentPanel.setBackground(BACKGROUND_COLOR);
		JLabel rentBuyLabel = new JLabel("This is rent page");
		rentPanel.add(rentBuyLabel);
		cardPanel.add(rentPanel, cardNames[2]);
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

	private void createCheckStatusCard(boolean approveStatus) {
		
		checkStatusPanel = new JPanel(new BorderLayout());
		checkStatusPanel.setBackground(BACKGROUND_COLOR);		
		cardPanel.add(checkStatusPanel, cardNames[7]);
		
		// North (Status Title)
		JLabel checkStatusTitleLabel = new JLabel("ผลการตรวจสอบ");
		checkStatusTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		checkStatusTitleLabel.setForeground(Color.BLACK);
		checkStatusTitleLabel.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 0));
		checkStatusPanel.add(checkStatusTitleLabel, BorderLayout.NORTH);
		
		// Line-Start and Line-End Empty Space
		JPanel fillStart = new JPanel();
		fillStart.add(Box.createRigidArea(new Dimension(0, 0)));
		fillStart.setOpaque(false);
		checkStatusPanel.add(fillStart, BorderLayout.LINE_START);
		
		JPanel fillEnd = new JPanel();
		fillEnd.add(Box.createRigidArea(new Dimension(20, 0)));
		fillEnd.setOpaque(false);
		checkStatusPanel.add(fillEnd, BorderLayout.LINE_END);
		
		// Center (Status Content)
		JPanel statusContentPanel = new JPanel(new GridBagLayout());
		statusContentPanel.setBackground(BACKGROUND_COLOR);
		checkStatusPanel.add(statusContentPanel, BorderLayout.CENTER);
		
		Color borderColor = new Color(166, 173, 168);

		// TODO: get wh from the database
		
		createStatusPanel(borderColor);
		GridBagConstraints statusC = new GridBagConstraints();
		statusC.gridx = 0;
		statusC.gridy = 0;
		statusC.fill = GridBagConstraints.HORIZONTAL;
		statusC.insets = new Insets(25, 25, 0, 0);
		statusContentPanel.add(statusPanel, statusC);
		
		JPanel detailStatusPanel = createDetailStatusPanel(borderColor);
		GridBagConstraints detailC = new GridBagConstraints();
		detailC.gridx = 1;
		detailC.gridy = 0;
		detailC.anchor = GridBagConstraints.NORTHWEST;
		detailC.fill = GridBagConstraints.BOTH;
		detailC.insets = new Insets(25, 25, 0, 0);
		statusContentPanel.add(detailStatusPanel, detailC);
		
		JPanel ruleStatusPanel = createRuleStatusPanel(borderColor);
		GridBagConstraints ruleGBC = new GridBagConstraints();
		ruleGBC.gridx = 0;
		ruleGBC.gridy = 1;
		ruleGBC.gridwidth = 2;
		ruleGBC.weightx = 0;
		ruleGBC.anchor = GridBagConstraints.WEST;
		ruleGBC.fill = GridBagConstraints.HORIZONTAL;
		ruleGBC.insets = new Insets(25, 25, 0, 0);
		statusContentPanel.add(ruleStatusPanel, ruleGBC);
		
		JPanel fillerPanel = new JPanel();
		fillerPanel.setOpaque(false);
		GridBagConstraints filler = new GridBagConstraints();
		filler.gridx = 0;
		filler.gridy = 2;
		filler.gridwidth = 2;
		filler.weightx = 1.0;
		filler.weighty = 1.0;
		filler.fill = GridBagConstraints.BOTH;
		statusContentPanel.add(fillerPanel, filler);
		
		// SOUTH (buttons)
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setBackground(BACKGROUND_COLOR);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
		checkStatusPanel.add(southPanel, BorderLayout.SOUTH);
		
		Dimension buttonSize = new Dimension(220, 50);
		// Check-again button
		JButton checkAgainButton = new JButton("ตรวจสอบอีกครั้ง");
		checkAgainButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		checkAgainButton.setPreferredSize(buttonSize);
		checkAgainButton.setFocusPainted(false);
		checkAgainButton.setBackground(Color.WHITE);
		checkAgainButton.setForeground(Color.BLUE);
		checkAgainButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		checkAgainButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkAgainButton.addActionListener(e -> {
			changeCard(1, 1);
		});
		southPanel.add(checkAgainButton, BorderLayout.WEST);
		
		// Finish button
		JButton finishButton = new JButton("เสร็จสิ้น");
		finishButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		finishButton.setPreferredSize(buttonSize);
		finishButton.setFocusPainted(false);
		finishButton.setBackground(Color.BLUE);
		finishButton.setForeground(Color.WHITE);
		finishButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		finishButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		finishButton.addActionListener(e -> {
			changeCard(0, 0);
		});
		southPanel.add(finishButton, BorderLayout.EAST);
	}
	
	private JPanel createRuleStatusPanel(Color borderColor) {
		JPanel ruleStatusPanel = new JPanel();
		ruleStatusPanel.setLayout(new BoxLayout(ruleStatusPanel, BoxLayout.Y_AXIS));
		ruleStatusPanel.setBackground(BACKGROUND_COLOR);
		ruleStatusPanel.setBorder(createPaddingBorder(borderColor, 15));
		
		// Rule Title
		JLabel ruleTitleLabel = new JLabel("กฎระเบียบทั่วไปในการนำ Power Bank ขึ้นเครื่อง");
		ruleTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		ruleTitleLabel.setForeground(Color.BLACK);
		ruleTitleLabel.setAlignmentX(LEFT_ALIGNMENT);
		ruleTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
		ruleStatusPanel.add(ruleTitleLabel);
		
		// Rule 1
		JLabel rule1 = new JLabel("- ความจุไม่เกิน 100 Wh สามารถนำขึ้นเครื่องได้โดยไม่ต้องขออนุญาต");
		rule1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rule1.setForeground(Color.BLACK);
		rule1.setAlignmentX(LEFT_ALIGNMENT);
		rule1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		ruleStatusPanel.add(rule1);
		
		// Rule 2
		JLabel rule2 = new JLabel("- จะต้องถือเป็นสัมภาระขึ้นเครื่องเท่านั้น");
		rule2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rule2.setForeground(Color.BLACK);
		rule2.setAlignmentX(LEFT_ALIGNMENT);
		rule2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		ruleStatusPanel.add(rule2);
		
		// Rule 3
		JLabel rule3 = new JLabel("- สามารถพกพาได้สูงสุดไม่เกิน 2 เครื่อง");
		rule3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rule3.setForeground(Color.BLACK);
		rule3.setAlignmentX(LEFT_ALIGNMENT);
		rule3.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		ruleStatusPanel.add(rule3);
		
		return ruleStatusPanel;
	}
	
	private JPanel createDetailStatusPanel(Color borderColor) {
		// TODO: get data from database
		double capacity = 20000;
		double voltage = 3.7;
		double wh = 74;
		double maximumWh = 100;
		
		JPanel detailPanel = new JPanel();
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
		detailPanel.setBackground(BACKGROUND_COLOR);
		detailPanel.setBorder(createPaddingBorder(borderColor, 15));
		
		// Detail Title
		JLabel detailTitle = new JLabel("รายละเอียด");
		detailTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		detailTitle.setForeground(new Color(36, 160, 237));
		detailTitle.setAlignmentX(LEFT_ALIGNMENT);
		detailTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		detailPanel.add(detailTitle);
		
		// Detail Content
		JPanel capacityPanel = createDetailContent("ความจุ", capacity, "mAh");
		detailPanel.add(capacityPanel);
		
		JPanel voltagePanel = createDetailContent("แรงดันไฟฟ้า", voltage, "V");
		detailPanel.add(voltagePanel);
		
		JPanel whPanel = createDetailContent("พลังงาน", wh, "Wh");
		detailPanel.add(whPanel);
		
		JPanel maximumPanel = createDetailContent("เกณฑ์พลังงานไม่เกิน", maximumWh, "Wh");
		detailPanel.add(maximumPanel);
		
		detailPanel.add(Box.createVerticalGlue());
		
		return detailPanel;
	}
	
	private JPanel createDetailContent(String valueName, double value, String unit) {
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(BACKGROUND_COLOR);
		contentPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		JLabel valueNameLabel = new JLabel(valueName + " ");
		valueNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		valueNameLabel.setForeground(Color.BLACK);
		
		GridBagConstraints valueNameGBC = new GridBagConstraints();
		valueNameGBC.gridx = 0;
		valueNameGBC.gridy = 0;
		valueNameGBC.ipadx = 100;
		valueNameGBC.weightx = 1;
		valueNameGBC.anchor = GridBagConstraints.WEST;
		contentPanel.add(valueNameLabel, valueNameGBC);
		
		JLabel valueLabel = new JLabel(String.valueOf(value) + " " + unit);
		valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		valueLabel.setForeground(Color.BLACK);
		
		GridBagConstraints valueGBC = new GridBagConstraints();
		valueGBC.gridx = 1;
		valueGBC.gridy = 0;
		valueGBC.weightx = 0;
		valueGBC.anchor = GridBagConstraints.EAST;
		valueGBC.insets = new Insets(0, 0, 5, 0);
		contentPanel.add(valueLabel, valueGBC);
		
		return contentPanel;
	}
	
	private void createStatusPanel(Color borderColor) {
		
		statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		statusPanel.setBorder(createPaddingBorder(borderColor, 15));
		
		statusIconLabel = new JLabel();
		statusIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		statusIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		statusPanel.add(statusIconLabel);
		
		
		statusLabelTitle = new JLabel();
		statusLabelTitle.setFont(new Font("Tahoma", Font.BOLD, 32));
		statusLabelTitle.setAlignmentX(CENTER_ALIGNMENT);
		statusLabelTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		statusPanel.add(statusLabelTitle);
		
		statusLabelDetail = new JLabel();
		statusLabelDetail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		statusLabelDetail.setForeground(Color.DARK_GRAY);
		statusLabelDetail.setAlignmentX(CENTER_ALIGNMENT);
		statusLabelDetail.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		statusPanel.add(statusLabelDetail);
		
		whLabel = new JLabel();
		whLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		whLabel.setAlignmentX(CENTER_ALIGNMENT);
		whLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
		statusPanel.add(whLabel);
		
		
		
	}
	
	private void updateStatusPanel(double wh, boolean approve) {
		
		String statusTitle = "สามารถนำขึ้นเครื่องได้";
		String statusDetail = "Power Bank ของคุณเป็นไปตามกฎระเบียบ";
		String statusImagePath = "icons/check_green_icon_128.png";
		Color backgroundColor = new Color(201, 242, 212);
		Color statusTextColor = new Color(0, 100, 0);

		if (!approve) {
			statusTitle = "ไม่สามารถนำขึ้นเครื่องได้";
			statusDetail = "Power Bank ของคุณไม่เป็นไปตามกฎระเบียบ";
			statusImagePath = "icons/cancel_red_icon_128.png";
			backgroundColor = new Color(247, 188, 188);
			statusTextColor = new Color(176, 4, 4);
		}
		
		statusPanel.setBackground(backgroundColor);
		
		// status icon
		ImageIcon statusImageIcon = createImageIcon(statusImagePath, "Status Icon");
		
		statusIconLabel.setIcon(statusImageIcon);
		

		// status label
		statusLabelTitle.setText(statusTitle);
		statusLabelTitle.setForeground(statusTextColor);
		

		statusLabelDetail.setText(statusDetail);
		
		// wh label
		whLabel.setText("พลังงาน: " + String.valueOf(wh) + " Wh");
		whLabel.setForeground(statusTextColor);
	}

	private void changeCard(int selectedMenuIndex, int cardIndex) {
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

	private Border createPaddingBorder(Color color, int thickness, Insets paddings, Insets margins) {
		Border marginBorder = BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right);

		Border lineBorder = BorderFactory.createLineBorder(color, thickness);
		Border paddingBorder = BorderFactory.createEmptyBorder(paddings.top, paddings.left, paddings.bottom,
				paddings.right);
		Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

		Border compoundBorder2 = BorderFactory.createCompoundBorder(marginBorder, compoundBorder);

		return compoundBorder2;
	}

	private Border createPaddingBorder(Color color, Insets paddings) {
		return createPaddingBorder(color, 1, paddings, new Insets(0, 0, 0, 0));
	}

	private Border createPaddingBorder(Color color, int padding) {
		return createPaddingBorder(color, 1, new Insets(padding, padding, padding, padding), new Insets(0, 0, 0, 0));
	}

}
