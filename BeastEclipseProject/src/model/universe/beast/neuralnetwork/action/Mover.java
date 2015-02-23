package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;


public class Mover extends Actuator {
	
	
	public Mover(int serial, Brain brain) {
		super(serial, brain);
	}
	public Mover(Mover other, Brain newBrain) {
		super(other, newBrain);
	}

	@Override
	protected void triggerAction() {
		brain.beast.move(power);
	}

}
