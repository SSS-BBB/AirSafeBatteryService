package GeneralPages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import CustomGUI.CustomScrollPane;
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
	
	public PowerBankAddPage(UserMainFrame userMainFrame, Color backgroundColor, Color mainColor, Color menuColor) {
		super(new BorderLayout());
		
		this.userMainFrame = userMainFrame;
		this.backgroundColor = backgroundColor;
		this.mainColor = mainColor;
		this.menuColor = menuColor;
		
		setBackground(backgroundColor);
		
		JPanel powerBankImageSelectPanel = createPowerBankImageSelectPanel();
		this.add(powerBankImageSelectPanel, BorderLayout.NORTH);
		
		CustomScrollPane detailInputPanel = createDetailInputPanel();
		this.add(detailInputPanel, BorderLayout.CENTER);
	}
	
	private CustomScrollPane createDetailInputPanel() {
		JPanel detailInputPanel = new JPanel(new GridLayout(0, 3));
		detailInputPanel.setBackground(backgroundColor);
		
		CustomScrollPane detailScrollPane = new CustomScrollPane(detailInputPanel, menuColor, backgroundColor);
		
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
		
		// Input
		
		return detailScrollPane;
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
		
		powerBankImageLabel = new JLabel();
		powerBankImageLabel.setBackground(null);
		powerBankImageLabel.setAlignmentX(CENTER_ALIGNMENT);
		powerBankImageLabel.setAlignmentY(CENTER_ALIGNMENT);
		powerBankImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		imageSelectPanel.add(powerBankImageLabel);
		
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
			
			ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(powerBankImage, 250, 250);
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