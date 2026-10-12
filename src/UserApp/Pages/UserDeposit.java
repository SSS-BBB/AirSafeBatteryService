package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.CustomDropDown;
import CustomGUI.LockerChooser;
import CustomGUI.PowerBankBox;
import Struct.PowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserDeposit extends JPanel {
	
	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;
	
	private PowerBankBox powerBankBox;
	
	// User Input Components
	private CustomDropDown<String> addressDropDown;
	
	// Input Data
	private String depositAddress;
	private int depositLockerNumber;
	private Calendar depositDate, returnDate;
	private double depositPrice;
	
	public UserDeposit(UserMainFrame mainFrame, 
			Color backgroundColor, Color menuColor, Color mainColor) {
		
		super(new BorderLayout());
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		
		depositAddress = "";
		depositLockerNumber = -1;
		depositDate = null;
		returnDate = null;
		depositPrice = -1.0;
		
		setBackground(backgroundColor);
		
		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		titleLabel.setText("ฝาก Power Bank");
		this.add(titleLabel, BorderLayout.NORTH);
		
		// Content
		JPanel contentPanel = createContentPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(contentPanel, BorderLayout.CENTER);
		
	}
	
	public UserDeposit(UserMainFrame mainFrame, 
			Color backgroundColor, Color menuColor, Color mainColor,
			PowerBank tempPowerBank) {
		this(mainFrame, backgroundColor, menuColor, mainColor);
		updatePage(tempPowerBank);
	}
	
	public void updatePage(PowerBank powerBank) {
		if (powerBankBox == null) {
			System.err.println("Null Power Bank Box. Unable to update deposit page.");
			return;
		}
		
		powerBankBox.updatePowerbankBox(powerBank);
	}
	
	private JPanel createUserInputPanel() {
		JPanel userInputPanel = new JPanel();
		userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.Y_AXIS));
		userInputPanel.setBackground(null);
		userInputPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		int margin = 8;
		int headingFontSize = 20;
		int bodyFontSize = 18;
		float componentAlignment = LEFT_ALIGNMENT;
		
		// Address
		String[] addressList = new String[] {"สนามบินสุวรรณภูมิ", "สนามบินดอนเมือง", "สนามบินภูเก็ต", "สนามบินเชียงใหม่", "สนามบินแม่ฟ้าหลวง", "สนามบินหาดใหญ่", "สนามบินขอนแก่น"};
		JPanel addressInputPanel = createTextWithComponentToAdd("สถานที่ฝาก:", bodyFontSize);
		userInputPanel.add(addressInputPanel);
		
		addressDropDown = new CustomDropDown<String>(addressList, backgroundColor, menuColor, 2);
		depositAddress = addressDropDown.getSelectedItem().toString();
		addressInputPanel.add(addressDropDown);
		
		Dimension boxSize = new Dimension(addressDropDown.getMinimumSize().width, 30);
		
		// Locker Number
		JPanel lockerPanel = createTextWithComponentToAdd("หมายเลข Locker:", bodyFontSize);
		userInputPanel.add(lockerPanel);
		
		JPanel lockerPlaceHolderBoxPanel = createPlaceHolderBox(boxSize);
		lockerPanel.add(lockerPlaceHolderBoxPanel);
		
		JLabel lockerHolderLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		lockerHolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		lockerPlaceHolderBoxPanel.add(lockerHolderLabel, BorderLayout.WEST);
		
		ImageIcon lockerImage = Utils.createImageIcon("/icons/locker_icon_24.png", "Locker Icon");
		JButton lockerSelectButton = Utils.createIconButton(lockerImage);
		lockerPanel.add(lockerSelectButton);
		
		lockerSelectButton.addActionListener(e -> {
			// String selectedAddress = addressDropDown.getSelectedItem().toString();
			LockerChooser lockerPicker = new LockerChooser(mainFrame, backgroundColor, Color.BLACK, mainColor, Color.DARK_GRAY, Color.BLACK);
			lockerPicker.setLockerChooserAction(lockerNumber -> {
				depositLockerNumber = lockerNumber;
				lockerHolderLabel.setText(String.valueOf(lockerNumber));
				
			});
			lockerPicker.showLockerChooser(depositAddress, false);
		});
		
		// force user to pick locker number again if they select new address
		addressDropDown.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String selectedAddress = e.getItem().toString();
				if (!selectedAddress.equals(depositAddress)) {
					depositAddress = selectedAddress;
					depositLockerNumber = -1;
					lockerHolderLabel.setText("");
				}
			}
		});
		
		return userInputPanel;
	}
	
	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		contentPanel.setBackground(null);
		
		int headingFontSize = 18;
		int bodyFontSize = 16;
		
		// Power Bank Box
		powerBankBox = new PowerBankBox(backgroundColor, headingFontSize, bodyFontSize, 5);
		contentPanel.add(powerBankBox);
		
		// User Input
		JPanel userInputPanel = createUserInputPanel();
		contentPanel.add(userInputPanel);
		
		return contentPanel;
	}
	
	private JPanel createPlaceHolderBox(Dimension size) {
		JPanel placeHolderBoxPanel = new JPanel(new BorderLayout());
		placeHolderBoxPanel.setBackground(null);
		placeHolderBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		placeHolderBoxPanel.setPreferredSize(size);
		
		return placeHolderBoxPanel;
	}
	
	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		
		return pageLabel;
	}
	
	private JPanel createTextWithComponentToAdd(String text, int fontSize) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		panel.setBackground(null);
		
		JLabel textLabel = createPageLabel(Font.BOLD, fontSize);
		textLabel.setText(text);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		panel.add(textLabel);
		
		return panel;
	}
	
}
