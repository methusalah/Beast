package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;


public class Rotator extends Actuator {
	
	
	public Rotator(int serial, Brain brain) {
		super(serial, brain);
	}
	public Rotator(Rotator other, Brain newBrain) {
		super(other, newBrain);
	}

	@Override
	protected void triggerAction() {
		brain.beast.rotate(power);
	}
}
