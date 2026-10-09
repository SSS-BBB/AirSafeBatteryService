package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import CustomGUI.CustomDropDown;
import CustomGUI.CustomScrollPane;
import Database.DatabaseConnector;
import Struct.ForRentPowerBank;
import UserApp.UserMainFrame;

import Utils.Utils;

public class UserRent extends JPanel {
	private UserMainFrame mainFrame;
	private Color backgroundColor, menuColor, mainColor;

	private ArrayList<String> addressFilterValues, brandFilterValues, inputFilterValues, outputFilterValues;
	private Map<String, String> orderByFilterAttributes;

	private JPanel filterPanel, powerbankPanel;
	private JComboBox<String> addressDropdown, brandDropdown, inputDropdown, outputDropdown, orderbyDropdown;
	private JTextField minPriceTextField, maxPriceTextField, minCapTextField, maxCapTextField, minWeightTextField,
			maxWeightTextField;

	private ArrayList<ForRentPowerBank> forRentPowerBankList;

	private UserForRentDetail detailPage;

	private JButton ascButton, descButton;
	private boolean ascending;

	public UserRent(UserMainFrame mainFrame, Color backgroundColor, Color mainColor, Color menuColor,
			UserForRentDetail detailPage) {
		super();

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
		this.menuColor = menuColor;
		this.mainColor = mainColor;
		this.detailPage = detailPage;

		forRentPowerBankList = DatabaseConnector.getForRentPowerBank();

		if (forRentPowerBankList == null) {
			forRentPowerBankList = new ArrayList<ForRentPowerBank>();
		}

		// get possible filter values
		addressFilterValues = new ArrayList<String>();
		brandFilterValues = new ArrayList<String>();
		inputFilterValues = new ArrayList<String>();
		outputFilterValues = new ArrayList<String>();
		
		// none value for not filter
		addressFilterValues.add("ทั้งหมด");
		brandFilterValues.add("ทั้งหมด");
		inputFilterValues.add("ทั้งหมด");
		outputFilterValues.add("ทั้งหมด");
		
		for (ForRentPowerBank powerBank : forRentPowerBankList) {
			if (!addressFilterValues.contains(powerBank.address)) 
				addressFilterValues.add(powerBank.address);
			
			if (!brandFilterValues.contains(powerBank.deviceInfo.brand)) 
				brandFilterValues.add(powerBank.deviceInfo.brand);
			
			for (String input : powerBank.deviceInfo.input)
				if (!inputFilterValues.contains(input)) 
					inputFilterValues.add(input);
			
			for (String outut : powerBank.deviceInfo.output)
				if (!outputFilterValues.contains(outut)) 
					outputFilterValues.add(outut);
		}
		
		// display name name -> attribute name
		orderByFilterAttributes = new LinkedHashMap<String, String>();
		orderByFilterAttributes.put("ไม่มี", "");
		orderByFilterAttributes.put("Brand", "brand");
		orderByFilterAttributes.put("สถานที่", "address");
		orderByFilterAttributes.put("ค่าเช่าต่อวัน", "price_per_day");
		orderByFilterAttributes.put("ค่าปรับส่งคืนช้า", "late_fee_per_day");
		orderByFilterAttributes.put("จำนวนวันให้เช่าสูงสุด", "max_duration");
		orderByFilterAttributes.put("ความจุ", "capacity");
		orderByFilterAttributes.put("Wh", "wh");
		orderByFilterAttributes.put("น้ำหนัก", "weight");
		
		
		createRentPage();
	}

	private void createRentPage() {
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(backgroundColor);
		this.add(topPanel, BorderLayout.NORTH);

		// Title
		JLabel titleLabel = new JLabel("เช่า Power Bank", SwingConstants.LEFT);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setBackground(backgroundColor);
		topPanel.add(titleLabel);

		createFilterPanel();
		JPanel filterWrapperPanel = new JPanel(new BorderLayout());
		filterWrapperPanel.setBackground(backgroundColor);
		filterWrapperPanel.setAlignmentX(LEFT_ALIGNMENT);
		filterWrapperPanel.add(filterPanel, BorderLayout.NORTH);
		topPanel.add(filterWrapperPanel);

		createPowerbankPanel();
		CustomScrollPane powerbankScrollPane = new CustomScrollPane(powerbankPanel, menuColor, backgroundColor);
		this.add(powerbankScrollPane, BorderLayout.CENTER);
	}

