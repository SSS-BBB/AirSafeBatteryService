package CustomGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class CustomDropDown<E> extends JComboBox<E>  {
	
	public CustomDropDown(E[] dropDownList, Color backgroundColor, Color selectColor) {
		this(dropDownList, backgroundColor, selectColor, 1);
	}
	
	public CustomDropDown(E[] dropDownList, Color backgroundColor, Color selectColor, int borderThickness) {
		this(dropDownList, backgroundColor, Color.BLACK, selectColor, backgroundColor, Color.BLACK, Color.BLACK, borderThickness);
	}
	
	public CustomDropDown(E[] dropDownList, 
			Color backgroundColor, Color fontColor, 
			Color selectColor, Color selectFontColor,
			Color arrowColor,
			Color borderColor, int borderThickness) {
		
		// Just don't use this constructor. I HAVE NO IDEA WHAT'S WRONG WITH IT!
		super(dropDownList);
		
		setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		setBackground(backgroundColor);
		setForeground(fontColor);
		setBorder(BorderFactory.createLineBorder(borderColor, borderThickness));
		
		
		setUI(new MetalComboBoxUI() {
			@Override
			protected ComboPopup createPopup() {
				BasicComboPopup popup = new BasicComboPopup(comboBox) {
					
					@Override
					protected JScrollPane createScroller() {
						JScrollPane scrollPane = new CustomScrollPane(list, fontColor, backgroundColor);
						
						JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
						verticalBar.setBorder(null);
						verticalBar.setPreferredSize(new Dimension(5, 0));
						
						return scrollPane;
					}
				};
				
				
				return popup;
			}
			
			@Override
			public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
				g.setColor(backgroundColor);
				g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
			
			
			@Override
			protected JButton createArrowButton() {
				JButton button = super.createArrowButton();
				
				button.setBackground(backgroundColor);
				button.setForeground(arrowColor);
				button.setBorderPainted(false);
				button.setFocusPainted(false);
				button.setContentAreaFilled(false);
				button.setOpaque(true);
	
				return button;
			}
			
			
		});
		
		setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(
					JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent
						(list, value, index, isSelected, cellHasFocus);
				
				if (index == -1) {
					label.setOpaque(false);
					label.setForeground(fontColor);
					label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				}
				
				else {
					label.setOpaque(true);
					label.setBackground(
							isSelected ? selectColor : backgroundColor
					);
					
					label.setForeground(
							isSelected ? selectFontColor : fontColor
					);
					
					label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				}
				return label;
			}
		});
		
	}
	
	public void setFontSize(int fontSize) {
		setFont(new Font("Tahoma", Font.PLAIN, fontSize));
	}
	
}
