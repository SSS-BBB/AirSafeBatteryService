package CustomGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NameDropDownHolder extends JPanel {
	
	private CustomDropDown<String> dropDown;
	
	public NameDropDownHolder(String[] dropDownList, String valueName, 
							 int headingFontSize, int bodyFontSize, 
							 Color dropDownBackgroundColor, Color dropDownSelectColor) {
		super();
		
		setBackground(null);
		
		dropDown = new CustomDropDown<String>(dropDownList, dropDownBackgroundColor, dropDownSelectColor, 2);
		
		createTextWithComponentToAdd(this, valueName, headingFontSize);
		this.add(dropDown);
		
	}
	
	private JLabel createComponentLabel(int fontStyle, int fontSize) {
		JLabel label = new JLabel();
		label.setFont(new Font("Tahoma", fontStyle, fontSize));
		label.setBackground(null);
		label.setAlignmentX(LEFT_ALIGNMENT);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
		
		return label;
	}
	
	private void createTextWithComponentToAdd(JPanel holderPanel, String text, int fontSize) {
		holderPanel.setLayout((new FlowLayout(FlowLayout.LEFT, 5, 0)));
		holderPanel.setAlignmentX(LEFT_ALIGNMENT);
		holderPanel.setBackground(null);
		
		JLabel textLabel = createComponentLabel(Font.BOLD, fontSize);
		textLabel.setText(text);
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		holderPanel.add(textLabel);
	}
}
