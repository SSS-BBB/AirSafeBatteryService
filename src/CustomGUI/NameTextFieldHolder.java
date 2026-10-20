package CustomGUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Utils.Utils;

public class NameTextFieldHolder extends JPanel {
	
	private JTextField textField;
	
	public NameTextFieldHolder(String valueName, int fontSize, int textFieldSize,
			Color nameColor, Color textFieldBackground, Color textFieldFont) {
		
		super();
		
		setBackground(null);
		
		textField = Utils.createAppTextField(textFieldBackground, textFieldSize, fontSize);
		textField.setForeground(textFieldFont);
		
		createTextWithComponentToAdd(this, valueName, fontSize, nameColor);
		
		this.add(textField);
	}
	
	public String getValue() {
		if (textField == null) {
			System.err.println("Null Text Field. Unable to get a value from the text field.");
			return "";
		}
		
		return textField.getText();
	}
	
	private JLabel createComponentLabel(int fontStyle, int fontSize, Color fontColor) {
		JLabel label = new JLabel();
		label.setFont(new Font("Tahoma", fontStyle, fontSize));
		label.setBackground(null);
		label.setForeground(fontColor);
		label.setAlignmentX(LEFT_ALIGNMENT);
		// label.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		
		return label;
	}
	
	private void createTextWithComponentToAdd(JPanel holderPanel, String text, int fontSize, Color fontColor) {
		holderPanel.setLayout((new FlowLayout(FlowLayout.LEFT, 5, 0)));
		holderPanel.setAlignmentX(LEFT_ALIGNMENT);
		holderPanel.setBackground(null);
		
		JLabel textLabel = createComponentLabel(Font.BOLD, fontSize, fontColor);
		textLabel.setText(text);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		holderPanel.add(textLabel);
	}
	
}
