package LockerUnlock.Pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.CustomDropDown;
import LockerUnlock.LockerUnlockMainFrame;
import Utils.Utils;

public class LockerAddressSelection extends JPanel {

	private LockerUnlockMainFrame mainFrame;
	private LockerHome homePage;
	
	// Components
	private CustomDropDown<String> addressSelectDropdown;

	public LockerAddressSelection(LockerUnlockMainFrame mainFrame, LockerHome homePage) {
		super(new GridBagLayout());
		this.mainFrame = mainFrame;
		this.homePage = homePage;

		setBackground(LockerUnlockMainFrame.BACKGROUND_COLOR);

		createAddressSelectionPage();
	}

	private void createAddressSelectionPage() {
		GridBagConstraints gbc = getDefaultGridBagConstraints(30, 30);
		int row = 0;

		int headingFontSize = 36;
		int bodyFontSize = 28;

		// Title
		JLabel titleLabel = createPageLabel("เลือกสถานที่", Font.BOLD, headingFontSize);
		gbc.gridy = row++;
		this.add(titleLabel, gbc);

		// Address Selection
		String[] addressArray = new String[] { "สนามบินสุวรรณภูมิ", "สนามบินดอนเมือง", "สนามบินภูเก็ต",
				"สนามบินเชียงใหม่", "สนามบินแม่ฟ้าหลวง", "สนามบินหาดใหญ่", "สนามบินขอนแก่น" };
		addressSelectDropdown = new CustomDropDown<String>(addressArray, LockerUnlockMainFrame.PRIMARY_FONT_COLOR,
				LockerUnlockMainFrame.PRIMARY_COLOR, 2);
		addressSelectDropdown.setPreferredSize(new Dimension(400, addressSelectDropdown.getPreferredSize().height));
		addressSelectDropdown.setFontSize(20);
		gbc.gridy = row++;
		this.add(addressSelectDropdown, gbc);

		// Select button
		Dimension buttonSize = new Dimension(200, 60);
		JButton selectButton = Utils.createBorderBackgroundButton("เลือก", LockerUnlockMainFrame.PRIMARY_COLOR,
				LockerUnlockMainFrame.PRIMARY_FONT_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, bodyFontSize, 2,
				buttonSize);
		gbc.gridy = row++;
		this.add(selectButton, gbc);
		
		selectButton.addActionListener(e -> {
			mainFrame.setAddress(addressSelectDropdown.getSelectedItem().toString());
			homePage.updateAddress(mainFrame.getAddress());
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
