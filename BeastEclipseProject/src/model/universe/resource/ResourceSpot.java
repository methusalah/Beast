package model.universe.resource;

import geometry.Point2D;

import java.awt.Color;

import math.MyRandom;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;

public class ResourceSpot extends UComp {

	public final Resource resource;
	public double q;
	int delay = 0;
	
	public ResourceSpot(Universe universe, Point2D coord, Resource resource) {
		super(universe, coord);
		this.resource = resource;
		q = resource.qStart;
		resource.registerSpot();
	}
	
	@Override
	public void update() {
		if(delay-- > 0)
			return;
			
		q += resource.qGrowth;
		q = Math.min(q, resource.qMax);
		q = Math.max(q, 0);
		
		if(q == 0)
			if(resource.qGrowth <= 0)
				destroy();
			else{
				delay = 1000;
				return;
			}
		
		if(q == resource.qMax)
			if(resource.canExpand){
				if(MyRandom.next()<resource.expandProb){
					boolean hasExpanded = tryToExpand();
					if(hasExpanded)
						avoidUpdate();
				} else
					delay = 1000;
			} else
				avoidUpdate();
		else
			delay = 10;
	}
	
	private boolean tryToExpand(){
		boolean success = false;
		for(Tile n : universe.getTile(coord).getAllNeighbors())
			if(!n.contains(resource)){
				success = true;
				new ResourceSpot(universe, new Point2D(n.x,  n.y), resource);
			}
		return success;
	}
	
	public double getRate(){
		return q/resource.qMax;
	}
	
	public double harvest(double power, double max){
		double harvested = Math.min(max, resource.qHarvest*power);
		q -= harvested;
		if(q < 0){
			harvested+=q;
			q = 0;
//			if(resource.canDisapear)
				destroy();
		}
		askUpdate();
		return harvested;
	}

	@Override
	public Color getColor() {
		int r = resource.color.getRed();
		int g = resource.color.getGreen();
		int b = resource.color.getBlue();
		double rate = Math.min(1, getRate());
		r = (int)((255-r)*(1-rate))+r;
		g = (int)((255-g)*(1-rate))+g;
		b = (int)((255-b)*(1-rate))+b;
		return new Color(r, g, b);
	}
	
	@Override
	protected void destroy() {
		super.destroy();
		resource.unregisterSpot();
	}
}
