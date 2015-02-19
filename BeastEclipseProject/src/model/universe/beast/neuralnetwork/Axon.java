package model.universe.beast.neuralnetwork;

public class Axon {

	final double polarizationValue;
	Neuron postSynaptic;
	int serial;
	
	public Axon(double polarizationValue, Neuron postSynaptic) {
		this.polarizationValue = polarizationValue;
		this.postSynaptic = postSynaptic;
	}
	public Axon(Axon other) {
		this.polarizationValue = other.polarizationValue;
		this.serial = other.getPostSynapticSerial();
	}
	
	public void activate(){
		postSynaptic.polarize(polarizationValue);
	}
	
	public int getPostSynapticSerial(){
		return postSynaptic.serial;
	}
}
