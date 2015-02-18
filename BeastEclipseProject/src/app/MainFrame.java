package app;


import javax.swing.JFrame;

import view.ViewPanel;
import model.Model;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame {

	private ViewPanel panel;
	
	public MainFrame (Model model) {
		super ("Beam");
		setLayout(new MigLayout());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		panel = new ViewPanel(model);
		add(panel);
		pack();
	}
	
	public ViewPanel getViewPanel() {
		return panel;
	}
	
}
