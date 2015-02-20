package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;


public class Mover extends Actuator {
	
	private final double speedRate;
	
	public Mover(int serial, Brain brain, double speedRate) {
		super(serial, brain);
		this.speedRate = speedRate;
	}
	public Mover(Mover other, Brain newBrain) {
		super(other, newBrain);
		this.speedRate = other.speedRate;
	}

	@Override
	public void excite() {
		super.excite();
		brain.beast.move(speedRate);
	}

}
