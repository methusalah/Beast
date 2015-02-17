package model.universe.beast.neuralnetwork.perception;

import model.universe.Universe;
import model.universe.beast.Beast;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class ResourceSensor extends ExternalSensor {

	final Resource resource;

	public ResourceSensor(Universe universe, Beast beast, Resource resource) {
		super(universe, beast);
		this.resource = resource;
	}

	@Override
	public void stimulate() {
		ResourceSpot spot = universe.getResourceSpot(resource, beast.coord);
		if(spot == null)
			return;
		polarize(spot.getRate()*THRESOLD_MAX);
	}
	
	

}
