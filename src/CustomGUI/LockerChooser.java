package CustomGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Database.DatabaseConnector;
import Utils.Utils;

public class LockerChooser extends JDialog {
	
	public interface LockerChooserAction {
		public void onLockerChosen(int selectedLockerNumber);
	}
	
	private Color backgroundColor, lineColor, fontColor, unavaliableBackgroundColor, unavaliableFontColor;
	
	private LockerChooserAction lockerChooserAction;
	
	public LockerChooser(JFrame frame, 
			Color backgroundColor, Color lineColor, Color fontColor, 
			Color unavaliableBackgroundColor, Color unavaliableFontColor) {
		super(frame, "เลือก locker", true);
		
		this.backgroundColor = backgroundColor;
		this.lineColor = lineColor;
		this.fontColor = fontColor;
		this.unavaliableBackgroundColor = unavaliableBackgroundColor;
		this.unavaliableFontColor = unavaliableFontColor;
		this.lockerChooserAction = null;
		
		setSize(600, 400);
		setBackground(backgroundColor);
	}
	
	public void setLockerChooserAction(LockerChooserAction lockerChooserAction) {
		this.lockerChooserAction = lockerChooserAction;
	}
	
	public void showLockerChooser(String address, boolean rentLocker) {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		this.add(mainPanel);
		
		// Address
		JPanel wrapperAddressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		wrapperAddressPanel.setBackground(null);
		mainPanel.add(wrapperAddressPanel, BorderLayout.NORTH);
		
		JLabel addressLabel = createPageLabel(Font.BOLD, 24);
		addressLabel.setText(address);
		wrapperAddressPanel.add(addressLabel);
		
		// Get Unavailable Locker
		int totalLocker = 40;
		boolean[] isUnavailable = new boolean[totalLocker + 1]; // 0th index doesn't count
		ArrayList<Integer> unavailableLockerList = new ArrayList<Integer>();
		if (rentLocker) unavailableLockerList = DatabaseConnector.getUnavailableRentLocker(address);
		else unavailableLockerList = DatabaseConnector.getUnavailableDepositLocker(address);
		if (unavailableLockerList == null) {
			System.err.println("Couldn't get unavailable locker. Unable to show locker chooser");
			dispose();
			return;
		}
		
		for (int unavailableLocker : unavailableLockerList) {
			if (unavailableLocker > totalLocker) {
				System.err.println("Warning: this locker number exceeds the number of total locker.");
				continue;
			}
			isUnavailable[unavailableLocker] = true;
		}
		
		JPanel lockerGridPanel = createLockerGrid(isUnavailable);
		mainPanel.add(lockerGridPanel, BorderLayout.CENTER);
		
		// Show
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel createLockerGrid(boolean[] isUnavailable) {
		JPanel lockerGridPanel = new JPanel(new GridLayout(0, 8));
		lockerGridPanel.setBackground(null);
		
		int totalLocker = isUnavailable.length - 1;
		int fontSize = 16;
		
		for (int i = 1; i <= totalLocker; i++) {
			JPanel lockerBox = createLockerBox(i, isUnavailable[i], fontSize);
			lockerGridPanel.add(lockerBox);
		}
		
		return lockerGridPanel;
	}
	
	private JPanel createLockerBox(int lockerNumber, boolean isUnavaiable, int fontSize) {
		
		Color boxBackgroundColor = (!isUnavaiable) ? backgroundColor : unavaliableBackgroundColor;
		Color boxFontColor = (!isUnavaiable) ? fontColor : unavaliableFontColor;
		
		JPanel lockerBox = new JPanel(new BorderLayout());
		lockerBox.setBackground(boxBackgroundColor);
		lockerBox.setBorder(Utils.createPaddingBorder(lineColor, 5));
		
		// Locker Number
		JButton lockerNumberButton = Utils.createNoBackgroundButton(String.valueOf(lockerNumber), boxFontColor);
		lockerBox.add(lockerNumberButton, BorderLayout.CENTER);
		if (isUnavaiable) {
			lockerNumberButton.setEnabled(false);
		}
		else {
			lockerNumberButton.addActionListener(e -> {
				if (lockerChooserAction != null) lockerChooserAction.onLockerChosen(lockerNumber);
				dispose();
			}); 
		}
		
		return lockerBox;
	}
	
	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return pageLabel;
	}
	
}
