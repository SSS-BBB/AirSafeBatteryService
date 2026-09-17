package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.JTextField;

import UserApp.UserMainFrame;
import Utils.Utils;

public class UserCheck extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor;
	
	private JPanel checkStatusPanel, statusPanel;
	private JLabel statusIconLabel, statusLabelTitle, statusLabelDetail, whLabel;

	public UserCheck(UserMainFrame mainFrame, Color backgroundColor) {
		super();

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;

		createCheckPage();
		createCheckStatusPage(false);
	}

	private void createCheckPage() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(backgroundColor);

		JLabel checkTitleLabel = new JLabel("ตรวจสอบ Power Bank");
		checkTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		checkTitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 30, 0));
		this.add(checkTitleLabel);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(backgroundColor);
		infoPanel.setBorder(
				Utils.createPaddingBorder(Color.BLACK, 1, new Insets(25, 25, 25, 25), new Insets(20, 25, 30, 0)));
		this.add(infoPanel);

		JLabel infoTitleLabel = new JLabel("กรอกข้อมูล Power Bank ของคุณ");
		infoTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		infoTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		infoPanel.add(infoTitleLabel);

		// Capacity
		JLabel capacityTitleLabel = new JLabel("ความจุ (mAh)");
		capacityTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		capacityTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		infoPanel.add(capacityTitleLabel);

		JTextField capacityTextField = Utils.createAppTextField(backgroundColor);
		infoPanel.add(capacityTextField);

		// Voltage
		JLabel voltageTitleLabel = new JLabel("แรงดันไฟฟ้า (V)");
		voltageTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		voltageTitleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 8, 0));
		infoPanel.add(voltageTitleLabel);

		JTextField voltageTextField = Utils.createAppTextField(backgroundColor);
		infoPanel.add(voltageTextField);

		// Or
		JLabel alternativeLabel = new JLabel("หรือ กรอก Wh โดยตรง");
		alternativeLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		alternativeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		infoPanel.add(alternativeLabel);

		// Energy
		JLabel energyTitleLabel = new JLabel("พลังงาน (Wh)");
		energyTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		energyTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		infoPanel.add(energyTitleLabel);

		JTextField energyTextField = Utils.createAppTextField(backgroundColor);
		infoPanel.add(energyTextField);
		infoPanel.add(Box.createRigidArea(new Dimension(0, 25)));

		// Check Button
		JButton checkButton = new JButton("ตรวจสอบ");
		checkButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		checkButton.setBackground(Color.BLUE);
		checkButton.setForeground(Color.WHITE);
		checkButton.setFocusPainted(false);
		checkButton.setMaximumSize(new Dimension(350, 50));
		checkButton.setPreferredSize(new Dimension(350, 50));
		checkButton.setAlignmentX(LEFT_ALIGNMENT);
		checkButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkButton.addActionListener(e -> {
			// TODO: check conditions from database

			double wh = Double.parseDouble(energyTextField.getText());

			if (wh > 100) {
				updateStatusPanel(wh, false);
			}

			else {
				updateStatusPanel(wh, true);
			}

			mainFrame.changeCard(1, 7);
		});
		infoPanel.add(checkButton);
	}

	private void createCheckStatusPage(boolean approveStatus) {

		checkStatusPanel = new JPanel(new BorderLayout());
		checkStatusPanel.setBackground(backgroundColor);
		mainFrame.addCard(checkStatusPanel, 7);

		// North (Status Title)
		JLabel checkStatusTitleLabel = new JLabel("ผลการตรวจสอบ");
		checkStatusTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		checkStatusTitleLabel.setForeground(Color.BLACK);
		checkStatusTitleLabel.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 0));
		checkStatusPanel.add(checkStatusTitleLabel, BorderLayout.NORTH);

		// Line-Start and Line-End Empty Space
		JPanel fillStart = new JPanel();
		fillStart.add(Box.createRigidArea(new Dimension(0, 0)));
		fillStart.setOpaque(false);
		checkStatusPanel.add(fillStart, BorderLayout.LINE_START);

		JPanel fillEnd = new JPanel();
		fillEnd.add(Box.createRigidArea(new Dimension(20, 0)));
		fillEnd.setOpaque(false);
		checkStatusPanel.add(fillEnd, BorderLayout.LINE_END);

		// Center (Status Content)
		JPanel statusContentPanel = new JPanel(new GridBagLayout());
		statusContentPanel.setBackground(backgroundColor);
		checkStatusPanel.add(statusContentPanel, BorderLayout.CENTER);

		Color borderColor = new Color(166, 173, 168);

		// TODO: get wh from the database

		createStatusPanel(borderColor);
		GridBagConstraints statusC = new GridBagConstraints();
		statusC.gridx = 0;
		statusC.gridy = 0;
		statusC.fill = GridBagConstraints.HORIZONTAL;
		statusC.insets = new Insets(25, 25, 0, 0);
		statusContentPanel.add(statusPanel, statusC);

		JPanel detailStatusPanel = createDetailStatusPanel(borderColor);
		GridBagConstraints detailC = new GridBagConstraints();
		detailC.gridx = 1;
		detailC.gridy = 0;
		detailC.anchor = GridBagConstraints.NORTHWEST;
		detailC.fill = GridBagConstraints.BOTH;
		detailC.insets = new Insets(25, 25, 0, 0);
		statusContentPanel.add(detailStatusPanel, detailC);

		JPanel ruleStatusPanel = createRuleStatusPanel(borderColor);
		GridBagConstraints ruleGBC = new GridBagConstraints();
		ruleGBC.gridx = 0;
		ruleGBC.gridy = 1;
		ruleGBC.gridwidth = 2;
		ruleGBC.weightx = 0;
		ruleGBC.anchor = GridBagConstraints.WEST;
		ruleGBC.fill = GridBagConstraints.HORIZONTAL;
		ruleGBC.insets = new Insets(25, 25, 0, 0);
		statusContentPanel.add(ruleStatusPanel, ruleGBC);

		JPanel fillerPanel = new JPanel();
		fillerPanel.setOpaque(false);
		GridBagConstraints filler = new GridBagConstraints();
		filler.gridx = 0;
		filler.gridy = 2;
		filler.gridwidth = 2;
		filler.weightx = 1.0;
		filler.weighty = 1.0;
		filler.fill = GridBagConstraints.BOTH;
		statusContentPanel.add(fillerPanel, filler);

		// SOUTH (buttons)
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setBackground(backgroundColor);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
		checkStatusPanel.add(southPanel, BorderLayout.SOUTH);

		Dimension buttonSize = new Dimension(220, 50);
		// Check-again button
		JButton checkAgainButton = new JButton("ตรวจสอบอีกครั้ง");
		checkAgainButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		checkAgainButton.setPreferredSize(buttonSize);
		checkAgainButton.setFocusPainted(false);
		checkAgainButton.setBackground(Color.WHITE);
		checkAgainButton.setForeground(Color.BLUE);
		checkAgainButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		checkAgainButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkAgainButton.addActionListener(e -> {
			mainFrame.changeCard(1, 1);
		});
		southPanel.add(checkAgainButton, BorderLayout.WEST);

		// Finish button
		JButton finishButton = new JButton("เสร็จสิ้น");
		finishButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		finishButton.setPreferredSize(buttonSize);
		finishButton.setFocusPainted(false);
		finishButton.setBackground(Color.BLUE);
		finishButton.setForeground(Color.WHITE);
		finishButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		finishButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		finishButton.addActionListener(e -> {
			mainFrame.changeCard(0, 0);
		});
		southPanel.add(finishButton, BorderLayout.EAST);
	}

	private JPanel createRuleStatusPanel(Color borderColor) {
		JPanel ruleStatusPanel = new JPanel();
		ruleStatusPanel.setLayout(new BoxLayout(ruleStatusPanel, BoxLayout.Y_AXIS));
		ruleStatusPanel.setBackground(backgroundColor);
		ruleStatusPanel.setBorder(Utils.createPaddingBorder(borderColor, 15));

		// Rule Title
		JLabel ruleTitleLabel = new JLabel("กฎระเบียบทั่วไปในการนำ Power Bank ขึ้นเครื่อง");
		ruleTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		ruleTitleLabel.setForeground(Color.BLACK);
		ruleTitleLabel.setAlignmentX(LEFT_ALIGNMENT);
		ruleTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
		ruleStatusPanel.add(ruleTitleLabel);

		// Rule 1
		JLabel rule1 = new JLabel("- ความจุไม่เกิน 100 Wh สามารถนำขึ้นเครื่องได้โดยไม่ต้องขออนุญาต");
		rule1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rule1.setForeground(Color.BLACK);
		rule1.setAlignmentX(LEFT_ALIGNMENT);
		rule1.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		ruleStatusPanel.add(rule1);

		// Rule 2
		JLabel rule2 = new JLabel("- จะต้องถือเป็นสัมภาระขึ้นเครื่องเท่านั้น");
		rule2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rule2.setForeground(Color.BLACK);
		rule2.setAlignmentX(LEFT_ALIGNMENT);
		rule2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		ruleStatusPanel.add(rule2);

		// Rule 3
		JLabel rule3 = new JLabel("- สามารถพกพาได้สูงสุดไม่เกิน 2 เครื่อง");
		rule3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rule3.setForeground(Color.BLACK);
		rule3.setAlignmentX(LEFT_ALIGNMENT);
		rule3.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		ruleStatusPanel.add(rule3);

		return ruleStatusPanel;
	}

	private JPanel createDetailStatusPanel(Color borderColor) {
		// TODO: get data from database
		double capacity = 20000;
		double voltage = 3.7;
		double wh = 74;
		double maximumWh = 100;

		JPanel detailPanel = new JPanel();
		detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
		detailPanel.setBackground(backgroundColor);
		detailPanel.setBorder(Utils.createPaddingBorder(borderColor, 15));

		// Detail Title
		JLabel detailTitle = new JLabel("รายละเอียด");
		detailTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		detailTitle.setForeground(new Color(36, 160, 237));
		detailTitle.setAlignmentX(LEFT_ALIGNMENT);
		detailTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		detailPanel.add(detailTitle);

		// Detail Content
		JPanel capacityPanel = createDetailContent("ความจุ", capacity, "mAh");
		detailPanel.add(capacityPanel);

		JPanel voltagePanel = createDetailContent("แรงดันไฟฟ้า", voltage, "V");
		detailPanel.add(voltagePanel);

		JPanel whPanel = createDetailContent("พลังงาน", wh, "Wh");
		detailPanel.add(whPanel);

		JPanel maximumPanel = createDetailContent("เกณฑ์พลังงานไม่เกิน", maximumWh, "Wh");
		detailPanel.add(maximumPanel);

		detailPanel.add(Box.createVerticalGlue());

		return detailPanel;
	}

	private JPanel createDetailContent(String valueName, double value, String unit) {
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(backgroundColor);
		contentPanel.setAlignmentX(LEFT_ALIGNMENT);

		JLabel valueNameLabel = new JLabel(valueName + " ");
		valueNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		valueNameLabel.setForeground(Color.BLACK);

		GridBagConstraints valueNameGBC = new GridBagConstraints();
		valueNameGBC.gridx = 0;
		valueNameGBC.gridy = 0;
		valueNameGBC.ipadx = 100;
		valueNameGBC.weightx = 1;
		valueNameGBC.anchor = GridBagConstraints.WEST;
		contentPanel.add(valueNameLabel, valueNameGBC);

		JLabel valueLabel = new JLabel(String.valueOf(value) + " " + unit);
		valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		valueLabel.setForeground(Color.BLACK);

		GridBagConstraints valueGBC = new GridBagConstraints();
		valueGBC.gridx = 1;
		valueGBC.gridy = 0;
		valueGBC.weightx = 0;
		valueGBC.anchor = GridBagConstraints.EAST;
		valueGBC.insets = new Insets(0, 0, 5, 0);
		contentPanel.add(valueLabel, valueGBC);

		return contentPanel;
	}

	private void createStatusPanel(Color borderColor) {

		statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		statusPanel.setBorder(Utils.createPaddingBorder(borderColor, 15));

		statusIconLabel = new JLabel();
		statusIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		statusIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		statusPanel.add(statusIconLabel);

		statusLabelTitle = new JLabel();
		statusLabelTitle.setFont(new Font("Tahoma", Font.BOLD, 32));
		statusLabelTitle.setAlignmentX(CENTER_ALIGNMENT);
		statusLabelTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		statusPanel.add(statusLabelTitle);

		statusLabelDetail = new JLabel();
		statusLabelDetail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		statusLabelDetail.setForeground(Color.DARK_GRAY);
		statusLabelDetail.setAlignmentX(CENTER_ALIGNMENT);
		statusLabelDetail.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		statusPanel.add(statusLabelDetail);

		whLabel = new JLabel();
		whLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		whLabel.setAlignmentX(CENTER_ALIGNMENT);
		whLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
		statusPanel.add(whLabel);

	}

	private void updateStatusPanel(double wh, boolean approve) {

		String statusTitle = "สามารถนำขึ้นเครื่องได้";
		String statusDetail = "Power Bank ของคุณเป็นไปตามกฎระเบียบ";
		String statusImagePath = "/icons/check_green_icon_128.png";
		Color backgroundColor = new Color(201, 242, 212);
		Color statusTextColor = new Color(0, 100, 0);

		if (!approve) {
			statusTitle = "ไม่สามารถนำขึ้นเครื่องได้";
			statusDetail = "Power Bank ของคุณไม่เป็นไปตามกฎระเบียบ";
			statusImagePath = "/icons/cancel_red_icon_128.png";
			backgroundColor = new Color(247, 188, 188);
			statusTextColor = new Color(176, 4, 4);
		}

		statusPanel.setBackground(backgroundColor);

		// status icon
		ImageIcon statusImageIcon = Utils.createImageIcon(statusImagePath, "Status Icon");

		statusIconLabel.setIcon(statusImageIcon);

		// status label
		statusLabelTitle.setText(statusTitle);
		statusLabelTitle.setForeground(statusTextColor);

		statusLabelDetail.setText(statusDetail);

		// wh label
		whLabel.setText("พลังงาน: " + String.valueOf(wh) + " Wh");
		whLabel.setForeground(statusTextColor);
	}

}
