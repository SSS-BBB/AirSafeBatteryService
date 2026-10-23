package LockerUnlock.Pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import LockerUnlock.LockerUnlockMainFrame;
import Utils.Utils;

public class LockerStatus extends JPanel {

	private LockerUnlockMainFrame mainFrame;

	// Components
	private JLabel lockerNumberLabel;
	private JLabel instructionLabel;
	private JLabel warningLabel;

	public LockerStatus(LockerUnlockMainFrame mainFrame) {
		super(new GridBagLayout());
		this.mainFrame = mainFrame;

		setBackground(LockerUnlockMainFrame.BACKGROUND_COLOR);

		createStatusPage();
	}

	public void updateStatusPage(int lockerNumber, String instruction, String warning) {
		
		mainFrame.userDetail = null;
		
		// JLabel lockerNumberLabel, instructionLabel, warningLabel;
		
		// Locker Number
		if (lockerNumberLabel != null) {
			lockerNumberLabel.setText("Locker หมายเลข " + String.valueOf(lockerNumber));
		} 
		else {
			System.err.println("Null lockerNumberLabel. Unable to update locker number label.");
		}
		
		// Instruction
		if (instructionLabel != null) {
			instructionLabel.setText(instruction);
		} 
		else {
			System.err.println("Null instructionLabel. Unable to update instruction label.");
		}
		
		// Warning
		if (warningLabel != null) {
			warningLabel.setText(warning);
		} 
		else {
			System.err.println("Null warningLabel. Unable to update warning label.");
		}
	}

	private void createStatusPage() {
		int margin = 14;
		GridBagConstraints gbc = getDefaultGridBagConstraints(margin, margin);
		int row = 0;

		int headingFontSize = 36;
		int bodyFontSize = 32;

		// Status Icon
		ImageIcon statusIcon = Utils.createImageIcon("/icons/check_green_icon_168.png", "Status Icon");
		JLabel statusIconLabel = new JLabel(statusIcon);
		gbc.gridy = row++;
		this.add(statusIconLabel, gbc);

		// Address
		JLabel addressLabel = createPageLabel(mainFrame.getAddress(), Font.BOLD, headingFontSize);
		gbc.gridy = row++;
		this.add(addressLabel, gbc);

		// Unlocked Locker
		JPanel unlockedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
		unlockedPanel.setBackground(null);
		gbc.gridy = row++;
		this.add(unlockedPanel, gbc);

		lockerNumberLabel = createPageLabel("", Font.PLAIN, headingFontSize);
		unlockedPanel.add(lockerNumberLabel);

		JLabel getUnlocked = createPageLabel(" ถูกปลดล็อก", Font.PLAIN, headingFontSize);
		getUnlocked.setForeground(LockerUnlockMainFrame.SUCCESSFUL_COLOR);
		unlockedPanel.add(getUnlocked);

		// Instruction
		String instructionText = "";
		instructionLabel = createPageLabel(instructionText, Font.PLAIN, bodyFontSize);
		gbc.gridy = row++;
		this.add(instructionLabel, gbc);

		// Warning
		String warningText = "";
		warningLabel = createPageLabel(warningText, Font.PLAIN, bodyFontSize);
		warningLabel.setForeground(LockerUnlockMainFrame.FAILED_COLOR);
		gbc.gridy = row++;
		this.add(warningLabel, gbc);
		
		// Back Button
		Dimension buttonSize = new Dimension(200, 60);
		JButton backButton = Utils.createBorderBackgroundButton("กลับ", LockerUnlockMainFrame.PRIMARY_FONT_COLOR,
				Color.BLACK, LockerUnlockMainFrame.PRIMARY_COLOR, bodyFontSize, 2, buttonSize);
		gbc.gridy = row++;
		this.add(backButton, gbc);
		
		backButton.addActionListener(e -> {
			mainFrame.changeCard(LockerUnlockMainFrame.HOME_PAGE);
		});
	}

	private GridBagConstraints getDefaultGridBagConstraints(int verticleMargin, int horizontalMargin) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(verticleMargin / 2, horizontalMargin / 2, verticleMargin / 2, horizontalMargin / 2);

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
