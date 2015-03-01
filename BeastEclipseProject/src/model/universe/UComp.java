package model.universe;

import geometry.Point2D;

import java.awt.Color;

public abstract class UComp {

	public Point2D coord;
	public final Universe universe;
	private boolean updated = true;
	protected boolean destroyed = false;

	public UComp(Universe universe, Point2D coord) {
		this.universe = universe;
		this.coord = coord;
		universe.register(this);
	}
	public abstract void update();
	
	protected void avoidUpdate(){
		updated = false;
		universe.removeFromUpdates(this);
	}
	
	protected void askUpdate(){
		if(updated)
			return;
		updated = true;
		universe.addToUpdates(this);
	}
	
	protected void destroy() {
		universe.unregister(this);
		destroyed = true;
	}
	
	public abstract Color getColor();
	
	public int getSize(){
		return 1;
	}
}
