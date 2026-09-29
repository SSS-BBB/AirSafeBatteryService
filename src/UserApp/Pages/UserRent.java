package UserApp.Pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import UserApp.UserMainFrame;

import Utils.Utils;

public class UserRent extends JPanel {
	
	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor;
	
	private JPanel filterPanel;
	JComboBox<String> addressDropdown;
	
	public UserRent(UserMainFrame mainFrame, Color backgroundColor, Color menuColor) {
		super();
		
		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		
		createRentPage();
	}
	
	private void createRentPage() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(backgroundColor);
		
		// Title
		JLabel titleLabel = new JLabel("เช่า Power Bank");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		this.add(titleLabel);
		
		createFilterPanel();
		this.add(filterPanel);
	}
	
	private void createFilterPanel() {
		filterPanel = new JPanel(new GridLayout(4, 3));
		filterPanel.setBackground(backgroundColor);
		
		// Address filter
		JPanel addressFilterPanel = new JPanel();
		addressFilterPanel.setBackground(backgroundColor);
		filterPanel.add(addressFilterPanel);
		
		JLabel addressLabel = new JLabel("สถานที่:");
		addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		addressFilterPanel.add(addressLabel);
		
		// TODO: get address from database
		String[] addressList = {"สนามบินสุวรรณภูมิ", "สนามบินดอนเมือง"};
		addressDropdown = Utils.createAppDropdown(addressList, backgroundColor, menuColor);
		addressFilterPanel.add(addressDropdown);
		
	}
	
	
	
}
