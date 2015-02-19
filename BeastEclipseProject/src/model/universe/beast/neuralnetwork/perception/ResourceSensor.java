package model.universe.beast.neuralnetwork.perception;

import model.universe.Universe;
import model.universe.beast.Beast;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class ResourceSensor extends ExternalSensor {

	final Resource resource;

	public ResourceSensor(int serial, Brain brain, Resource resource) {
		super(serial, brain);
		this.resource = resource;
	}

	@Override
	public void stimulate() {
		ResourceSpot spot = brain.beast.universe.getResourceSpot(resource, brain.beast.coord);
		if(spot == null)
			return;
		polarize(spot.getRate()*THRESOLD_MAX);
	}
	
	

}
