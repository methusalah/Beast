package model.universe.beast.neuralnetwork.perception;

import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.Neuron;

public abstract class Sensor extends Neuron{
	
	final Brain brain;
	
	public Sensor(int serial, Brain newBrain) {
		super(serial);
		this.brain = newBrain;
	}
	
	public Sensor(Sensor other, Brain newBrain){
		super(other);
		this.brain = newBrain;
	}

	public abstract void stimulate();
}
