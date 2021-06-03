package view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LegendPanel extends JPanel{
	
	public LegendPanel(String title) {
		this.setBorder(BorderFactory.createTitledBorder(title));
		this.setLayout(null);
		this.setVisible(true);
	}
}
