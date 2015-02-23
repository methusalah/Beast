package app;


import java.util.Random;
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
		int seed = (int)(Math.random()*10000000);
		MyRandom.changeSeed(seed);
		LogUtil.logger.info("Seed : "+seed);
		
		Model model = new Model(150, 50);
		
		final MainFrame frame = new MainFrame(model);
		ViewPanel view = frame.getViewPanel();
		
		Loop l = new Loop(model, view);
		frame.addKeyListener(l);
		l.start();
		
	}
	
}
