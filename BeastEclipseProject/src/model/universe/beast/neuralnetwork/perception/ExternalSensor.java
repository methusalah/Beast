package model.universe.beast.neuralnetwork.perception;

import model.universe.Universe;
import model.universe.beast.Beast;

public abstract class ExternalSensor extends Sensor{

	final Universe universe;
	final Beast beast;
	
	public ExternalSensor(Universe universe, Beast beast) {
		this.universe = universe;
		this.beast = beast;
	}
}
