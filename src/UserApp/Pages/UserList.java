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
	private ImageIcon unCheckedIcon, checkedIcon;
	private NameDropDownHolder typeDropDownHolder, orderByDropDownHolder;
	private JButton pastDueButton, ascButton, descButton;

	private boolean showPastDue;
	private boolean orderAsc, orderDesc;

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
		int margin = 10;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(margin, margin, margin, margin);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		// Type (Rented or Deposited)
		String[] typeList = new String[] { "ทั้งหมด", "การเช่า", "การฝาก" };
		typeDropDownHolder = new NameDropDownHolder(typeList, "ประเภท:", fontSize, fontSize, backgroundColor,
				mainColor);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		filterPanel.add(typeDropDownHolder, gbc);

		// Show Past Due
		JPanel pastDuePanel = createTextWithComponentToAdd("เลยกำหนด", fontSize);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 2.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		filterPanel.add(pastDuePanel, gbc);

		checkedIcon = Utils.createImageIcon("/icons/checked_icon_24.png", "Checked Icon");
		unCheckedIcon = Utils.createImageIcon("/icons/uncheck_icon_24.png", "Uncheck Icon");
		pastDueButton = Utils.createIconButton(unCheckedIcon);
		pastDuePanel.add(pastDueButton);

		showPastDue = false;
		pastDueButton.addActionListener(e -> {
			showPastDue = !showPastDue;
			if (showPastDue)
				pastDueButton.setIcon(checkedIcon);
			else
				pastDueButton.setIcon(unCheckedIcon);
		});

		// Order By
		String[] orderByList = new String[] { "ไม่มี", "ประเภท", "สถานะ", "สถานที่รับ/คืน", "วันที่รับ/คืน" };
		orderByDropDownHolder = new NameDropDownHolder(orderByList, "จัดเรียงตาม:", fontSize, fontSize, backgroundColor,
				mainColor);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		filterPanel.add(orderByDropDownHolder, gbc);

		// Order type
		JPanel ordertypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		ordertypePanel.setBackground(backgroundColor);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		filterPanel.add(ordertypePanel, gbc);

		ascButton = Utils.createBorderButton("น้อยไปมาก", Color.BLACK, Color.BLACK, fontSize);
		ordertypePanel.add(ascButton);

		descButton = Utils.createBorderButton("มากไปน้อย", Color.BLACK, Color.BLACK, fontSize);
		ordertypePanel.add(descButton);

		ascButton.addActionListener(e -> {
			ascButton.setBackground(mainColor);
			descButton.setBackground(null);

			ascButton.setForeground(Color.WHITE);
			descButton.setForeground(Color.BLACK);

			orderAsc = true;
			orderDesc = false;
		});

		descButton.addActionListener(e -> {
			descButton.setBackground(mainColor);
			ascButton.setBackground(null);

			descButton.setForeground(Color.WHITE);
			ascButton.setForeground(Color.BLACK);

			orderAsc = false;
			orderDesc = true;
		});

		// Apply Filter
		JButton applyFilterButton = Utils.createNoBackgroundButton("ยืนยัน", mainColor);
		applyFilterButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		gbc.gridx = 2;
		gbc.gridy = 1;
		filterPanel.add(applyFilterButton, gbc);

		applyFilterButton.addActionListener(e -> {
			applyFilter();
		});

		// Clear Filter
		JButton clearFilterButton = Utils.createNoBackgroundButton("ล้าง filter", new Color(36, 160, 237));
		clearFilterButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		filterPanel.add(clearFilterButton, gbc);

		clearFilterButton.addActionListener(e -> {
			clearFilter();
			applyFilter();
		});
		
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.weightx = 2.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		filterPanel.add(Box.createHorizontalGlue(), gbc);

		return filterPanel;
	}
	
	private void clearFilter() {
		// TODO: Clear Filter
		
		// Clear Type
		typeDropDownHolder.setSelectedValue(0);
		
		// Clear Past Due Date
		showPastDue = false;
		pastDueButton.setIcon(unCheckedIcon);
		
		// Clear Order By
		orderByDropDownHolder.setSelectedValue(0);
		
		ascButton.setBackground(null);
		descButton.setBackground(null);

		ascButton.setForeground(Color.BLACK);
		descButton.setForeground(Color.BLACK);

		orderAsc = false;
		orderDesc = false;
	}
	
	private void applyFilter() {
		
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