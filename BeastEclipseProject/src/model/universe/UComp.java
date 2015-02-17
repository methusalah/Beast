package model.universe;

import geometry.Point2D;

public abstract class UComp {

	protected Point2D coord;
	final protected Universe universe;

	public UComp(Universe universe, Point2D coord) {
		this.universe = universe;
		this.coord = coord;
		universe.registerNewComp(this);
	}
	public abstract void update();
	
	protected void destroy() {
		universe.unregisterDestroyedComp(this);
	}
}
