package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import UserApp.UserMainFrame;
import Utils.Utils;

// show success / failed status from rent/deposit
public class UserProcessStatus extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, mainColor, menuColor;
	
	private JLabel titleLabel, statusIconLabel, detailBoldLabel1, detailBoldLabel2, detailBoldLabel3, detailPlainLabel1;
	
	public UserProcessStatus(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.mainColor = mainColor;
		this.menuColor = menuColor;

		setBackground(backgroundColor);

		// Title
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titlePanel.setBackground(backgroundColor);
		this.add(titlePanel, BorderLayout.NORTH);

		titleLabel = new JLabel("เช่าสำเร็จ");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 32));
		titleLabel.setBackground(backgroundColor);
		titleLabel.setForeground(new Color(82, 190, 79));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		titlePanel.add(titleLabel);
		
		JPanel statusDetailPanel = createStatusDetailPanel();
		this.add(statusDetailPanel, BorderLayout.CENTER);
		
		updateProcessStatusPage(true, 
				"เช่าสำเร็จ", "สนามบินดอนเมือง", "Locker หมายเลข 1", "รหัสสำหรับปลดล็อก XYZ123",
				"โปรดบันทึกรหัสนี้ไว้สำหรับการปลดล็อกตู้");
	}
	
	public void updateProcessStatusPage(boolean successful, String titleText, 
			String detailBoldText1, String detailBoldText2, String detailBoldText3, 
			String detailPlainText1) {
		
		titleLabel.setText(titleText);
		Color statusColor = (successful) ? new Color(82, 190, 79) : new Color(214, 49, 36);
		titleLabel.setForeground(statusColor);
		
		String imagePath = (successful) ? "/icons/check_green_icon_128.png" : "/icons/cancel_red_icon_128.png";
		ImageIcon statusImageIcon = Utils.createImageIcon(imagePath, "Status Image");
		statusIconLabel.setIcon(statusImageIcon);
		
		detailBoldLabel1.setText(detailBoldText1);
		detailBoldLabel2.setText(detailBoldText2);
		detailBoldLabel3.setText(detailBoldText3);
		detailPlainLabel1.setText(detailPlainText1);
	}
	
	private JPanel createStatusDetailPanel() {
		JPanel statusDetailPanel = new JPanel();
		statusDetailPanel.setLayout(new BoxLayout(statusDetailPanel, BoxLayout.Y_AXIS));
		statusDetailPanel.setBackground(null);
		
		statusIconLabel = new JLabel();
		statusIconLabel.setAlignmentX(CENTER_ALIGNMENT);
		statusIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		statusDetailPanel.add(statusIconLabel);
		
		int fontSize = 28;
		detailBoldLabel1 = createPageLabel(Font.BOLD, fontSize);
		statusDetailPanel.add(detailBoldLabel1);
		detailBoldLabel2 = createPageLabel(Font.BOLD, fontSize);
		statusDetailPanel.add(detailBoldLabel2);
		detailBoldLabel3 = createPageLabel(Font.BOLD, fontSize);
		statusDetailPanel.add(detailBoldLabel3);
		detailPlainLabel1 = createPageLabel(Font.PLAIN, fontSize - 4);
		statusDetailPanel.add(detailPlainLabel1);
		
		Dimension buttonSize = new Dimension(150, 60);
		JButton backButton = Utils.createBorderButton("กลับ", menuColor, mainColor, fontSize, 2);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		backButton.setAlignmentX(CENTER_ALIGNMENT);
		statusDetailPanel.add(backButton);
		
		backButton.addActionListener(e -> {
			// TODO: change to list page
			mainFrame.changeCard(0, 0);
		});
		
		return statusDetailPanel;
	}
	
	private JLabel createPageLabel(int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel();
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(CENTER_ALIGNMENT);
		pageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		
		return pageLabel;
	}

}