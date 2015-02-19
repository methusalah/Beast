package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import utils.MyRandom;

public class Neuron {
	protected static final double THRESOLD_MAX = 100;
	private static final double POLARIZATION_MIN = -50; 
	private static final double POLARIZATION_MAX = 50; 
	

	final List<Axon> axons = new ArrayList<>();
	final int serial;
	final double thresold;
	
	boolean excitedThisTurn = false;
	double polarisation = 0;
	
	public Neuron(int serial) {
		this.serial = serial;
		thresold = MyRandom.between(0, THRESOLD_MAX);
	}

	public Neuron(Neuron other) {
		this.serial = other.serial;
		thresold = other.thresold;
		for(Axon a : other.axons)
			axons.add(new Axon(a));
	}
	
	public void polarize(double value){
		polarisation += value;
		if(polarisation > thresold){
			excite();
		}
	}
	
	public void launchAxonOn(Neuron other){
		axons.add(new Axon(MyRandom.between(POLARIZATION_MIN, POLARIZATION_MAX), other));
	}
	
	public List<Integer> getPostSynapticsSerial(){
		List<Integer> res = new ArrayList<>();
		for(Axon a : axons)
			res.add(a.getPostSynapticSerial());
		return res;
	}
	
	public void calm(){
		polarisation = 0;
		excitedThisTurn = false;
	}
	
	public void excite(){
		if(!excitedThisTurn){
			excitedThisTurn = true;
			for(Axon a : axons)
				a.activate();
		}
	}
}
