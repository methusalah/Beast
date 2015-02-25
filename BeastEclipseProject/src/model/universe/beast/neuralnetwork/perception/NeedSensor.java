package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.neuralnetwork.Brain;

public class NeedSensor extends Sensor {
	
	public NeedSensor(int serial, Brain brain) {
		super(serial, brain);
	}
	public NeedSensor(NeedSensor other, Brain newBrain) {
		super(other, newBrain);
	}

	@Override
	public double getStimulationRate() {
		return brain.beast.need.getDepletionRate();
	}
}
