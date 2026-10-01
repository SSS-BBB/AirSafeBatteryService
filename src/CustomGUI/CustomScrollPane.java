package CustomGUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollPane extends JScrollPane {
	
	private Color scrollThumbColor, scrollTrackColor;
	
	public CustomScrollPane(JComponent view, Color scrollThumbColor, Color scrollTrackColor) {
		super(view);
		
		this.scrollThumbColor = scrollThumbColor;
		this.scrollTrackColor = scrollTrackColor;
		
		setBorder(null);
		
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar verticalBar = getVerticalScrollBar();
		verticalBar.setBorder(null);
		verticalBar.setUI(new BasicScrollBarUI() {
			
			@Override
			protected void configureScrollBarColors() {
				thumbColor = scrollThumbColor;
				trackColor = scrollTrackColor;
			}
			
			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createNullButton();
			}
			
			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createNullButton();
			}
			
			private JButton createNullButton() {
				// to remove increase and decrease button of the scroll bar
				JButton nullButton = new JButton();
				nullButton.setPreferredSize(new Dimension(0, 0));
				nullButton.setMinimumSize(new Dimension(0, 0));
				nullButton.setMaximumSize(new Dimension(0, 0));
				return nullButton;
			}
			
		});
		
	}
	
}
