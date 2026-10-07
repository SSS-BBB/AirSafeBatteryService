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
	
	public CustomDropDown(E[] dropDownList, Color backgroundColor, Color menuColor) {
		super(dropDownList);
		
		setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		setBackground(backgroundColor);
		setForeground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		setUI(new MetalComboBoxUI() {
			@Override
			protected ComboPopup createPopup() {
				BasicComboPopup popup = new BasicComboPopup(comboBox) {
					
					@Override
					protected JScrollPane createScroller() {
						JScrollPane scrollPane = new CustomScrollPane(list, Color.BLACK, backgroundColor);
						
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
				button.setForeground(Color.BLACK);
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
				
				
				
				label.setBackground(
						isSelected ? menuColor : backgroundColor
				);
				
				label.setForeground(
						isSelected ? backgroundColor : Color.BLACK
				);
				
				label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				
				return label;
			}
		});
		
	}
	
}
