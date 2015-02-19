package app;


import java.util.logging.Level;
import java.util.logging.Logger;

import controller.Loop;
import model.Model;
import utils.LogUtil;
import view.ViewPanel;

public class BeastMain {
	public static void main(String[] args) throws Exception {

		Logger.getLogger("").setLevel(Level.INFO);
		LogUtil.init();
		
		Model model = new Model(50, 50);
		
		final MainFrame frame = new MainFrame(model);
		ViewPanel view = frame.getViewPanel();
		
		Loop l = new Loop(model, view);
		l.start();
		
	}
	
}
