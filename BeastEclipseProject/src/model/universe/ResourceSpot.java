package model.universe;

import utils.MyRandom;

public class ResourceSpot extends UComp {

	public final Resource resource;
	double q;
	
	public ResourceSpot(Tile tile, Resource resource) {
		super(tile);
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
		Tile n = tile.getAnyNeighbor();
		for(UComp c : n.comps)
			if(c instanceof ResourceSpot)
				if(((ResourceSpot)c).resource == resource)
					return;
		new ResourceSpot(n, resource);
	}
	
	public double getRate(){
		return q/resource.qMax;
	}
}
