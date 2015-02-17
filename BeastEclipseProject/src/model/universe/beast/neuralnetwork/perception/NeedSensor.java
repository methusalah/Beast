package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.Need;

public class NeedSensor extends Sensor {
	
	final Need need;
	
	public NeedSensor(Need need) {
		this.need = need;
	}

	@Override
	public void stimulate() {
		polarize(need.getDepletionRate()*THRESOLD_MAX);
	}

}
