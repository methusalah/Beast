package model.universe.resource;

import java.awt.Color;

import geometry.Point2D;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;
import utils.MyRandom;

public class ResourceSpot extends UComp {

	public final Resource resource;
	double q;
	int delay = 0;
	
	public ResourceSpot(Universe universe, Point2D coord, Resource resource) {
		super(universe, coord);
		this.resource = resource;
		q = resource.qStart;
	}
	
	@Override
	public void update() {
		if(delay-- > 0)
			return;
			
		q += resource.qGrowth;
		q = Math.min(q, resource.qMax);
		q = Math.max(q, 0);
		
		if(q == 0 && resource.canDisapear)
			destroy();
		
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
	
	public double harvest(){
		double harvested = resource.qHarvest;
		q -= harvested;
		if(q < 0){
			harvested+=q;
			q = 0;
			if(resource.canDisapear)
				destroy();
		}
		return harvested;
	}

	@Override
	public Color getColor() {
		return resource.color;
	}
}
