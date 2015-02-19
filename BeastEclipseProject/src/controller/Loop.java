package controller;

import model.Model;
import utils.LogUtil;
import view.ViewPanel;

public class Loop {
	private static final double DEFAULT_FPS = 15;

	private Model model;
	private ViewPanel view;
	
	long lastRepaint;
	double fps = DEFAULT_FPS;
	
	public Loop(Model model, ViewPanel view) {
		this.model = model;
		this.view = view;
		lastRepaint = 0;
	}
	
	public void start() {
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						while (true) {
							try {
								synchronized (model) {
									model.universe.update();
								}
								
								double elpasedTime = System.currentTimeMillis()-lastRepaint;
								if(elpasedTime > 1000/fps){
									view.repaint();
									lastRepaint = System.currentTimeMillis();
								}
							} catch (Exception e) {
								// OK something went wrong... we're not going to stop everything for such a small thing, are we ?
								e.printStackTrace();
							}
						}
					}
				}		
			).start();
	}	
}
