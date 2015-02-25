package model.universe.beast.neuralnetwork.perception;

import geometry.Point2D;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class ResourceSensor extends ExternalSensor {

	final Resource resource;
	final double distance;
	final double angle;

	public ResourceSensor(int serial, Brain brain, Resource resource, double distance, double angle) {
		super(serial, brain);
		this.resource = resource;
		this.angle = angle;
		this.distance = distance;
	}
	public ResourceSensor(ResourceSensor other, Brain newBrain) {
		super(other, newBrain);
		this.resource = other.resource;
		this.angle = other.angle;
		this.distance = other.distance;
	}

	@Override
	public double getStimulationRate() {
		Point2D p = Point2D.ORIGIN;
		if(distance > 0)
			p = brain.beast.universe.getInBounds(brain.beast.coord.getTranslation(angle, distance));
		ResourceSpot spot = brain.beast.universe.getResourceSpot(resource, p);
		if(spot == null)
			return 0;
		else
			return spot.getRate();
	}
}
