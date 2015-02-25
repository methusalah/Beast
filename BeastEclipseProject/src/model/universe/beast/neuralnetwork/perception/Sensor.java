package model.universe.beast.neuralnetwork.perception;

import tools.LogUtil;
import model.universe.beast.neuralnetwork.Axon;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.Neuron;

public abstract class Sensor extends Neuron{
	
	final double polarisationValue;
	
	public Sensor(int serial, Brain brain) {
		super(serial, brain);
		polarisationValue = Axon.getRandomPolarisationValue();
	}
	
	public Sensor(Sensor other, Brain newBrain){
		super(other, newBrain);
		polarisationValue = other.polarisationValue;
	}

	public abstract double getStimulationRate();
	
	public void stimulate(){
		polarize(getStimulationRate()*polarisationValue);
	}
}
