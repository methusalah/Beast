package model.universe.beast.neuralnetwork.action;

import model.universe.beast.Beast;
import model.universe.beast.neuralnetwork.Neuron;

public class Actuator extends Neuron{
	
	final Beast beast;
	
	public Actuator(Beast beast) {
		this.beast = beast;
	}

}
