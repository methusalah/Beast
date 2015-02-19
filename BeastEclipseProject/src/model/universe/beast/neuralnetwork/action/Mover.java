package model.universe.beast.neuralnetwork.action;

import model.universe.beast.Beast;


public class Mover extends Actuator {
	
	private final double speedRate;
	
	public Mover(Beast beast, double speedRate) {
		super(beast);
		this.speedRate = speedRate;
	}

	@Override
	public void excite() {
		super.excite();
		beast.move(speedRate);
	}

}
