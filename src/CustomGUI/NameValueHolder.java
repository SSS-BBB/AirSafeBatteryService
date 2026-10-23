package CustomGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NameValueHolder extends JPanel {
	
	// Name Label to tell what value is being hold
	// Value Label to hold that value inside a box
	
	private JPanel placeHolderBoxPanel;
	private JLabel valueLabel;
	
	public NameValueHolder(String valueName, int headingFontSize, int bodyFontSize, Dimension boxSize) {
		super();
		
		setBackground(null);
		
		// Name before the value
		if (!valueName.isEmpty()) createTextWithComponentToAdd(this, valueName, headingFontSize);
		
		// Value inside a box
		JPanel holderBoxPanel = createPlaceHolderBox(boxSize);
		this.add(holderBoxPanel);
		
		valueLabel = createComponentLabel(Font.PLAIN, bodyFontSize);
		valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		holderBoxPanel.add(valueLabel, BorderLayout.WEST);
	}
	
	public NameValueHolder(String valueName, int headingFontSize, int bodyFontSize) {
		// Name value holder without a box
		
		super();
		
		setBackground(null);
		
		// Name before the value
		if (!valueName.isEmpty()) createTextWithComponentToAdd(this, valueName, headingFontSize);
		
		// Value not inside a box
		valueLabel = createComponentLabel(Font.PLAIN, bodyFontSize);
		valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		this.add(valueLabel);
	}
	
	public NameValueHolder(String valueName, String valueText, int headingFontSize, int bodyFontSize) {
		// Name value holder without a box
		
		super();
		
		setBackground(null);
		
		// Name before the value
		if (!valueName.isEmpty()) createTextWithComponentToAdd(this, valueName, headingFontSize);
		
		// Value not inside a box
		valueLabel = createComponentLabel(Font.PLAIN, bodyFontSize);
		valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		valueLabel.setText(valueText);
		this.add(valueLabel);
	}
	
	public String getValue() {
		if (valueLabel == null) {
			System.err.println("Null Value Label. Unable to get a value from Name Value Holder.");
			return "";
		}
		
		return valueLabel.getText();
	}
	
	public void setValue(String valueText) {
		if (valueLabel == null) {
			System.err.println("Null Value Label. Unable to set a value to Name Value Holder.");
			return;
		}
		
		valueLabel.setText(valueText);
	}
	
	public void setValueLabelAlignment(float alignment) {
		if (valueLabel == null) {
			System.err.println("Null Value Label. Unable to set value label alignment.");
			return;
		}
		
		valueLabel.setAlignmentX(alignment);
	}
	
	public void setBoxColor(Color color) {
		if (placeHolderBoxPanel == null) {
			System.err.println("Null place holder box panel. Unable to set box color.");
			return;
		}
		
		placeHolderBoxPanel.setBorder(BorderFactory.createLineBorder(color, 2));
	}
	
	private JPanel createPlaceHolderBox(Dimension size) {
		placeHolderBoxPanel = new JPanel(new BorderLayout());
		placeHolderBoxPanel.setBackground(null);
		placeHolderBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		placeHolderBoxPanel.setPreferredSize(size);
		
		return placeHolderBoxPanel;
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
