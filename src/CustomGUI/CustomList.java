package CustomGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import Utils.Utils;

public class CustomList<E> extends JPanel {
	private DefaultListModel<E> model;
	private ArrayList<E> itemList;
	
	public CustomList(Color backgroundColor) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentY(TOP_ALIGNMENT);
		setBackground(backgroundColor);
		/*
		model = new DefaultListModel<E>();
		setModel(model);
		*/
	
		itemList = new ArrayList<E>();
	}
	
	public void addItem(E item, int fontSize) {
		// model.addElement(item);
		if (itemList == null) {
			System.err.println("Null Item List. Unable to add item to the list.");
			return;
		}
		
		if (itemList.contains(item)) return;
		
		itemList.add(item);
		int itemIndex = itemList.size() - 1;
		this.add(createItemPanel(item, itemIndex, fontSize));
		this.revalidate();
		this.repaint();
	}
	
	public ArrayList<E> getItemList() {
		return itemList;
	}
	
	/*
	@Override
	public ListCellRenderer<Object> getCellRenderer() {
		return new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				
				return createItemPanel(value, index, fontSize);
			}
			
		};
	}
	*/
	

	private JPanel createItemPanel(E item, int itemIndex, int fontSize) {		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(null);
		
		int margin = 5;
		Border marginBorder = BorderFactory.createEmptyBorder(margin, margin, margin, margin);
		Border paddingBorder = Utils.createPaddingBorder(Color.BLACK, 5, 2);
		Border panelBorder = BorderFactory.createCompoundBorder(marginBorder, paddingBorder);
		panel.setBorder(panelBorder);
		
		// Item text
		JLabel textLabel = createPageLabel(item.toString(), Font.PLAIN, fontSize);
		textLabel.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(textLabel, BorderLayout.WEST);
		
		// Delete Button
		ImageIcon deleteIcon = Utils.createImageIcon("/icons/delete_icon_24.png", "Delete Icon");
		JButton deleteButton = Utils.createIconButton(deleteIcon);
		deleteButton.setAlignmentX(RIGHT_ALIGNMENT);
		panel.add(deleteButton, BorderLayout.EAST);
		
		deleteButton.addActionListener(e -> {
			this.remove(panel);
			itemList.remove(item);
			this.revalidate();
			this.repaint();
		});
		
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width,
				panel.getPreferredSize().height));
		
		return panel;
	}
	
	private JLabel createPageLabel(String text, int fontStyle, int fontSize) {
		JLabel pageLabel = new JLabel(text);
		pageLabel.setFont(new Font("Tahoma", fontStyle, fontSize));
		pageLabel.setBackground(null);
		pageLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		return pageLabel;
	}

}
