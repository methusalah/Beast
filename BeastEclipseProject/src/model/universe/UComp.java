package model.universe;

import java.awt.Color;

import geometry.Point2D;

public abstract class UComp {

	public Point2D coord;
	final protected Universe universe;

	public UComp(Universe universe, Point2D coord) {
		this.universe = universe;
		this.coord = coord;
		universe.register(this);
	}
	public abstract void update();
	
	protected void avoidUpdate(){
		universe.removeFromUpdates(this);
	}
	
	protected void askUpdate(){
		universe.addToUpdates(this);
	}
	
	protected void destroy() {
		universe.unregister(this);
	}
	
	public abstract Color getColor();
	public int getSize(){
		return 1;
	}
}
