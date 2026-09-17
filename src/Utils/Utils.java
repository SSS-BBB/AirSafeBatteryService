package Utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

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
}
