package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;


public class Rotator extends Actuator {
	
	private final double angle;
	
	public Rotator(int serial, Brain brain, double angle) {
		super(serial, brain);
		this.angle = angle;
	}
	public Rotator(Rotator other, Brain newBrain) {
		super(other, newBrain);
		this.angle = other.angle;
	}

	@Override
	public void excite() {
		super.excite();
		brain.beast.rotate(angle);
	}

}
