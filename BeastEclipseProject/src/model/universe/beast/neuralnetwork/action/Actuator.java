package model.universe.beast.neuralnetwork.action;

import math.MyRandom;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.Neuron;

public abstract class Actuator extends Neuron {
	
	double power;
	
	
	final Brain brain; 
	
	public Actuator(int serial, Brain brain) {
		super(serial);
		this.brain = brain;
		setRandomPower();
	}
	public Actuator(Actuator other, Brain newBrain) {
		super(other);
		this.brain = newBrain;
		this.power = other.power;
	}
	
	public void setRandomPower(){
		power = MyRandom.next();
	}
	
	public abstract void act();

}
