package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import Struct.CheckHistory;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserHistoryDetail extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	// TODO: North Components
	private JLabel typeIconLabel, typeTitleLabel, transactionDateLabel;
	private JLabel statusIconLabel, statusLabel;

	public UserHistoryDetail(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;

		setBackground(backgroundColor);

		// Top Panel
		JPanel topPanel = createTopPanel();
		this.add(topPanel, BorderLayout.NORTH);

	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(null);
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		int headingFontSize = 24;
		int bodyFontSize = 18;
		int margin = 10;
		
		// Title
		JLabel titleLabel = createPageLabel("ประวัติการใช้งาน", Font.BOLD, 28);
		topPanel.add(titleLabel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		// Back Button
		ImageIcon backIcon = Utils.createImageIcon("/icons/back_icon_36.png", "Back Icon");
		JButton backButton = Utils.createIconButton(backIcon);
		topPanel.add(backButton);
		topPanel.add(Box.createVerticalStrut(margin));
		
		backButton.addActionListener(e -> {
			mainFrame.changeCard(5, 5);
		});
		
		// Use Detail
		int useDetailPadding = 5;
		JLabel useDetailLabel = createPageLabel("รายละเอียดการใช้งาน", Font.BOLD, headingFontSize);
		useDetailLabel.setForeground(Color.WHITE);
		useDetailLabel.setBackground(mainColor);
		useDetailLabel.setBorder(BorderFactory.createEmptyBorder(useDetailPadding, useDetailPadding, useDetailPadding, useDetailPadding));
		topPanel.add(useDetailLabel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		// History Type and Status
		JPanel typeStatusPanel = new JPanel(new BorderLayout());
		typeStatusPanel.setBackground(null);
		typeStatusPanel.setAlignmentX(LEFT_ALIGNMENT);
		topPanel.add(typeStatusPanel);
		topPanel.add(Box.createVerticalStrut(margin));
		
		int detailMargin = 5;
		
		// History Type (check, rent, deposit)
		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, detailMargin, 0));
		typePanel.setBackground(null);
		typeStatusPanel.add(typePanel, BorderLayout.WEST);

		typeIconLabel = new JLabel();
		typePanel.add(typeIconLabel);
		
		JPanel typeDetailPanel = new JPanel();
		typeDetailPanel.setLayout(new BoxLayout(typeDetailPanel, BoxLayout.Y_AXIS));
		typeDetailPanel.setBackground(null);
		typePanel.add(typeDetailPanel);
		
		typeTitleLabel = createPageLabel(Font.BOLD, bodyFontSize);
		typeTitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		typeDetailPanel.add(typeTitleLabel);
		typeDetailPanel.add(Box.createVerticalStrut(detailMargin));
		
		transactionDateLabel = createPageLabel(Font.PLAIN, bodyFontSize);
		transactionDateLabel.setAlignmentX(CENTER_ALIGNMENT);
		typeDetailPanel.add(transactionDateLabel);
		
		// Status
		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, detailMargin, 0));
		statusPanel.setBackground(null);
		typeStatusPanel.add(statusPanel, BorderLayout.CENTER);
		
		statusPanel.add(Box.createHorizontalStrut(50));
		
		statusIconLabel = new JLabel();
		statusPanel.add(statusIconLabel);
		
		statusLabel = createPageLabel(Font.BOLD, headingFontSize);
		statusPanel.add(statusLabel);
		
		// Line separator
		JSeparator lineSeparator = createPageSeparator();
		topPanel.add(lineSeparator);
		
		return topPanel;
	}

	public void updatePage(CheckHistory checkHistory) {
		// update top panel
		String imagePath = (checkHistory.approve) ? "/icons/search_icon_56.png" : "/icons/warning_icon_56.png";
		ImageIcon checkIcon = Utils.createImageIcon(imagePath, "Check Icon");
		typeIconLabel.setIcon(checkIcon);
		
		typeTitleLabel.setText("ตรวจสอบ Power Bank");
		transactionDateLabel.setText(Utils.getDateFormat(checkHistory.checkTime.toLocalDate(), "dd/MM/yyyy"));
		
		String statusImagePath = (checkHistory.approve) ? "/icons/check_icon_56.png" : "/icons/cancel_icon_56.png";
		ImageIcon statusIcon = Utils.createImageIcon(statusImagePath, "Status Icon");
		statusIconLabel.setIcon(statusIcon);
		
		String statusText = (checkHistory.approve) ? "ตรวจสอบแล้วผ่าน" : "ตรวจสอบแล้วไม่ผ่าน";
		Color statusColor = (checkHistory.approve) ? new Color(45, 172, 51) : new Color(255, 51, 51);
		statusLabel.setText(statusText);
		statusLabel.setForeground(statusColor);
	}
	
	private void createPageSeparator(JPanel gridBagContainerPanel, int row, int columnSpan) {
		JSeparator separator = createPageSeparator();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = columnSpan;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gridBagContainerPanel.add(separator, gbc);
	}
	
	private JSeparator createPageSeparator() {
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.HORIZONTAL);
		separator.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		return separator;
	}
	
	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		pageLabel.setOpaque(true);

		return pageLabel;
	}

	private JLabel createPageLabel(int fontStyle, int fontSize) {
		return createPageLabel("", fontStyle, fontSize);
	}

}
