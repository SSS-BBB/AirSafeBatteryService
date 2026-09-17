package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import Utils.Utils;
import UserApp.UserMainFrame;

public class UserHome extends JPanel {
	
	private UserMainFrame mainFrame;
	private Color backgroundColor, detailFontColor;
	private String appName;
	
	private JPanel topPanel, aboutPanel, notiAndDateTimePanel, dateAndTimePanel, 
				  centerPanel, powerBankCheckPanel, rentBuyPanel, storagePanel,
				  currentRentPanel;
	private JLabel appTitleLabel, aboutLabel1, aboutLabel2, aboutLabel3,
				  noteLabel1, noteLabel2, noteLabel3, notiIconLabel,
				  dateLabel, timeLabel, checkTitleLabel, checkDetailLabel1,
				  checkDetailLabel2, rentBuyTitleLabel, rentBuyDetailLabel1,
				  rentBuyDetailLabel2, storageTitleLabel, storageDetailLabel1,
				  storageDetailLabel2, currentRentIconLabel;
	
	private JButton checkButton, rentBuyButton, storageButton;
	
	public UserHome(UserMainFrame mainFrame, Color backgroundColor, Color detailFontColor, String appName) {
		super(new BorderLayout());
		
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.appName = appName;
		
		createHomePage();
	}
	
