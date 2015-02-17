package model.universe;

import geometry.Point2D;

public abstract class UComp {

	public Point2D coord;
	final protected Universe universe;

	public UComp(Universe universe, Point2D coord) {
		this.universe = universe;
		this.coord = coord;
		askUpdate();
	}
	public abstract void update();
	
	protected void avoidUpdate(){
		universe.unregister(this);
	}
	
	protected void askUpdate(){
		universe.register(this);
	}
	
	protected void destroy() {
		universe.destroy(this);
	}
}
