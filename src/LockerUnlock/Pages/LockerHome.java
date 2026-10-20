package LockerUnlock.Pages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import LockerUnlock.LockerUnlockMainFrame;
import Utils.Utils;

public class LockerHome extends JPanel {
	
	private LockerUnlockMainFrame mainFrame;
	
	public LockerHome(LockerUnlockMainFrame mainFrame) {
		super(new BorderLayout());
		this.mainFrame = mainFrame;
		
		setBackground(LockerUnlockMainFrame.BACKGROUND_COLOR);
		
		JPanel topPanel = createTopPanel();
		this.add(topPanel, BorderLayout.NORTH);
		
		JPanel buttonPanel = createButtonPanel();
		this.add(buttonPanel, BorderLayout.CENTER);
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		// buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(null);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		int fontSize = 32;
		int lineThickness = 2;
		Dimension buttonSize = new Dimension(200, 60);
		int margin = 10;
		
		GridBagConstraints gbc = getDefaultGridBagConstraints(margin);
		gbc.gridx = 0;
		
		// Rent
		JButton rentButton = Utils.createBorderBackgroundButton("เช่า", 
				LockerUnlockMainFrame.PRIMARY_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, 
				fontSize, lineThickness, buttonSize);
		rentButton.setAlignmentX(CENTER_ALIGNMENT);
		gbc.gridy = 0;
		gbc.weighty = 0.25;
		buttonPanel.add(rentButton, gbc);
		// buttonPanel.add(Box.createVerticalStrut(margin));
		
		// Deposit
		JButton depositButton = Utils.createBorderBackgroundButton("ฝาก", 
				LockerUnlockMainFrame.PRIMARY_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, 
				fontSize, lineThickness, buttonSize);
		depositButton.setAlignmentX(CENTER_ALIGNMENT);
		gbc.gridy = 1;
		gbc.weighty = 0.0;
		buttonPanel.add(depositButton, gbc);
		
		createGridBagBottomGlue(buttonPanel, 2, margin);
		
		// Action Listener
		rentButton.addActionListener(e -> {
			onTypeButtonClicked(LockerUnlockMainFrame.RENT_LOCKER);
		});
		
		depositButton.addActionListener(e -> {
			onTypeButtonClicked(LockerUnlockMainFrame.DEPOSIT_LOCKER);
		});
		
		return buttonPanel;
	}
	
	private void onTypeButtonClicked(int lockerType) {
		mainFrame.lockerType = lockerType;
		
		// TODO: change to login page
	}
	
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		int headingFontSize = 40;
		int bodyFontSize = 36;
		int margin = 8;
		
		// Title
		JLabel titleLabel = createPageLabel("AirSafe Battery Service", Font.BOLD, headingFontSize);
		topPanel.add(titleLabel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		// Address
		JLabel addressLabel = createPageLabel(mainFrame.getAddress(), Font.BOLD, bodyFontSize);
		topPanel.add(addressLabel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		return topPanel;
	}
	
	private void createGridBagBottomGlue(JPanel gridBagPanel, int row, int margin) {
		// make components go from top to bottom
		GridBagConstraints gbc = getDefaultGridBagConstraints(margin);
		gbc.gridy = row;
		gbc.weighty = 1.0;
		gbc.gridx = 0;

		gridBagPanel.add(Box.createGlue(), gbc);
	}

	private GridBagConstraints getDefaultGridBagConstraints(int margin) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(margin/2, 0, margin/2, 0);

		return gbc;
	}

	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setForeground(LockerUnlockMainFrame.PRIMARY_FONT_COLOR);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setOpaque(true);

		return pageLabel;
	}
	
}
