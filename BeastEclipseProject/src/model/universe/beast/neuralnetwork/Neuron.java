package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import utils.MyRandom;

public class Neuron {
	protected static final double THRESOLD_MAX = 100;
	private static final double POLARIZATION_MIN = -50; 
	private static final double POLARIZATION_MAX = 50; 
	

	final List<Axon> axons = new ArrayList<>();
	final double thresold;
	
	double polarisation = 0;
	
	public Neuron() {
		thresold = MyRandom.between(0, THRESOLD_MAX);
	}
	
	public void polarize(double value){
		polarisation += value;
		if(polarisation > thresold){
			excite();
			calm();
		}
	}
	
	public void launchAxonOn(Neuron other){
		axons.add(new Axon(MyRandom.between(POLARIZATION_MIN, POLARIZATION_MAX), other));
	}
	
	public void calm(){
		polarisation = 0;
	}
	
	public void excite(){
		for(Axon a : axons)
			a.activate();
	}
}
