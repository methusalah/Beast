package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.neuralnetwork.Brain;

public class AttackSensor extends Sensor {

	public AttackSensor(int serial, Brain brain) {
		super(serial, brain);
	}
	
	public AttackSensor(AttackSensor other, Brain newBrain){
		super(other, newBrain);
	}

	@Override
	public double getStimulationRate() {
		if(brain.beast.grabAttackedState())
			return 1;
		else
			return 0;
	}

}
