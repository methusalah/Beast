package model.universe.beast.neuralnetwork.action;

import model.universe.beast.Beast;


public class Rotator extends Actuator {
	
	private final double angle;
	
	public Rotator(Beast beast, double angle) {
		super(beast);
		this.angle = angle;
	}

	@Override
	public void excite() {
		super.excite();
		beast.rotate(angle);
	}

}
