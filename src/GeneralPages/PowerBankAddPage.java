package GeneralPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import CustomGUI.CustomDropDown;
import CustomGUI.CustomList;
import CustomGUI.CustomScrollPane;
import Struct.PowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class PowerBankAddPage extends JPanel {
	
	private UserMainFrame userMainFrame;
	private Color backgroundColor, mainColor, menuColor;
	
	private BufferedImage powerBankImage;
	private JLabel powerBankImageLabel;
	private JTextField brandTextField, nameTextField, modelTextField,
			capacityTextField, whTextField, weightTextField,
			widthTextField, lengthTextField, heightTextField;
	
	private ArrayList<String> addedInputList, addedOutputList;
	
	private CustomDropDown[] chargerSelectDropDown;
	private CustomList[] chargerList;
	
	public PowerBankAddPage(UserMainFrame userMainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new GridBagLayout());
		
		this.userMainFrame = userMainFrame;
		this.backgroundColor = backgroundColor;
		this.mainColor = mainColor;
		this.menuColor = menuColor;
		
		setBackground(backgroundColor);
		
		// 0 -> input, 1 -> output
		String[] chargerArray = new String[] { "USB-C", "USB-A", "Micro USB", "Lightning" };
		
		chargerSelectDropDown = new CustomDropDown[] {
			new CustomDropDown<String>(chargerArray, backgroundColor, mainColor),
			new CustomDropDown<String>(chargerArray, backgroundColor, mainColor),
		};
		
		chargerList = new CustomList[] {
				new CustomList<String>(backgroundColor),
				new CustomList<String>(backgroundColor)
		};
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(0, 0, 20, 0);
		gbc.fill = GridBagConstraints.BOTH;
		
		JPanel powerBankImageSelectPanel = createPowerBankImageSelectPanel();
		gbc.gridy = 0;
		gbc.weighty = 0.0;
		this.add(powerBankImageSelectPanel, gbc);
		
		JPanel detailInputPanel = createDetailInputPanel();
		gbc.gridy = 1;
		gbc.weighty = 2.0;
		this.add(detailInputPanel, gbc);
		
		JPanel inAndOutPanel = createInputAndOutputPanel();
		gbc.gridy = 2;
		gbc.weighty = 3.0;
		this.add(inAndOutPanel, gbc);
		
		JPanel buttonPanel = createButtonPanel();
		gbc.gridy = 3;
		gbc.weighty = 0.0;
		this.add(buttonPanel, gbc);
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setBackground(null);
		
		int fontSize = 16;
		
		Dimension buttonSize = new Dimension(150, 50);
		
		// Back Button
		JButton backButton = Utils.createBorderButton("กลับ", menuColor, mainColor, fontSize, 2);
		backButton.setPreferredSize(buttonSize);
		backButton.setMaximumSize(buttonSize);
		buttonPanel.add(backButton);
		
		backButton.addActionListener(e -> {
			if (userMainFrame != null) userMainFrame.changeCard(3, 11);
		});
		
		// Next Button
		JButton nextButton = Utils.createColorBackgroundButton("ต่อไป", menuColor, Color.WHITE, fontSize);
		nextButton.setPreferredSize(buttonSize);
		nextButton.setMaximumSize(buttonSize);
		buttonPanel.add(nextButton);
		
		nextButton.addActionListener(e -> {
			PowerBank powerBank = getPowerBankDataFromUserInput();
			System.out.println(powerBank);
			
			// TODO: check for duplicate of (brand, model) on the power bank table
		});
		
		return buttonPanel;
	}
	
	private PowerBank getPowerBankDataFromUserInput() {
		PowerBank powerBank = new PowerBank();
		
		// gather all data from the input
		String brand = brandTextField.getText();
		if (brand.isEmpty()) return null;
		
		String name = nameTextField.getText();
		if (name.isEmpty()) return null;
		
		String model = modelTextField.getText();
		if (model.isEmpty()) return null;
		
		double capacity = Utils.getNumberFromTextField(capacityTextField, -1);
		if (capacity < 0) return null;
		
		double wh = Utils.getNumberFromTextField(whTextField, -1);
		if (wh < 0) return null;
		
		double weight = Utils.getNumberFromTextField(weightTextField, -1);
		if (weight < 0) return null;
		
		double width = Utils.getNumberFromTextField(widthTextField, -1);
		if (width < 0) return null;
		
		double length = Utils.getNumberFromTextField(lengthTextField, -1);
		if (length < 0) return null;
		
		double height = Utils.getNumberFromTextField(heightTextField, -1);
		if (height < 0) return null;
		
		// Image
		if (powerBankImage == null) return null;
		
		// Charger Type
		if (chargerList[0].getItemList().isEmpty()) return null;
		if (chargerList[1].getItemList().isEmpty()) return null;
		
		// put all data into a power bank object
		powerBank.brand = brand;
		powerBank.name = name;
		powerBank.model = model;
		powerBank.capacity = capacity;
		powerBank.wh = wh;
		powerBank.weight = weight;
		powerBank.width = width;
		powerBank.length = length;
		powerBank.height = height;
		powerBank.image = powerBankImage;
		powerBank.input = chargerList[0].getItemList();
		powerBank.output = chargerList[1].getItemList();
		
		return powerBank;
	}
	
	private JPanel createDetailInputPanel() {
		JPanel detailInputPanel = new JPanel(new GridLayout(0, 3, 10, 10));
		detailInputPanel.setBackground(backgroundColor);
		
		// CustomScrollPane detailScrollPane = new CustomScrollPane(detailInputPanel, menuColor, backgroundColor);
		
		int fontSize = 16;
		int textFieldSize = 8;
		
		// Brand
		JPanel brandPanel = createTextWithComponentToAdd("Brand:", fontSize);
		detailInputPanel.add(brandPanel);
		brandTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		brandPanel.add(brandTextField);
		
		// Name
		JPanel namePanel = createTextWithComponentToAdd("Name:", fontSize);
		detailInputPanel.add(namePanel);
		nameTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		namePanel.add(nameTextField);
		
		// Model
		JPanel modelPanel = createTextWithComponentToAdd("Model:", fontSize);
		detailInputPanel.add(modelPanel);
		modelTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		modelPanel.add(modelTextField);
		
		// Capacity
		JPanel capacityPanel = createTextWithComponentToAdd("Capacity:", fontSize);
		detailInputPanel.add(capacityPanel);
		capacityTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		capacityPanel.add(capacityTextField);
		JLabel capacityUnitLabel = createPageLabel("mAh", Font.PLAIN, fontSize);
		capacityPanel.add(capacityUnitLabel);
		
		// Wh
		JPanel whPanel = createTextWithComponentToAdd("Wh:", fontSize);
		detailInputPanel.add(whPanel);
		whTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		whPanel.add(whTextField);
		JLabel whUnitLabel = createPageLabel("Wh", Font.PLAIN, fontSize);
		whPanel.add(whUnitLabel);
		
		// Weight
		JPanel weightPanel = createTextWithComponentToAdd("น้ำหนัก:", fontSize);
		detailInputPanel.add(weightPanel);
		weightTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		weightPanel.add(weightTextField);
		JLabel weightUnitLabel = createPageLabel("kg", Font.PLAIN, fontSize);
		weightPanel.add(weightUnitLabel);
		
		// Width
		JPanel widthPanel = createTextWithComponentToAdd("กว้าง:", fontSize);
		detailInputPanel.add(widthPanel);
		widthTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		widthPanel.add(widthTextField);
		JLabel widthUnitLabel = createPageLabel("cm", Font.PLAIN, fontSize);
		widthPanel.add(widthUnitLabel);
		
		// Length
		JPanel lengthPanel = createTextWithComponentToAdd("ยาว:", fontSize);
		detailInputPanel.add(lengthPanel);
		lengthTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		lengthPanel.add(lengthTextField);
		JLabel lengthUnitLabel = createPageLabel("cm", Font.PLAIN, fontSize);
		lengthPanel.add(lengthUnitLabel);
				
		// Height
		JPanel heightPanel = createTextWithComponentToAdd("สูง:", fontSize);
		detailInputPanel.add(heightPanel);
		heightTextField = Utils.createAppTextField(backgroundColor, textFieldSize, fontSize);
		heightPanel.add(heightTextField);
		JLabel heightUnitLabel = createPageLabel("cm", Font.PLAIN, fontSize);
		heightPanel.add(heightUnitLabel);
		
		
		
		return detailInputPanel;
	}
	
	private JPanel createInputAndOutputPanel() {
		JPanel inAndOutPanel = new JPanel(new GridBagLayout());
		inAndOutPanel.setBackground(null);
		
		int fontSize = 16;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.NONE;
		
		// Input
		JPanel inputSelectPanel = createChargerSelectPanel(true, fontSize);
		gbc.gridx = 0;
		inAndOutPanel.add(inputSelectPanel, gbc);
		
		// Output
		JPanel outpuSelectPanel = createChargerSelectPanel(false, fontSize);
		gbc.gridx = 1;
		inAndOutPanel.add(outpuSelectPanel, gbc);
		
		return inAndOutPanel;
	}
	
	private JPanel createChargerSelectPanel(boolean isInput, int fontSize) {
		if (chargerSelectDropDown == null || chargerSelectDropDown.length < 2) {
			System.err.println("One/Both of charger select drop down is not defined. Unable to create charger select panel.");
			return new JPanel();
		}
		
		if (chargerList == null || chargerList.length < 2) {
			System.err.println("One/Both of charger list is not defined. Unable to create charger select panel.");
			return new JPanel();
		}
		
		// 0 -> input, 1 -> output
		
		JPanel chargerPanel = new JPanel();
		chargerPanel.setBackground(null);
		chargerPanel.setLayout(new BoxLayout(chargerPanel, BoxLayout.Y_AXIS));
		
		String inOrOut = (isInput) ? "Input" : "Output";
		int chargerIndex = (isInput) ? 0 : 1;
		JPanel chargerSelectPanel = createTextWithComponentToAdd(inOrOut + ":", fontSize);
		chargerPanel.add(chargerSelectPanel);
		
		// Drop down
		chargerSelectPanel.add(chargerSelectDropDown[chargerIndex]);
		
		// Add Button
		ImageIcon addIcon = Utils.createImageIcon("/icons/add_box_icon_24.png", "Add Box Icon");
		JButton addButton = Utils.createIconButton(addIcon);
		chargerSelectPanel.add(addButton);
		
		chargerPanel.add(Box.createVerticalStrut(5));
		
		// Charger List
		CustomScrollPane chargerListScrollPane = new CustomScrollPane(chargerList[chargerIndex], menuColor, backgroundColor);
		chargerListScrollPane.setPreferredSize(new Dimension(200, 150));
		chargerPanel.add(chargerListScrollPane);
		
		addButton.addActionListener(e -> {
			chargerList[chargerIndex].addItem(chargerSelectDropDown[chargerIndex].getSelectedItem().toString(), fontSize);
		});
		
		return chargerPanel;
	}
	
	private JPanel createTextWithComponentToAdd(String text, int fontSize) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		panel.setBackground(null);
		
		JLabel textLabel = createPageLabel(text, Font.PLAIN, fontSize);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		panel.add(textLabel);
		
		return panel;
	}
	
	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		return pageLabel;
	}
	
	private JPanel createPowerBankImageSelectPanel() {
		int margin = 5;
		
		JPanel imageSelectPanel = new JPanel();
		imageSelectPanel.setLayout(new BoxLayout(imageSelectPanel, BoxLayout.Y_AXIS));
		imageSelectPanel.setBackground(null);
		imageSelectPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
		
		// Power Bank Image Uploaded
		ImageIcon powerBankFrameIcon = Utils.createImageIcon("/icons/picture_frame_icon.png", "Power Bank Frame");
		powerBankImageLabel = new JLabel(powerBankFrameIcon);
		powerBankImageLabel.setBackground(null);
		powerBankImageLabel.setAlignmentX(CENTER_ALIGNMENT);
		powerBankImageLabel.setAlignmentY(CENTER_ALIGNMENT);
		powerBankImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		imageSelectPanel.add(powerBankImageLabel);
		
		// Select Image Button
		Dimension buttonSize = new Dimension(150, 30);
		JButton selectImageButton = Utils.createColorBackgroundButton("เลือกรูปภาพ", mainColor, Color.WHITE, 18);
		selectImageButton.setPreferredSize(buttonSize);
		selectImageButton.setMaximumSize(buttonSize);
		selectImageButton.setAlignmentX(CENTER_ALIGNMENT);
		selectImageButton.setAlignmentY(CENTER_ALIGNMENT);
		imageSelectPanel.add(selectImageButton);
		
		selectImageButton.addActionListener(e -> {
			powerBankImage = getImageFromFileChooser("Selected Power Bank");
			
			if (powerBankImage == null) {
				System.err.println("Couldn't get image from file chooser. Unable to show the image.");
				return;
			}
			
			ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(powerBankImage, 200, 200);
			
			powerBankImageLabel.setIcon(scaledPowerBankImage);
		});
		
		return imageSelectPanel;
	}
	
	private BufferedImage getImageFromFileChooser(String imageDescription) {
		BufferedImage image = null;
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./res/power_banks/"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				
				String fileName = f.getName().toLowerCase();
				return fileName.endsWith(".jpg") || fileName.endsWith(".png");
			}

			@Override
			public String getDescription() {
				return "Image Only";
			}
			
		});
		
		int result = fileChooser.showOpenDialog(fileChooser);
		
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedImageFile = fileChooser.getSelectedFile();
			String fileName = selectedImageFile.getName().toLowerCase();
			if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
				image = Utils.createBufferedImage(selectedImageFile);
			}
		}
		
		return image;
	}
	
}