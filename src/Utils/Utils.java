package Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
	
	public static ImageIcon scaleImageKeepRatio(BufferedImage image, int maxWidth, int maxHeight) {
		if (image == null || maxWidth < 0 || maxHeight < 0) {
			System.err.println("Invalid parameters. Unable to scale the image while keeping the ratio.");
			return null;
		}
		
		int originalWidth = image.getWidth();
		int originalHeight = image.getHeight();
		
		double scaledWidth = maxWidth;
		double scaledHeight = maxHeight;
		
		if (maxHeight <= (maxWidth * originalHeight) / originalWidth) {
			// change width
			scaledWidth = (double) ((originalWidth / (originalHeight * 1.0)) * maxHeight);
		}
		else {
			// change height
			scaledWidth = (double) ((originalHeight / (originalWidth * 1.0)) * maxWidth);
		}
		
		Image scaledImage = image.getScaledInstance((int) scaledWidth, (int) scaledHeight, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}
	
	public static String insertArrayListToString(ArrayList<String> list, String start, String delimiter) {
		if (list == null) {
			System.err.println("Null List. Unable to insert array list to string.");
			return "";
		}
		
		String result = start;
		
		for (int i = 0; i < list.size() - 1; i++) {
			result += list.get(i) + delimiter;
		}
		
		// insert last string in the list without delimiter
		result += list.get(list.size() - 1);
		
		return result;
	}
	
	public static int randRange(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}
	
	public static String getCalendarDateFormat(Calendar c) {
		return getCalendarDateFormat(c, "yyyy-MM-dd");
	}
	
	public static String getCalendarDateFormat(Calendar c, String format) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
		String formattedDate = dateFormatter.format(c.getTime());
		return formattedDate;
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
	
	public static Border createPaddingBorder(Color color, int padding, int thickness) {
		return createPaddingBorder(color, thickness, new Insets(padding, padding, padding, padding), new Insets(0, 0, 0, 0));
	}
	
	public static JPanel createBoxWithLabelInside(String text, Color boxColor, Color fontColor, int fontSize) {
		JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		wrapperPanel.setBackground(null);
		
		JPanel boxPanel = new JPanel(new BorderLayout());
		boxPanel.setBackground(boxColor);
		// boxPanel.setPreferredSize(new Dimension(180, 30));
		wrapperPanel.add(boxPanel);
		
		JLabel label = new JLabel(text);
		label.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		label.setForeground(fontColor);
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		boxPanel.add(label, BorderLayout.CENTER);
		
		return wrapperPanel;
	}
	
	public static JButton createNoBackgroundButton(String text, Color color) {
		JButton noBgButton = new JButton(text);
		noBgButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		noBgButton.setForeground(color);
		noBgButton.setBackground(null);
		noBgButton.setFocusPainted(false);
		noBgButton.setBorder(null);
		noBgButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		noBgButton.setOpaque(true);
		noBgButton.setContentAreaFilled(false);
		
		return noBgButton;
	}
	
	public static JButton createColorBackgroundButton(String text, Color backgroundColor, Color fontColor, int fontSize) {
		JButton button = new JButton(text);
		button.setFont(new Font("Tahoma", Font.BOLD, fontSize));
		button.setBackground(backgroundColor);
		button.setForeground(fontColor);
		button.setFocusPainted(false);
		button.setBorder(null);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		return button;
	}
	
	public static JButton createBorderButton(String text, Color borderColor, Color textColor, int fontSize) {
		return createBorderButton(text, borderColor, textColor, fontSize, 1);
	}
	
	public static JButton createBorderButton(String text, Color borderColor, Color textColor, int fontSize, int lineThickness) {
		JButton button = new JButton(text);
		button.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBackground(null);
		button.setFocusPainted(false);
		button.setForeground(textColor);
		button.setBorder(Utils.createPaddingBorder(borderColor, 5, lineThickness));
		
		return button;
	}
	
	public static JTextField createAppTextField(Color backgroundColor, int size) {
		JTextField textField = new JTextField(size);
		textField.setBackground(backgroundColor);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		return textField;
	}
	
	public static JTextField createAppTextField(Color backgroundColor, Dimension size) {
		JTextField textField = new JTextField();
		textField.setBackground(backgroundColor);
		textField.setPreferredSize(size);
		textField.setMaximumSize(size);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		return textField;
	}
	
	public static JTextField createAppTextField(Color backgroundColor) {
		return createAppTextField(backgroundColor, new Dimension(350, 35));
	}
	
	public static JButton createIconButton(ImageIcon icon) {
		if (icon == null) {
			System.err.println("Null icon. Unable to create an icon button.");
			return new JButton("No Icon");
		}
		
		JButton button = new JButton(icon);
		button.setBackground(null);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}
	
	public static JButton createNoBackgroundButton(String text, Color fontColor, int fontSize) {
		JButton button = new JButton(text);
		button.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		button.setBackground(null);
		button.setForeground(fontColor);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return button;
	}
	
	public static String[] setToArray(Set<String> s) {
		if (s == null) {
			System.err.println("Null set. unable to turn this set into an array.");
			return null;
		}
		
		String[] arr = new String[s.size()];
		
		int i = 0;
		for (String element : s)
			arr[i++] = element;
		
		return arr;
	}
	
	public static String[] listToArray(List<String> l) {
		if (l == null) {
			System.err.println("Null list. unable to turn this list into an array.");
			return null;
		}
		
		String[] arr = new String[l.size()];
		arr = l.toArray(arr);
		return arr;
	}
	
	public static double getNumberFromTextField(JTextField textField, double invalidNumber) {
		try {
			double num = Double.parseDouble(textField.getText());
			return num;
		}
		catch (NumberFormatException e) {
			// return invalidNumber when the text in text field is not a number
			return invalidNumber;
		}
	}
	
	public static <T> JComboBox<T> createAppDropdown(T[] dropDownList, Color backgroundColor, Color menuColor) {
		// Drop down with default size
		return createAppDropdown(dropDownList, backgroundColor, menuColor, new Dimension(0, 0));
	}
	
	public static <T> JComboBox<T> createAppDropdown(T[] dropDownList, Color backgroundColor, Color menuColor, Dimension size) {
		JComboBox<T> dropDown = new JComboBox<T>(dropDownList);
		dropDown.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		// Drop down style
		dropDown.setBackground(backgroundColor);
		dropDown.setForeground(Color.BLACK);
		dropDown.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		if (size.width > 0 && size.height > 0)
			dropDown.setPreferredSize(size);
		
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
	
	public static boolean isDatePast(Calendar date) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		
		return date.before(today);
	}
	
	public static int countDays(Calendar c1, Calendar c2) {
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		
		return (int) ChronoUnit.DAYS.between(c1.toInstant(), c2.toInstant());
	}
	
	public static void showDialog(JFrame frame, String title, String detail) {
		JDialog dialog = new JDialog(frame, title, true);
		dialog.setBackground(Color.WHITE);
		dialog.setSize(800, 150);
		
		JPanel wrapperPanel = new JPanel(new BorderLayout());
		wrapperPanel.setBackground(Color.WHITE);
		dialog.add(wrapperPanel);
		
		JLabel detailLabel = new JLabel(detail, SwingConstants.CENTER);
		detailLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		detailLabel.setForeground(Color.BLACK);
		detailLabel.setBackground(Color.WHITE);
		wrapperPanel.add(detailLabel, BorderLayout.CENTER);
		
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
}
