package app;


import java.io.File;

import javax.swing.JFrame;

import view.ViewPanel;
import model.Model;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame {

	private ViewPanel panel;
	
	public MainFrame () {
		super ("Beam");
		setLayout(new MigLayout());
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	public void init(Model model) {
		setTitle("Beast");
		if (panel!=null)
			remove(panel);
		panel = new ViewPanel(model);
		add(panel);
		pack();
	}

	public ViewPanel getViewPanel() {
		return panel;
	}
	
}
