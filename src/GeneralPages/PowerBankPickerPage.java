package GeneralPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.CustomDropDown;
import Database.DatabaseConnector;
import Struct.PowerBank;
import UserApp.UserMainFrame;
import UserApp.Pages.UserDeposit;
import Utils.Utils;

public class PowerBankPickerPage extends JPanel {

	private UserMainFrame userMainFrame;
	private Color backgroundColor, mainColor, menuColor;

	private JLabel titleLabel;
	private CustomDropDown<Object> brandDropDown, modelDropDown;

	private UserDeposit userDepositPage;

	public PowerBankPickerPage(UserMainFrame userMainFrame, Color backgroundColor, Color mainColor, Color menuColor,
			UserDeposit userDepositPage) {

		super(new GridBagLayout());
		this.userMainFrame = userMainFrame;
		this.backgroundColor = backgroundColor;
		this.mainColor = mainColor;
		this.menuColor = menuColor;
		this.userDepositPage = userDepositPage;

		setBackground(backgroundColor);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 10, 0);

		// Title
		gbc.gridy = 0;
		titleLabel = new JLabel();
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		titleLabel.setBackground(backgroundColor);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		this.add(titleLabel, gbc);
		
		// Power Bank Pick
		JPanel powerBankPickPanel = createPowerBankPickPanel();
		gbc.gridy = 1;
		this.add(powerBankPickPanel, gbc);
	}

	private JPanel createPowerBankPickPanel() {
		JPanel powerBankPickPanel = new JPanel();
		powerBankPickPanel.setLayout(new BoxLayout(powerBankPickPanel, BoxLayout.Y_AXIS));
		powerBankPickPanel.setBackground(null);

		int topicFontSize = 24;
		int bodyFontSize = 20;
		int margin = 10;

		// Power Bank Pick
		JLabel powerBankPickLabel = createPageLabel(Font.BOLD, topicFontSize);
		powerBankPickLabel.setText("เลือก Power Bank ที่มีอยู่");
		powerBankPickPanel.add(powerBankPickLabel);
		powerBankPickPanel.add(Box.createVerticalStrut(margin));

		// Brand Select
		JPanel brandSelectPanel = createTextWithDropDownToAdd("Brand:", bodyFontSize);
		powerBankPickPanel.add(brandSelectPanel);
		powerBankPickPanel.add(Box.createVerticalStrut(margin));

		brandDropDown = new CustomDropDown<Object>(DatabaseConnector.getDistinctBrand().toArray(), backgroundColor,
				mainColor);
		brandSelectPanel.add(brandDropDown);

		// Model Select
		JPanel modelSelectPanel = createTextWithDropDownToAdd("Model:", bodyFontSize);
		powerBankPickPanel.add(modelSelectPanel);
		powerBankPickPanel.add(Box.createVerticalStrut(margin));

		ArrayList<String> modelList = DatabaseConnector.getModelFromBrand(brandDropDown.getSelectedItem().toString());
		modelDropDown = new CustomDropDown<Object>(modelList.toArray(), backgroundColor, mainColor);
		modelSelectPanel.add(modelDropDown);

		brandDropDown.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// change model item list from brand
				String selectedBrand = e.getItem().toString();
				ArrayList<String> modelListFromBrand = DatabaseConnector.getModelFromBrand(selectedBrand);
				modelDropDown.removeAllItems();
				for (String model : modelListFromBrand)
					modelDropDown.addItem(model);
			}
		});

		// Next Button
		Dimension buttonSize = new Dimension(200, 40);
		JButton nextButton = Utils.createColorBackgroundButton("ต่อไป", menuColor, Color.WHITE, bodyFontSize);
		nextButton.setPreferredSize(buttonSize);
		nextButton.setMaximumSize(buttonSize);
		nextButton.setAlignmentX(CENTER_ALIGNMENT);
		powerBankPickPanel.add(nextButton);

		nextButton.addActionListener(e -> {
			PowerBank powerBank = DatabaseConnector.getPowerBank(brandDropDown.getSelectedItem().toString(),
					modelDropDown.getSelectedItem().toString(), true);
			if (powerBank == null) {
				System.err.println("Couldn't get a power bank from database. Unable to go to the next page.");
				return;
			}

			if (userMainFrame != null && userDepositPage != null) {
				userDepositPage.updatePage(powerBank);
				userMainFrame.changeCard(3, 3);
			}
		});

		powerBankPickPanel.add(Box.createVerticalStrut(10));

		// Or
		JLabel orLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		orLabel.setText("หรือ");
		powerBankPickPanel.add(orLabel);
		powerBankPickPanel.add(Box.createVerticalStrut(margin));

		// Add new power bank
		JButton addNewPowerBankButton = Utils.createNoBackgroundButton("เพิ่ม Power Bank ใหม่", mainColor,
				bodyFontSize);
		addNewPowerBankButton.setAlignmentX(CENTER_ALIGNMENT);
		powerBankPickPanel.add(addNewPowerBankButton);

		addNewPowerBankButton.addActionListener(e -> {
			if (userMainFrame != null)
				userMainFrame.changeCard(3, 12);
		});

		return powerBankPickPanel;
	}

	private JPanel createTextWithDropDownToAdd(String text, int fontSize) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setAlignmentX(CENTER_ALIGNMENT);
		panel.setBackground(null);

		JLabel textLabel = createPageLabel(Font.PLAIN, fontSize);
		textLabel.setText(text);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		panel.add(textLabel);

		return panel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

		return pageLabel;
	}

	public void updateTitleLabel(String title) {
		titleLabel.setText(title);
	}

}