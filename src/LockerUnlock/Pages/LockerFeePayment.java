package LockerUnlock.Pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import CustomGUI.NameValueHolder;
import LockerUnlock.LockerUnlockMainFrame;
import Utils.Utils;

public class LockerFeePayment extends JPanel {
	
	private LockerUnlockMainFrame mainFrame;
	
	// Components
	private JLabel feeLabel;
	private JButton paymentConfirmButton;
	
	public LockerFeePayment(LockerUnlockMainFrame mainFrame) {
		super(new GridBagLayout());
		this.mainFrame = mainFrame;

		setBackground(LockerUnlockMainFrame.BACKGROUND_COLOR);
		
		createFeePaymentPage();
	}
	
	public void updateFee(double fee) {
		if (feeLabel == null) {
			System.err.println("Null Fee Label. Unable to update fee.");
			return;
		}
		
		feeLabel.setText(String.valueOf(fee) + " บาท");
	}
	
	private void createFeePaymentPage() {
		int margin = 15;
		GridBagConstraints componentGBC = getDefaultGridBagConstraints(margin, margin);
		GridBagConstraints glueGBC = getVerticleGlueGridBagConstraints(margin, margin);
		int row = 0;
		
		int headingFontSize = 28;
		int bodyFontSize = 24;
		
		// Title
		JLabel titleLabel = createPageLabel("รับคืน Power Bank สาย", Font.BOLD, 32);
		titleLabel.setForeground(LockerUnlockMainFrame.FAILED_COLOR);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		componentGBC.gridy = row++;
		this.add(titleLabel, componentGBC);
		glueGBC.gridy = row++;
		this.add(Box.createVerticalGlue(), glueGBC);
		
		// Please pay fee
		JLabel pleasePayFeeLabel = createPageLabel("กรุณาชำระค่าปรับ", Font.PLAIN, headingFontSize);
		componentGBC.gridy = row++;
		this.add(pleasePayFeeLabel, componentGBC);
		
		// Scan to pay
		JLabel scanToPayLabel = createPageLabel("สแกนเพื่อชำระเงิน", Font.PLAIN, bodyFontSize);
		componentGBC.gridy = row++;
		this.add(scanToPayLabel, componentGBC);
		
		// Qr Code
		ImageIcon qrIcon = Utils.createImageIcon("/qr_code.png", "Qr Code Icon");
		JLabel qrIconLabel = new JLabel(qrIcon);
		qrIconLabel.setBackground(Color.WHITE);
		qrIconLabel.setOpaque(true);
		componentGBC.gridy = row++;
		this.add(qrIconLabel, componentGBC);
		
		// Total Fee
		JLabel totalFeeLabel = createPageLabel("ยอดชำระทั้งหมด", Font.PLAIN, bodyFontSize);
		componentGBC.gridy = row++;
		this.add(totalFeeLabel, componentGBC);
		
		// Fee Label
		feeLabel = createPageLabel("", Font.PLAIN, bodyFontSize);
		feeLabel.setBorder(Utils.createPaddingBorder(LockerUnlockMainFrame.PRIMARY_FONT_COLOR, 8, 2));
		feeLabel.setPreferredSize(new Dimension(300, 30));
		componentGBC.gridy = row++;
		this.add(feeLabel, componentGBC);
		
		// Pay within time limit
		JLabel payWithinTimeLabel = createPageLabel("กรุณาชำระภายใน 15 นาที", Font.PLAIN, bodyFontSize);
		payWithinTimeLabel.setForeground(LockerUnlockMainFrame.SUCCESSFUL_COLOR);
		componentGBC.gridy = row++;
		this.add(payWithinTimeLabel, componentGBC);
		
		// Payment confirm
		Dimension buttonSize = new Dimension(225, 50);
		paymentConfirmButton = Utils.createColorBackgroundButton("ยืนยันการชำระเงิน", 
				LockerUnlockMainFrame.PRIMARY_COLOR, LockerUnlockMainFrame.PRIMARY_FONT_COLOR, 
				bodyFontSize);
		paymentConfirmButton.setPreferredSize(buttonSize);
		paymentConfirmButton.setMaximumSize(buttonSize);
		
		componentGBC.gridy = row++;
		this.add(paymentConfirmButton, componentGBC);
		
		glueGBC.gridy = row++;
		glueGBC.weighty = 3.0;
		this.add(Box.createVerticalGlue(), glueGBC);
		
	}
	
	public void addPaymentConfirmActionListener(ActionListener action) {
		if (paymentConfirmButton != null) {
			paymentConfirmButton.addActionListener(action);			
		}
		else {
			System.err.println("Null Payment Confirm Button. Unable to add action listener.");
		}
	}
	
	private GridBagConstraints getVerticleGlueGridBagConstraints(int verticleMargin, int horizontalMargin) {
		GridBagConstraints gbc = getDefaultGridBagConstraints(verticleMargin, horizontalMargin);
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		
		return gbc;
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
