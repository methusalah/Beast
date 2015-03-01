package model.universe.beast.neuralnetwork.perception;

import geometry.Point2D;
import math.Angle;
import math.MyRandom;
import model.universe.Tile;
import model.universe.beast.neuralnetwork.Brain;

public abstract class ExternalSensor extends Sensor{

	private static final double MAX_VIEW_ANGLE = Angle.RIGHT;
	private static final int MAX_VIEW_DISTANCE = 5;

	final double distance;
	final double angle;

	public ExternalSensor(int serial, Brain brain) {
		super(serial, brain);
		distance = MyRandom.nextInt(MAX_VIEW_DISTANCE);
		angle = (MAX_VIEW_ANGLE/5)*MyRandom.between(-5, +5);
	}
	public ExternalSensor(ExternalSensor other, Brain newBrain){
		super(other, newBrain);
		this.distance = other.distance;
		this.angle = other.angle;
	}
	
	protected Point2D getTarget(){
		Point2D res = Point2D.ORIGIN;
		if(distance > 0)
			res = brain.beast.universe.getInBounds(brain.beast.coord.getTranslation(angle, distance));
		return res;
	}
}
