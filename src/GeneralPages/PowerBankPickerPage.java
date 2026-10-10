package GeneralPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import UserApp.UserMainFrame;
import Utils.Utils;

public class PowerBankPickerPage extends JPanel {

	private UserMainFrame userMainFrame;
	private Color backgroundColor, mainColor, menuColor;

	private JLabel titleLabel;
	private CustomDropDown<Object> brandDropDown, modelDropDown;

	public PowerBankPickerPage(UserMainFrame userMainFrame, Color backgroundColor, Color mainColor, Color menuColor) {

		super(new BorderLayout());
		this.userMainFrame = userMainFrame;
		this.backgroundColor = backgroundColor;
		this.mainColor = mainColor;
		this.menuColor = menuColor;

		setBackground(backgroundColor);

		// Title
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titlePanel.setBackground(backgroundColor);
		this.add(titlePanel, BorderLayout.NORTH);

		titleLabel = new JLabel();
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		titleLabel.setBackground(backgroundColor);
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		titlePanel.add(titleLabel);
		
		JPanel powerBankPickPanel = createPowerBankPickPanel();
		this.add(powerBankPickPanel, BorderLayout.CENTER);
		
		// this.add(Box.createVerticalStrut(200), BorderLayout.SOUTH);
	}
	
	private JPanel createPowerBankPickPanel() {
		JPanel powerBankPickPanel = new JPanel();
		powerBankPickPanel.setLayout(new BoxLayout(powerBankPickPanel, BoxLayout.Y_AXIS));
		powerBankPickPanel.setBackground(null);
		
		int topicFontSize = 24;
		int bodyFontSize = 20;
		
		// Power Bank Pick
		JLabel powerBankPickLabel = createPageLabel(Font.BOLD, topicFontSize);
		powerBankPickLabel.setText("เลือก Power Bank ที่มีอยู่");
		powerBankPickPanel.add(powerBankPickLabel);
		
		// Brand Select
		JPanel brandSelectPanel = createTextWithDropDownToAdd("Brand:", bodyFontSize);
		powerBankPickPanel.add(brandSelectPanel);
		
		brandDropDown = new CustomDropDown<Object>(DatabaseConnector.getDistinctBrand().toArray(), backgroundColor, mainColor);
		brandSelectPanel.add(brandDropDown);
		
		// Model Select
		JPanel modelSelectPanel = createTextWithDropDownToAdd("Model:", bodyFontSize);
		powerBankPickPanel.add(modelSelectPanel);
		
		ArrayList<String> modelList = DatabaseConnector.getModelFromBrand(brandDropDown.getSelectedItem().toString());
		modelDropDown = new CustomDropDown<Object>(modelList.toArray(), backgroundColor, mainColor);
		modelSelectPanel.add(modelDropDown);
		
		brandDropDown.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// change model item list from brand
				String selectedBrand = e.getItem().toString();
				ArrayList<String> modelListFromBrand = DatabaseConnector.getModelFromBrand(selectedBrand);
				modelDropDown.removeAllItems();
				for (String model : modelListFromBrand) modelDropDown.addItem(model);
			}
		});
		
		// Next Button
		Dimension buttonSize = new Dimension(200, 40);
		JButton nextButton = Utils.createColorBackgroundButton("ต่อไป", menuColor, Color.WHITE, bodyFontSize);
		nextButton.setPreferredSize(buttonSize);
		nextButton.setMaximumSize(buttonSize);
		nextButton.setAlignmentX(CENTER_ALIGNMENT);
		powerBankPickPanel.add(nextButton);
		
		powerBankPickPanel.add(Box.createVerticalStrut(10));
		
		// Or
		JLabel orLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		orLabel.setText("หรือ");
		powerBankPickPanel.add(orLabel);
		
		// Add new power bank
		JButton addNewPowerBankButton = Utils.createNoBackgroundButton("เพิ่ม Power Bank ใหม่", mainColor, bodyFontSize);
		addNewPowerBankButton.setAlignmentX(CENTER_ALIGNMENT);
		powerBankPickPanel.add(addNewPowerBankButton);
		
		powerBankPickPanel.add(Box.createVerticalStrut(250));
		
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