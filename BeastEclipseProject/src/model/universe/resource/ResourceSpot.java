package model.universe.resource;

import geometry.Point2D;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;
import utils.MyRandom;

public class ResourceSpot extends UComp {

	public final Resource resource;
	double q;
	
	public ResourceSpot(Universe universe, Point2D coord, Resource resource) {
		super(universe, coord);
		this.resource = resource;
		q = resource.qStart;
	}
	
	@Override
	public void update() {
		q += resource.qGrowth;
		q = Math.min(q, resource.qMax);
		q = Math.max(q, 0);
		
		if(q == 0 && resource.canDisapear)
			destroy();
		
		if(q == resource.qMax && resource.canExpand && MyRandom.next()<resource.expandProb)
			expand();
	}
	
	private void expand(){
		Tile n = universe.getTile(coord).getAnyNeighbor();
		for(UComp c : n.comps)
			if(c instanceof ResourceSpot)
				if(((ResourceSpot)c).resource == resource)
					return;
		n.register(new ResourceSpot(universe, new Point2D(n.x,  n.y), resource));
	}
	
	public double getRate(){
		return q/resource.qMax;
	}
}
