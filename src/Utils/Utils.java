package Utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class Utils {
	public static ImageIcon createImageIcon(String path, String description) {
		
		InputStream inputStream = Utils.class.getResourceAsStream(path);

		if (inputStream == null) {
			System.err.println("Couldn't find file: " + path);
			return null;
		}

		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(inputStream);
			if (bufferedImage == null) {
				System.err.println("Couldn't decide the image from: " + path);
				return null;
			}

			return new ImageIcon(bufferedImage, description);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Border createPaddingBorder(Color color, int thickness, Insets paddings, Insets margins) {
		Border marginBorder = BorderFactory.createEmptyBorder(margins.top, margins.left, margins.bottom, margins.right);

		Border lineBorder = BorderFactory.createLineBorder(color, thickness);
		Border paddingBorder = BorderFactory.createEmptyBorder(paddings.top, paddings.left, paddings.bottom,
				paddings.right);
		Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

		Border compoundBorder2 = BorderFactory.createCompoundBorder(marginBorder, compoundBorder);

		return compoundBorder2;
	}

	public static Border createPaddingBorder(Color color, Insets paddings) {
		return createPaddingBorder(color, 1, paddings, new Insets(0, 0, 0, 0));
	}

	public static Border createPaddingBorder(Color color, int padding) {
		return createPaddingBorder(color, 1, new Insets(padding, padding, padding, padding), new Insets(0, 0, 0, 0));
	}
	
	public static JButton createNoBackgroundButton(String text, Color color) {
		JButton noBgButton = new JButton(text);
		noBgButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		noBgButton.setForeground(color);
		noBgButton.setBackground(null);
		noBgButton.setFocusPainted(false);
		noBgButton.setBorder(null);
		noBgButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return noBgButton;
	}
	
	public static JTextField createAppTextField(Color backgroundColor) {
		JTextField textField = new JTextField();
		textField.setBackground(backgroundColor);
		textField.setPreferredSize(new Dimension(350, 35));
		textField.setMaximumSize(new Dimension(350, 35));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		return textField;
	}
	
	public static <T> JComboBox<T> createAppDropdown(T[] dropDownList, Color backgroundColor, Color menuColor) {
		JComboBox<T> dropDown = new JComboBox<T>(dropDownList);
		dropDown.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		// Drop down style
		dropDown.setBackground(backgroundColor);
		dropDown.setForeground(Color.BLACK);
		dropDown.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		// Selected item style
		dropDown.setRenderer(new DefaultListCellRenderer() {
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
		
		
		dropDown.setUI(new MetalComboBoxUI() {

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
		
		

		return dropDown;
	}
}
