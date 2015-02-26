package controller;

import geometry.Point2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import model.Model;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.beast.Beast;
import tools.LogUtil;
import view.ViewPanel;

public class Loop implements KeyListener, MouseListener{
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

	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D coordInModel = new Point2D(e.getX()/view.SCALE, e.getY()/view.SCALE);
		Beast closest = null;
		synchronized (model) {
			for(Beast b : model.universe.beasts)
				if(b.gen > 0 && 
						(closest == null || b.coord.getDistance(coordInModel) < closest.coord.getDistance(coordInModel)))
					closest = b;
		}
		LogUtil.logger.info(""+closest.brain);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}	
}


