	private void createHomePage() {
		this.setBackground(backgroundColor);

		// Top Panel (About This App + Notification and Date-Time)
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(backgroundColor);
		this.add(topPanel, BorderLayout.NORTH);

		// About This App
		aboutPanel = new JPanel();
		aboutPanel.setBackground(backgroundColor);
		aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
		aboutPanel.setBorder(new EmptyBorder(15, 15, 0, 0));
		topPanel.add(aboutPanel, BorderLayout.WEST);
		appTitleLabel = new JLabel(appName);
		appTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		appTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		aboutPanel.add(appTitleLabel);
		aboutLabel1 = new JLabel(
				"โปรแกรมนี้คือโปรแกรมสำหรับตรวจสอบ Power Bank ก่อนนำขึ้นเครื่องตามข้อกำหนด IATA");
		aboutLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aboutLabel1.setForeground(detailFontColor);
		aboutPanel.add(aboutLabel1);
		aboutLabel2 = new JLabel("มีบริการเช่า/ซื้อ Power Bank ที่ตรงตามมาตรฐานในการขึ้นเครื่อง");
		aboutLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aboutLabel2.setForeground(detailFontColor);
		aboutPanel.add(aboutLabel2);
		aboutLabel3 = new JLabel("และยังมีบริการรับฝาก Power Bank ก่อนขึ้นเครื่อง");
		aboutLabel3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aboutLabel3.setForeground(detailFontColor);
		aboutLabel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		aboutPanel.add(aboutLabel3);
		noteLabel1 = new JLabel(
				"หมายเหตุ: โปรแกรมนี้ถูกจัดทำขึ้นสำหรับการเรียนรู้รายวิชา Database System Concepts");
		noteLabel1.setFont(new Font("Tahoma", Font.BOLD, 14));
		aboutPanel.add(noteLabel1);
		noteLabel2 = new JLabel("ไม่ได้ถูกจัดทำขึ้นโดยหน่วยงาน IATA หรือองค์กรใดๆที่เกี่ยวข้อง");
		noteLabel2.setFont(new Font("Tahoma", Font.BOLD, 14));
		aboutPanel.add(noteLabel2);
		noteLabel3 = new JLabel("โปรดตรวจสอบข้อมูลเพิ่มเติมจากแหล่งที่น่าเชื่อถือ");
		noteLabel3.setFont(new Font("Tahoma", Font.BOLD, 14));
		aboutPanel.add(noteLabel3);

		// Notification
		notiAndDateTimePanel = new JPanel();
		notiAndDateTimePanel.setBackground(backgroundColor);
		notiAndDateTimePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		topPanel.add(notiAndDateTimePanel, BorderLayout.EAST);
		ImageIcon notiImageIcon = Utils.createImageIcon("/icons/noti_icon_32.png", "Notification Icon");
		notiIconLabel = new JLabel(notiImageIcon);
		notiIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		notiIconLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mainFrame.changeCard(6, 6);
			}
		});
		notiAndDateTimePanel.add(notiIconLabel);

		// Date And Time
		dateAndTimePanel = new JPanel();
		dateAndTimePanel.setBackground(backgroundColor);
		dateAndTimePanel.setBorder(Utils.createPaddingBorder(detailFontColor, 5));
		dateAndTimePanel.setLayout(new BoxLayout(dateAndTimePanel, BoxLayout.Y_AXIS));
		notiAndDateTimePanel.add(dateAndTimePanel);
		dateLabel = new JLabel("24 กรกฎาคม 2569");
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateLabel.setForeground(detailFontColor);
		dateAndTimePanel.add(dateLabel);
		timeLabel = new JLabel("05:18 PM");
		timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		timeLabel.setForeground(detailFontColor);
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
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(backgroundColor);
		this.add(centerPanel, BorderLayout.CENTER);

		// Power Bank Check
		powerBankCheckPanel = new JPanel();
		powerBankCheckPanel.setLayout(new BoxLayout(powerBankCheckPanel, BoxLayout.Y_AXIS));
		powerBankCheckPanel.setBackground(Color.decode("#ECF3FE"));
		powerBankCheckPanel.setBorder(Utils.createPaddingBorder(new Color(202, 207, 217), 15));
		GridBagConstraints checkConstraints = new GridBagConstraints();
		checkConstraints.gridx = 0;
		checkConstraints.gridy = 0;
		centerPanel.add(powerBankCheckPanel, checkConstraints);

		checkTitleLabel = new JLabel("ตรวจสอบ Power Bank");
		checkTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		checkTitleLabel.setForeground(Color.decode("#4E5986"));
		checkTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		powerBankCheckPanel.add(checkTitleLabel);

		checkDetailLabel1 = new JLabel("ตรวจสอบว่า Power Bank ของคุณ");
		checkDetailLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkDetailLabel1.setForeground(new Color(92, 105, 156));
		powerBankCheckPanel.add(checkDetailLabel1);

		checkDetailLabel2 = new JLabel("สามารถนำขึ้นเครื่องได้หรือไม่");
		checkDetailLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkDetailLabel2.setForeground(new Color(92, 105, 156));
		checkDetailLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		powerBankCheckPanel.add(checkDetailLabel2);

		checkButton = new JButton("ตรวจสอบเลย");
		checkButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkButton.setBackground(Color.decode("#0A5DF5"));
		checkButton.setForeground(Color.WHITE);
		checkButton.setFocusPainted(false);
		checkButton.setMargin(new Insets(10, 10, 10, 10));
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCard(1, 1);
			}

		});
		powerBankCheckPanel.add(checkButton);

		// Rent Buy
		rentBuyPanel = new JPanel();
		rentBuyPanel.setLayout(new BoxLayout(rentBuyPanel, BoxLayout.Y_AXIS));
		rentBuyPanel.setBackground(Color.decode("#EFF8F5"));
		rentBuyPanel.setBorder(Utils.createPaddingBorder(new Color(176, 184, 181), 15));
		GridBagConstraints rentBuyConstraints = new GridBagConstraints();
		rentBuyConstraints.gridx = 1;
		rentBuyConstraints.gridy = 0;
		rentBuyConstraints.insets = new Insets(0, 15, 0, 0);
		centerPanel.add(rentBuyPanel, rentBuyConstraints);

		rentBuyTitleLabel = new JLabel("เช่า Power Bank");
		rentBuyTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		rentBuyTitleLabel.setForeground(Color.decode("#3B6045"));
		rentBuyTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		rentBuyPanel.add(rentBuyTitleLabel);

		rentBuyDetailLabel1 = new JLabel("เลือกเช่า Power Bank");
		rentBuyDetailLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rentBuyDetailLabel1.setForeground(Color.decode("#7A8688"));
		rentBuyPanel.add(rentBuyDetailLabel1);

		rentBuyDetailLabel2 = new JLabel("ที่ได้มาตรฐานการบิน");
		rentBuyDetailLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rentBuyDetailLabel2.setForeground(Color.decode("#7A8688"));
		rentBuyDetailLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		rentBuyPanel.add(rentBuyDetailLabel2);

		rentBuyButton = new JButton("เลือก Power Bank");
		rentBuyButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rentBuyButton.setBackground(Color.decode("#3B6045"));
		rentBuyButton.setForeground(Color.WHITE);
		rentBuyButton.setFocusPainted(false);
		rentBuyButton.setMargin(new Insets(10, 10, 10, 10));
		rentBuyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCard(2, 2);
			}

		});
		rentBuyPanel.add(rentBuyButton);

		// Storage
		storagePanel = new JPanel();
		storagePanel.setLayout(new BoxLayout(storagePanel, BoxLayout.Y_AXIS));
		storagePanel.setBackground(Color.decode("#FFDDB0"));
		storagePanel.setBorder(Utils.createPaddingBorder(new Color(219, 191, 154), 15));
		GridBagConstraints storageConstraints = new GridBagConstraints();
		storageConstraints.gridx = 2;
		storageConstraints.gridy = 0;
		storageConstraints.insets = new Insets(0, 15, 0, 0);
		centerPanel.add(storagePanel, storageConstraints);

		storageTitleLabel = new JLabel("ฝาก Power Bank");
		storageTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		storageTitleLabel.setForeground(new Color(158, 90, 0));
		storageTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		storagePanel.add(storageTitleLabel);

		storageDetailLabel1 = new JLabel("ฝาก Power Bank ไว้ก่อน");
		storageDetailLabel1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageDetailLabel1.setForeground(Color.decode("#7A8688"));
		storagePanel.add(storageDetailLabel1);

		storageDetailLabel2 = new JLabel("แล้วกลับมารับ");
		storageDetailLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageDetailLabel2.setForeground(Color.decode("#7A8688"));
		storageDetailLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		storagePanel.add(storageDetailLabel2);

		storageButton = new JButton("ฝาก Power Bank");
		storageButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		storageButton.setBackground(new Color(158, 90, 0));
		storageButton.setForeground(Color.WHITE);
		storageButton.setFocusPainted(false);
		storageButton.setMargin(new Insets(10, 10, 10, 10));
		storageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCard(3, 3);
			}
		});
		storagePanel.add(storageButton);

		// Row 2
		Color borderColorRow2 = new Color(211, 211, 211);
		Color detailButtonColor = new Color(36, 160, 237);

		// Current Rent List
		currentRentPanel = new JPanel();
		currentRentPanel.setBackground(backgroundColor);
		currentRentPanel.setBorder(Utils.createPaddingBorder(borderColorRow2, 15));
		GridBagConstraints currentRentPanelConstraints = new GridBagConstraints();
		currentRentPanelConstraints.gridx = 1;
		currentRentPanelConstraints.gridy = 1;
		currentRentPanelConstraints.anchor = GridBagConstraints.CENTER;
		currentRentPanelConstraints.insets = new Insets(15, 0, 0, 0);
		centerPanel.add(currentRentPanel, currentRentPanelConstraints);

		ImageIcon currentRentImageIcon = Utils.createImageIcon("/icons/purchase_black_icon.png", "Currnet Rent Icon");
		currentRentIconLabel = new JLabel(currentRentImageIcon);
		currentRentPanel.add(currentRentIconLabel);

		JPanel currentRentTextPanel = new JPanel();
		currentRentTextPanel.setLayout(new BoxLayout(currentRentTextPanel, BoxLayout.Y_AXIS));
		currentRentTextPanel.setBackground(backgroundColor);
		currentRentPanel.add(currentRentTextPanel);

		JLabel currentRentTitleLabel = new JLabel("การเช่าในปัจจุบัน");
		currentRentTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentRentTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		currentRentTextPanel.add(currentRentTitleLabel);

		int rentNumber = 1;
		JPanel rentNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		rentNumberPanel.setBackground(backgroundColor);
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

		JButton rentDetailButton = Utils.createNoBackgroundButton("ดูรายละเอียด", detailButtonColor);
		rentDetailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCard(4, 4);
			}
		});

		currentRentTextPanel.add(rentDetailButton);

		// Current Storage List
		JPanel currentStoragePanel = new JPanel();
		currentStoragePanel.setBackground(backgroundColor);
		currentStoragePanel.setBorder(Utils.createPaddingBorder(borderColorRow2, 15));
		GridBagConstraints currentStoragePanelConstraints = new GridBagConstraints();
		currentStoragePanelConstraints.gridx = 2;
		currentStoragePanelConstraints.gridy = 1;
		currentStoragePanelConstraints.anchor = GridBagConstraints.CENTER;
		currentStoragePanelConstraints.insets = new Insets(15, 0, 0, 0);
		centerPanel.add(currentStoragePanel, currentStoragePanelConstraints);

		ImageIcon currentStorageImageIcon = Utils.createImageIcon("/icons/storage_black_icon.png", "Currnet Storage Icon");
		JLabel currentStorageIconLabel = new JLabel(currentStorageImageIcon);
		currentStoragePanel.add(currentStorageIconLabel);

		JPanel currentStorageTextPanel = new JPanel();
		currentStorageTextPanel.setLayout(new BoxLayout(currentStorageTextPanel, BoxLayout.Y_AXIS));
		currentStorageTextPanel.setBackground(backgroundColor);
		currentStoragePanel.add(currentStorageTextPanel);

		JLabel currentStorageTitleLabel = new JLabel("การฝากในปัจจุบัน");
		currentStorageTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		currentStorageTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		currentStorageTextPanel.add(currentStorageTitleLabel);

		int storageNumber = 1;
		JPanel storageNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		storageNumberPanel.setBackground(backgroundColor);
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

		JButton storageDetailButton = Utils.createNoBackgroundButton("ดูรายละเอียด", detailButtonColor);
		storageDetailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changeCard(4, 4);
			}
		});
		currentStorageTextPanel.add(storageDetailButton);

		// Bottom (link to IATA)
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.setBackground(backgroundColor);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 0));
		this.add(bottomPanel, BorderLayout.SOUTH);

		// Title
		JLabel bottomTitleLabel = new JLabel("แหล่งอ้างอิง");
		bottomTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		bottomTitleLabel.setForeground(Color.BLACK);
		bottomTitleLabel.setAlignmentX(LEFT_ALIGNMENT);
		bottomPanel.add(bottomTitleLabel);

		// Detail (link to IATA)
		JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		detailPanel.setBackground(backgroundColor);
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

		
	}
}