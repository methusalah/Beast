package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;

public class Neuron {
	protected static final double THRESOLD_MAX = 100;
	

	final List<Axon> axons = new ArrayList<>();
	final List<Neuron> preSynaptics = new ArrayList<>();
	final public int serial;
	double thresold;
	
	protected boolean excitedThisTurn = false;
	double polarisation = 0;
	
	public Neuron(int serial) {
		this.serial = serial;
		setRandomThresold();
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
		axons.add(new Axon(other));
		other.preSynaptics.add(this);
	}
	
	public void retireAxonOn(Neuron other){
		Axon toRetire = null;
		for(Axon a : axons)
			if(a.postSynaptic == other){
				toRetire = a;
				break;
			}
		axons.remove(toRetire);
		
	}
	
	public List<Integer> getPostSynapticsSerial(){
		List<Integer> res = new ArrayList<>();
		for(Axon a : axons)
			res.add(a.getPostSynapticSerial());
		return res;
	}
	
	public boolean excited(){
		return excitedThisTurn;
	}
	
	protected void calm(){
		polarisation = 0;
		excitedThisTurn = false;
	}
	
	protected void excite(){
		if(!excitedThisTurn){
			excitedThisTurn = true;
			for(Axon a : axons)
				a.activate();
		}
	}
	
	public void setRandomThresold(){
		thresold = MyRandom.between(0, THRESOLD_MAX);		
	}
}
