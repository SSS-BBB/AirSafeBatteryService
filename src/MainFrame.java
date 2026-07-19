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
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

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
	private JPanel logoMenuPanel, homeMenuPanel, checkMenuPanel, rentBuyMenuPanel, powerBankStorageMenuPanel,
			listMenuPanel, historyMenuPanel, notificationMenuPanel, userMenuPanel;

	// cards (app pages)
	private JPanel homePanel;

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
		// TODO: add menus
		createMenus();
		menuPanel.add(menuSelectPanel, BorderLayout.CENTER);
		mainPanel.add(menuPanel, BorderLayout.WEST);

		// Card Panel
		cardPanel = new JPanel(new CardLayout());
		cardPanel.setBackground(BACKGROUND_COLOR);
		// TODO: add cards
		createHomeCard();
		mainPanel.add(cardPanel, BorderLayout.CENTER);

		// Split
		/*
		 * splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel,
		 * cardPanel); splitPane.setResizeWeight(0.2); mainPanel.add(splitPane);
		 */
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
	
	private void createMenuButton(JPanel panel, String iconPath, String iconDescription, String menuStr, int margin) {
		panel = new JPanel(new BorderLayout());
		panel.setBackground(MENU_COLOR);
		ImageIcon menuIcon = createImageIcon(iconPath, iconDescription);
		JLabel iconLabel = new JLabel(menuIcon);
		panel.add(iconLabel, BorderLayout.WEST);
		JLabel label = new JLabel(menuStr);
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setForeground(Color.WHITE);
		label.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(label, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(margin, 20, margin, 20));
		menuSelectPanel.add(panel);
	}
	
	private void createMenus() {

		// home
		createMenuButton(homeMenuPanel, "icons/home_icon.png", "Home Icon", "หน้าหลัก", 8);
		createMenuButton(checkMenuPanel, "icons/home_icon.png", "Home Icon", "ตรวจสอบ Power Bank", 8);
		createMenuButton(rentBuyMenuPanel, "icons/home_icon.png", "Home Icon", "เช่า/ซื้อ Power Bank", 8);
		createMenuButton(powerBankStorageMenuPanel, "icons/home_icon.png", "Home Icon", "ฝาก Power Bank", 8);
		createMenuButton(listMenuPanel, "icons/home_icon.png", "Home Icon", "รายการของฉัน", 8);
		createMenuButton(historyMenuPanel, "icons/home_icon.png", "Home Icon", "ประวัติการใช้งาน", 8);
		createMenuButton(notificationMenuPanel, "icons/home_icon.png", "Home Icon", "แจ้งเตือน", 8);
		createMenuButton(userMenuPanel, "icons/home_icon.png", "Home Icon", "ผู้ใช้", 8);
		/*
		homeMenuPanel = new JPanel();
		homeMenuPanel.setBackground(MENU_COLOR);
		ImageIcon homeIcon = createImageIcon("icons/home_icon.png", "Home Icon");
		JLabel homeIconLabel = new JLabel(homeIcon);
		homeMenuPanel.add(homeIconLabel);
		JLabel homeLabel = new JLabel("หน้าหลัก");
		homeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		homeLabel.setForeground(Color.WHITE);
		homeMenuPanel.add(homeLabel);
		homeMenuPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		menuSelectPanel.add(homeMenuPanel);
		*/

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

	private void createHomeCard() {
		homePanel = new JPanel();
		JLabel homeLabel = new JLabel("This is home page");
		homePanel.add(homeLabel);
		cardPanel.add(homePanel);
	}

}
