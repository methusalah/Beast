package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.neuralnetwork.Brain;

public class NeedSensor extends Sensor {
	
	public NeedSensor(int serial, Brain brain) {
		super(serial, brain);
	}

	@Override
	public void stimulate() {
		polarize(brain.beast.need.getDepletionRate()*THRESOLD_MAX);
	}

}
