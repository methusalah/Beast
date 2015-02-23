package app;


import java.util.logging.Level;
import java.util.logging.Logger;

import math.MyRandom;
import model.Model;
import tools.LogUtil;
import view.ViewPanel;
import controller.Loop;

public class BeastMain {
	public static void main(String[] args) throws Exception {

		Logger.getLogger("").setLevel(Level.INFO);
		LogUtil.init();
		MyRandom.changeSeed(7);
		LogUtil.logger.info("Seed : "+MyRandom.SEED);
		
		Model model = new Model(150, 50);
		
		final MainFrame frame = new MainFrame(model);
		ViewPanel view = frame.getViewPanel();
		
		Loop l = new Loop(model, view);
		frame.addKeyListener(l);
		l.start();
		
	}
	
}
