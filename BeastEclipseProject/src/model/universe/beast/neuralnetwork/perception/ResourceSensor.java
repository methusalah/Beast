package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class ResourceSensor extends ExternalSensor {
	final Resource resource;

	public ResourceSensor(int serial, Brain brain, Resource resource) {
		super(serial, brain);
		this.resource = resource;
	}
	public ResourceSensor(ResourceSensor other, Brain newBrain) {
		super(other, newBrain);
		this.resource = other.resource;
	}

	@Override
	public double getStimulationRate() {
		ResourceSpot spot = brain.beast.universe.getResourceSpot(resource, getTarget());
		if(spot == null)
			return 0;
		else
			return spot.getRate();
	}
}