	private JPanel createPowerbankDisplayPanel(ForRentPowerBank powerBank) {
		if (powerBank == null) {
			System.err.println("Null powerbank. Unable to create this powerbank display");
			return null;
		}

		JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		wrapperPanel.setBackground(backgroundColor);

		int margin = 5;
		int fontSize = 14;
		float componentAlignment = CENTER_ALIGNMENT;

		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		displayPanel.setBorder(Utils.createPaddingBorder(Color.BLACK, 8));
		displayPanel.setBackground(backgroundColor);
		displayPanel.setMaximumSize(displayPanel.getPreferredSize());
		wrapperPanel.add(displayPanel);

		ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(powerBank.deviceInfo.image, 120, 120);
		JLabel powerBankImageLabel = new JLabel(scaledPowerBankImage);
		powerBankImageLabel.setAlignmentX(componentAlignment);
		displayPanel.add(powerBankImageLabel);

		JLabel brandLabel = new JLabel(powerBank.deviceInfo.brand);
		brandLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + 2));
		brandLabel.setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));
		brandLabel.setAlignmentX(componentAlignment);
		displayPanel.add(brandLabel);

		JLabel nameLabel = new JLabel(powerBank.deviceInfo.name);
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + 2));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		nameLabel.setAlignmentX(componentAlignment);
		displayPanel.add(nameLabel);

		JLabel modelLabel = new JLabel(powerBank.deviceInfo.model);
		modelLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + 2));
		modelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		modelLabel.setAlignmentX(componentAlignment);
		displayPanel.add(modelLabel);

		String whAndCapacity = String.valueOf(powerBank.deviceInfo.wh) + " Wh / "
				+ String.valueOf(powerBank.deviceInfo.capacity) + " mAh";
		JLabel whAndCapacityLabel = new JLabel(whAndCapacity);
		whAndCapacityLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		whAndCapacityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		whAndCapacityLabel.setAlignmentX(componentAlignment);
		whAndCapacityLabel.setForeground(Color.GRAY);
		displayPanel.add(whAndCapacityLabel);

		String sizeAndWeight = String.valueOf(powerBank.deviceInfo.width) + "x"
				+ String.valueOf(powerBank.deviceInfo.length) + "x" + String.valueOf(powerBank.deviceInfo.height)
				+ " cm" + " " + String.valueOf(powerBank.deviceInfo.weight) + " kg";
		JLabel sizeAndWeightLabel = new JLabel(sizeAndWeight);
		sizeAndWeightLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		sizeAndWeightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		sizeAndWeightLabel.setAlignmentX(componentAlignment);
		sizeAndWeightLabel.setForeground(Color.GRAY);
		displayPanel.add(sizeAndWeightLabel);

		String locker = "Locker หมายเลข " + String.valueOf(powerBank.lockerNumber);
		JLabel lockerLabel = new JLabel(locker);
		lockerLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		lockerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		lockerLabel.setAlignmentX(componentAlignment);
		displayPanel.add(lockerLabel);

		String rentPrice = String.valueOf(powerBank.pricePerDay) + " บาท/วัน";
		JLabel rentPriceLabel = new JLabel(rentPrice);
		rentPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		rentPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		rentPriceLabel.setAlignmentX(componentAlignment);
		displayPanel.add(rentPriceLabel);

		String maxRentDay = "เช่าได้สูงสุด " + String.valueOf(powerBank.maxDuration) + " วัน";
		JLabel maxRentDayLabel = new JLabel(maxRentDay);
		maxRentDayLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		maxRentDayLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		maxRentDayLabel.setAlignmentX(componentAlignment);
		displayPanel.add(maxRentDayLabel);

		String lateFee = "ค่าปรับส่งคืนสาย " + String.valueOf(powerBank.lateFeePerDay) + " บาท/วัน";
		JLabel lateFeeLabel = new JLabel(lateFee);
		lateFeeLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		lateFeeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		lateFeeLabel.setAlignmentX(componentAlignment);
		lateFeeLabel.setForeground(new Color(255, 95, 21)); // Warning Color
		displayPanel.add(lateFeeLabel);

		JButton rentButton = new JButton("เช่า");
		rentButton.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		rentButton.setBackground(Color.GREEN);
		rentButton.setForeground(Color.WHITE);
		rentButton.setFocusPainted(false);
		rentButton.setBorder(null);
		rentButton.setMaximumSize(new Dimension(100, 25));
		rentButton.setPreferredSize(new Dimension(100, 25));
		rentButton.setAlignmentX(componentAlignment);
		rentButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		rentButton.addActionListener(e -> {
			// set data
			detailPage.powerBank = powerBank;

			// update page
			detailPage.updateForRentDetailPage();

			// change page
			mainFrame.changeCard(2, 8);
		});

		displayPanel.add(rentButton);

		return wrapperPanel;
	}
	
	private void updatePowerBankPanel() {
		if (powerbankPanel == null) {
			System.err.println("Null Power Bank Panel. Unable to update.");
			return;
		}
		
		for (int i = 0; i < forRentPowerBankList.size(); i++)
			powerbankPanel.add(createPowerbankDisplayPanel(forRentPowerBankList.get(i)));
		
		powerbankPanel.revalidate();
		powerbankPanel.repaint();
	}
	
	private void createPowerbankPanel() {
		powerbankPanel = new JPanel(new GridLayout(0, 4, 5, 5));
		powerbankPanel.setBackground(backgroundColor);
		powerbankPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		powerbankPanel.setAlignmentX(LEFT_ALIGNMENT);

		updatePowerBankPanel();
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

		String[] addressList = Utils.listToArray(addressFilterValues);
		addressDropdown = Utils.createAppDropdown(addressList, backgroundColor, mainColor);
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
		capToLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		capToLabel.setHorizontalAlignment(SwingConstants.LEFT);
		capacityFilterPanel.add(capToLabel);

		maxCapTextField = Utils.createAppTextField(backgroundColor, textFieldSize);
		capacityFilterPanel.add(maxCapTextField);

		JLabel capUnitLabel = new JLabel("mAh");
		capUnitLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		capUnitLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
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

		String[] brandList = Utils.listToArray(brandFilterValues);
		brandDropdown = Utils.createAppDropdown(brandList, backgroundColor, mainColor);
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

		String[] inputList = Utils.listToArray(inputFilterValues);
		inputDropdown = Utils.createAppDropdown(inputList, backgroundColor, mainColor);
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

		String[] outputList = Utils.listToArray(outputFilterValues);
		outputDropdown = Utils.createAppDropdown(outputList, backgroundColor, mainColor);
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
		
		// get display name
		String[] orderList = new String[orderByFilterAttributes.size()];
		int i = 0;
		for (String displayName : orderByFilterAttributes.keySet()) {
			orderList[i++] = displayName;
		}
		
		// orderbyDropdown = Utils.createAppDropdown(orderList, backgroundColor, mainColor);
		orderbyDropdown = new CustomDropDown<String>(orderList, backgroundColor, mainColor);
		orderbyDropdown.setAlignmentX(LEFT_ALIGNMENT);
		orderbyPanel.add(orderbyDropdown);

		// Order type
		JPanel ordertypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		ordertypePanel.setBackground(backgroundColor);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		filterPanel.add(ordertypePanel, gbc);

		ascButton = Utils.createBorderButton("น้อยไปมาก", Color.BLACK, Color.BLACK, 16);
		ordertypePanel.add(ascButton);

		descButton = Utils.createBorderButton("มากไปน้อย", Color.BLACK, Color.BLACK, 16);
		ordertypePanel.add(descButton);

		ascButton.addActionListener(e -> {
			ascButton.setBackground(mainColor);
			descButton.setBackground(null);

			ascButton.setForeground(Color.WHITE);
			descButton.setForeground(Color.BLACK);

			ascending = true;
		});

		descButton.addActionListener(e -> {
			descButton.setBackground(mainColor);
			ascButton.setBackground(null);

			descButton.setForeground(Color.WHITE);
			ascButton.setForeground(Color.BLACK);

			ascending = false;
		});

		// Apply Filter
		JButton applyFilterButton = Utils.createNoBackgroundButton("ยืนยัน", mainColor);
		applyFilterButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gbc.gridx = 2;
		gbc.gridy = 3;
		filterPanel.add(applyFilterButton, gbc);

		applyFilterButton.addActionListener(e -> {
			applyFilter();
		});

		// Clear Filter
		JButton clearFilterButton = Utils.createNoBackgroundButton("ล้าง filter", new Color(36, 160, 237));
		clearFilterButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		gbc.gridx = 3;
		gbc.gridy = 3;
		filterPanel.add(clearFilterButton, gbc);

		clearFilterButton.addActionListener(e -> {
			clearFilter();
			applyFilter();
		});

	}

	public void applyFilter() {
		// TextField
		// minPriceTextField, maxPriceTextField, minCapTextField, maxCapTextField, minWeightTextField,
		// maxWeightTextField;
		
		// Price
		double minPrice = Utils.getNumberFromTextField(minPriceTextField, -1);
		
		double maxPrice = Utils.getNumberFromTextField(maxPriceTextField, -1);
		
		// Capacity
		double minCap = Utils.getNumberFromTextField(minCapTextField, -1);
		
		double maxCap = Utils.getNumberFromTextField(maxCapTextField, -1);
		
		// Weight
		double minWeight = Utils.getNumberFromTextField(minWeightTextField, -1);
		
		double maxWeight = Utils.getNumberFromTextField(maxWeightTextField, -1);
		
		// ComboBox
		// addressDropdown, brandDropdown, inputDropdown, outputDropdown, orderbyDropdown
		String addressFilter = addressDropdown.getSelectedItem().toString();
		addressFilter = (addressFilter != "ทั้งหมด") ? addressFilter : "";
		
		String brandFilter = brandDropdown.getSelectedItem().toString();
		brandFilter = (brandFilter != "ทั้งหมด") ? brandFilter : "";
		
		// input and output
		String inputFilter = inputDropdown.getSelectedItem().toString();
		inputFilter = (inputFilter != "ทั้งหมด") ? inputFilter : "";
		
		String outputFilter = outputDropdown.getSelectedItem().toString();
		outputFilter = (outputFilter != "ทั้งหมด") ? outputFilter : "";
		
		// order by
		String orderByAttribute = orderByFilterAttributes.get(orderbyDropdown.getSelectedItem().toString());
		
		forRentPowerBankList.clear();
		forRentPowerBankList = DatabaseConnector.getForRentPowerBank(
				addressFilter, brandFilter,
				inputFilter, outputFilter,
				minPrice, maxPrice, minCap, maxCap, minWeight, maxWeight,
				orderByAttribute, ascending);
		powerbankPanel.removeAll();
		updatePowerBankPanel();
	}

	public void clearFilter() {
		// clear text field
		minPriceTextField.setText("");
		maxPriceTextField.setText("");
		minCapTextField.setText("");
		maxCapTextField.setText("");
		minWeightTextField.setText("");
		maxWeightTextField.setText("");
		
		// reset drop down
		// ComboBox
		// addressDropdown, brandDropdown, inputDropdown, outputDropdown, orderbyDropdown
		addressDropdown.setSelectedIndex(0);
		brandDropdown.setSelectedIndex(0);
		inputDropdown.setSelectedIndex(0);
		outputDropdown.setSelectedIndex(0);
		orderbyDropdown.setSelectedIndex(0);

		ascButton.setBackground(null);
		descButton.setBackground(null);

		ascButton.setForeground(Color.BLACK);
		descButton.setForeground(Color.BLACK);
	}

}
