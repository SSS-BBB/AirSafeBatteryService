package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import UserApp.UserMainFrame;

import Utils.Utils;

public class UserRent extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor;

	private JPanel filterPanel;
	private JComboBox<String> addressDropdown, brandDropdown, inputDropdown, 
			outputDropdown, orderbyDropdown;
	private JTextField minPriceTextField, maxPriceTextField, minCapTextField, maxCapTextField, minWeightTextField,
			maxWeightTextField;

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
		JLabel titleLabel = new JLabel("เช่า Power Bank", SwingConstants.LEFT);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.setBackground(backgroundColor);
		this.add(titleLabel);

		createFilterPanel();
		JPanel filterWrapperPanel = new JPanel(new BorderLayout());
		filterWrapperPanel.setBackground(backgroundColor);
		filterWrapperPanel.setAlignmentX(LEFT_ALIGNMENT);
		filterWrapperPanel.add(filterPanel, BorderLayout.NORTH);
		this.add(filterWrapperPanel);

		// this.add(Box.createVerticalGlue());
	}

	private void createFilterPanel() {
		filterPanel = new JPanel(new GridBagLayout());
		filterPanel.setBackground(backgroundColor);
		filterPanel.setAlignmentX(LEFT_ALIGNMENT);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;

		int textFieldSize = 6;

		// ---------- Row 1 ----------
		// Address filter
		JPanel addressFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addressFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 0;
		gbc.gridy = 0;
		filterPanel.add(addressFilterPanel, gbc);

		JLabel addressLabel = new JLabel("สถานที่:");
		addressLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		addressLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		addressLabel.setAlignmentX(LEFT_ALIGNMENT);
		addressFilterPanel.add(addressLabel);

		// TODO: get address from database
		String[] addressList = { "สนามบินสุวรรณภูมิ", "สนามบินดอนเมือง" };
		addressDropdown = Utils.createAppDropdown(addressList, backgroundColor, menuColor);
		addressDropdown.setAlignmentX(LEFT_ALIGNMENT);
		addressFilterPanel.add(addressDropdown);

		// ---------- Row 2 ----------
		// Price Filter
		JPanel priceFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		priceFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 0;
		gbc.gridy = 1;
		filterPanel.add(priceFilterPanel, gbc);

		JLabel priceLabel = new JLabel("ราคา:");
		priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		priceLabel.setHorizontalAlignment(SwingConstants.LEFT);
		priceFilterPanel.add(priceLabel);

		minPriceTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		priceFilterPanel.add(minPriceTextField);

		JLabel toLabel = new JLabel("ถึง");
		toLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		toLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		toLabel.setHorizontalAlignment(SwingConstants.LEFT);
		priceFilterPanel.add(toLabel);

		maxPriceTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		priceFilterPanel.add(maxPriceTextField);

		JLabel priceUnitLabel = new JLabel("บาท/วัน");
		priceUnitLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		priceUnitLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		priceUnitLabel.setHorizontalAlignment(SwingConstants.LEFT);
		priceFilterPanel.add(priceUnitLabel);

		// Capacity Filter
		JPanel capacityFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		capacityFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 1;
		gbc.gridy = 1;
		filterPanel.add(capacityFilterPanel, gbc);

		JLabel capacityLabel = new JLabel("ความจุ:");
		capacityLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		capacityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		capacityLabel.setHorizontalAlignment(SwingConstants.LEFT);
		capacityFilterPanel.add(capacityLabel);

		minCapTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		capacityFilterPanel.add(minCapTextField);

		JLabel capToLabel = new JLabel("ถึง");
		capToLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		capToLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		capToLabel.setHorizontalAlignment(SwingConstants.LEFT);
		capacityFilterPanel.add(capToLabel);

		maxCapTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		capacityFilterPanel.add(maxCapTextField);

		JLabel capUnitLabel = new JLabel("Wh");
		capUnitLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		capUnitLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		capUnitLabel.setHorizontalAlignment(SwingConstants.LEFT);
		capacityFilterPanel.add(capUnitLabel);

		// Weight Filter
		JPanel weightFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		weightFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 2;
		gbc.gridy = 1;
		filterPanel.add(weightFilterPanel, gbc);

		JLabel weightLabel = new JLabel("น้ำหนัก:");
		weightLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		weightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		weightLabel.setHorizontalAlignment(SwingConstants.LEFT);
		weightFilterPanel.add(weightLabel);

		minWeightTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		weightFilterPanel.add(minWeightTextField);

		JLabel weightToLabel = new JLabel("ถึง");
		weightToLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		weightToLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		weightToLabel.setHorizontalAlignment(SwingConstants.LEFT);
		weightFilterPanel.add(weightToLabel);

		maxWeightTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		weightFilterPanel.add(maxWeightTextField);

		JLabel weightUnitLabel = new JLabel("kg");
		weightUnitLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		weightUnitLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		weightUnitLabel.setHorizontalAlignment(SwingConstants.LEFT);
		weightFilterPanel.add(weightUnitLabel);

		// ---------- Row 3 ----------
		// Brand Filter
		JPanel brandFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		brandFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 0;
		gbc.gridy = 2;
		filterPanel.add(brandFilterPanel, gbc);

		JLabel brandLabel = new JLabel("Brand:");
		brandLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		brandLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		brandLabel.setHorizontalAlignment(SwingConstants.LEFT);
		brandFilterPanel.add(brandLabel);

		// TODO: get for rent power bank brand from database
		String[] brandList = { "Asafjpaf", "Bsafasfg", "Csafsfa", "Dsaf", "Efsaf" };
		brandDropdown = Utils.createAppDropdown(brandList, backgroundColor, menuColor);
		brandDropdown.setAlignmentX(LEFT_ALIGNMENT);
		brandFilterPanel.add(brandDropdown);

		// Input filter
		JPanel inputFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 1;
		gbc.gridy = 2;
		filterPanel.add(inputFilterPanel, gbc);

		JLabel inputLabel = new JLabel("Input:");
		inputLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		inputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		inputLabel.setHorizontalAlignment(SwingConstants.LEFT);
		inputFilterPanel.add(inputLabel);

		// TODO: get power bank input type from database
		String[] inputList = { "Type-C", "Lighting" };
		inputDropdown = Utils.createAppDropdown(inputList, backgroundColor, menuColor);
		inputDropdown.setAlignmentX(LEFT_ALIGNMENT);
		inputFilterPanel.add(inputDropdown);

		// Output filter
		JPanel outputFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		outputFilterPanel.setBackground(backgroundColor);
		gbc.gridx = 2;
		gbc.gridy = 2;
		filterPanel.add(outputFilterPanel, gbc);

		JLabel outputLabel = new JLabel("Output:");
		outputLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		outputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		outputLabel.setHorizontalAlignment(SwingConstants.LEFT);
		outputFilterPanel.add(outputLabel);

		// TODO: get power bank output type from database
		String[] outputList = { "Type-C", "Lighting" };
		outputDropdown = Utils.createAppDropdown(outputList, backgroundColor, menuColor);
		outputDropdown.setAlignmentX(LEFT_ALIGNMENT);
		outputFilterPanel.add(outputDropdown);

		// ---------- Row 4 ----------
		// Order by filter
		JPanel orderbyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		orderbyPanel.setBackground(backgroundColor);
		gbc.gridx = 0;
		gbc.gridy = 3;
		filterPanel.add(orderbyPanel, gbc);

		JLabel orderbyLabel = new JLabel("จัดเรียงตาม:");
		orderbyLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		orderbyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		orderbyLabel.setHorizontalAlignment(SwingConstants.LEFT);
		orderbyPanel.add(orderbyLabel);

		String[] orderList = { "ราคา", "ความจุ", "น้ำหนัก" };
		orderbyDropdown = Utils.createAppDropdown(orderList, backgroundColor, menuColor);
		orderbyDropdown.setAlignmentX(LEFT_ALIGNMENT);
		orderbyPanel.add(orderbyDropdown);
		
		// Order type
		JPanel ordertypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		ordertypePanel.setBackground(backgroundColor);
		// ordertypePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		filterPanel.add(ordertypePanel, gbc);
		
		JButton ascButton = Utils.createBorderButton("น้อยไปมาก", Color.BLACK, Color.BLACK, 16);
		ordertypePanel.add(ascButton);
		
		JButton descButton = Utils.createBorderButton("มากไปน้อย", Color.BLACK, Color.BLACK, 16);
		ordertypePanel.add(descButton);
		
		ascButton.addActionListener(e -> {
			ascButton.setBackground(menuColor);
			descButton.setBackground(null);
			
			ascButton.setForeground(Color.WHITE);
			descButton.setForeground(Color.BLACK);
			
			// TODO: order power banks
		});
		
		descButton.addActionListener(e -> {
			descButton.setBackground(menuColor);
			ascButton.setBackground(null);
			
			descButton.setForeground(Color.WHITE);
			ascButton.setForeground(Color.BLACK);
			
			// TODO: order power banks
		});
		
		// Clear Filter
		JButton clearFilterButton = Utils.createNoBackgroundButton("ล้าง filter", new Color(36, 160, 237));
		clearFilterButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gbc.gridx = 2;
		gbc.gridy = 3;
		filterPanel.add(clearFilterButton, gbc);
		
		clearFilterButton.addActionListener(e -> {
			// clear input
			minPriceTextField.setText("");
			maxPriceTextField.setText("");
			minCapTextField.setText("");
			maxCapTextField.setText("");
			minWeightTextField.setText("");
			maxWeightTextField.setText("");
			
			ascButton.setBackground(null);
			descButton.setBackground(null);
			
			ascButton.setForeground(Color.BLACK);
			descButton.setForeground(Color.BLACK);
		});

	}

}
