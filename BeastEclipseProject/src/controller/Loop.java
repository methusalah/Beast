package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.Model;
import view.ViewPanel;

public class Loop implements KeyListener {
	private static final double DEFAULT_FPS = 25;

	private Model model;
	private ViewPanel view;
	
	private boolean realtime = false;
	
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
							if(!realtime)
								synchronized (model) {
									model.universe.update();
								}
							double elpasedTime = System.currentTimeMillis()-lastRepaint;
							if(elpasedTime > 1000/fps){
								synchronized (model) {
									model.universe.update();
								}
								view.repaint();
								lastRepaint = System.currentTimeMillis();
							}
						}
					}
				}		
			).start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isShiftDown())
			switch(e.getKeyChar()){
			case ' ': ; break;
			}
		else
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE: realtime = !realtime; break;
			case KeyEvent.VK_A: fps++; break;
			case KeyEvent.VK_Q: fps--; break;
			}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}	
}
