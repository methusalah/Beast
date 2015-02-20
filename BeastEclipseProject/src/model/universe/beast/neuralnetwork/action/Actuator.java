package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.Neuron;

public class Actuator extends Neuron{
	
	final Brain brain;
	
	public Actuator(int serial, Brain brain) {
		super(serial);
		this.brain = brain;
	}
	public Actuator(Actuator other, Brain newBrain) {
		super(other);
		this.brain = newBrain;
	}

}
