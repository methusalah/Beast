package model.universe.beast.neuralnetwork.action;

import math.MyRandom;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.Neuron;

public abstract class Actuator extends Neuron {
	
	double power;
	
	public Actuator(int serial, Brain brain) {
		super(serial, brain);
		setRandomPower();
	}
	public Actuator(Actuator other, Brain newBrain) {
		super(other, newBrain);
		this.power = other.power;
	}
	
	public void setRandomPower(){
		power = MyRandom.next();
	}
	
	@Override
	public abstract void excite();
}
