package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CustomGUI.NameDropDownHolder;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserList extends JPanel {
	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;
	
	// Filter components
	private NameDropDownHolder typeDropDownHolder, orderByDropDownHolder;
	private JButton pastDueButton;
	
	public UserList(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());
		
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		
		setBackground(backgroundColor);
		
		// Top Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(topPanel, BorderLayout.NORTH);
		
		// Title
		JLabel titleLabel = createPageLabel(Font.BOLD, 28);
		titleLabel.setText("รายการของฉัน");
		topPanel.add(titleLabel);
		
		topPanel.add(Box.createVerticalStrut(10));
		
		// Filter Panel
		JPanel filterPanel = createFilterPanel();
		topPanel.add(filterPanel);
		
		// topPanel.add(Box.createVerticalGlue());
	}
	
	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel(new GridBagLayout());
		filterPanel.setAlignmentX(LEFT_ALIGNMENT);
		filterPanel.setAlignmentY(TOP_ALIGNMENT);
		filterPanel.setBackground(null);
		
		int fontSize = 16;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		
		// Type (Rented or Deposited)
		String[] typeList = new String[] { "การเช่า", "การฝาก" };
		typeDropDownHolder = new NameDropDownHolder(typeList, "ประเภท:", fontSize, fontSize, backgroundColor, mainColor);
		gbc.gridx = 0;
		gbc.gridy = 0;
		filterPanel.add(typeDropDownHolder, gbc);
		
		// Show Past Due
		JPanel pastDuePanel = createTextWithComponentToAdd("เลยกำหนด", fontSize);
		gbc.gridx = 1;
		gbc.gridy = 0;
		filterPanel.add(pastDuePanel, gbc);
		
		ImageIcon unCheckIcon = Utils.createImageIcon("/icons/uncheck_icon_24.png", "Uncheck Icon");
		pastDueButton = Utils.createIconButton(unCheckIcon);
		pastDuePanel.add(pastDueButton);
		
		return filterPanel;
	}
	
	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		// pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		
		return pageLabel;
	}
	
	private JPanel createTextWithComponentToAdd(String text, int fontSize) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panel.setBackground(null);
		
		JLabel textLabel = createPageLabel(Font.BOLD, fontSize);
		textLabel.setText(text);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		panel.add(textLabel);
		
		return panel;
	}
	
}