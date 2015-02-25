package model.universe.beast.neuralnetwork.perception;

import geometry.Point2D;
import math.Angle;
import math.MyRandom;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class ResourceSensor extends ExternalSensor {
	private static final double MAX_ANGLE = Angle.RIGHT;
	private static final double MAX_VIEW_DISTANCE = 5;

	final Resource resource;
	final double distance;
	final double angle;

	public ResourceSensor(int serial, Brain brain, Resource resource) {
		super(serial, brain);
		this.resource = resource;
		this.angle = MyRandom.between(-MAX_ANGLE, MAX_ANGLE);
		this.distance = MyRandom.between(0, MAX_VIEW_DISTANCE);
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
