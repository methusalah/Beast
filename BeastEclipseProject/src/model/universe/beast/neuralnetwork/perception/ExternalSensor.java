package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.neuralnetwork.Brain;

public abstract class ExternalSensor extends Sensor{

	public ExternalSensor(int serial, Brain brain) {
		super(serial, brain);
	}
	public ExternalSensor(ExternalSensor other, Brain newBrain){
		super(other, newBrain);
	}
}
