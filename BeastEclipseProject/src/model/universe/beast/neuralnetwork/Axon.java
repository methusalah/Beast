package model.universe.beast.neuralnetwork;

public class Axon {

	final double polarizationValue;
	final Neuron postSynaptic;
	
	public Axon(double polarizationValue, Neuron postSynaptic) {
		this.polarizationValue = polarizationValue;
		this.postSynaptic = postSynaptic;
	}
	
	public void activate(){
		postSynaptic.polarize(polarizationValue);
	}
}
