package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import tools.LogUtil;
import math.MyRandom;

public class Neuron {
	protected static final double THRESOLD_MAX = 100;
	

	protected final Brain brain;
	final List<Axon> axons = new ArrayList<>();
	final List<Neuron> preSynaptics = new ArrayList<>();
	final public int serial;
	double thresold;
	
	double polarisation = Double.NaN;
	
	public Neuron(int serial, Brain brain) {
		this.serial = serial;
		this.brain = brain;
		setRandomThresold();
	}

	public Neuron(Neuron other, Brain newBrain) {
		this.serial = other.serial;
		this.brain = newBrain;
		thresold = other.thresold;
		for(Axon a : other.axons)
			axons.add(new Axon(a));
	}
	
	public void polarize(double value){
		if(Double.isNaN(polarisation)){
			brain.polarized.add(this);
			polarisation = 0;
		}
		polarisation += value;
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
	
	protected void calm(){
		polarisation = Double.NaN;
	}
	
	protected void finalizePolarisation(){
		if(polarisation > thresold)
			excite();
	}
	
	public void excite(){
		for(Axon a : axons)
			a.activate();
	}
	
	public void setRandomThresold(){
		thresold = MyRandom.between(0, THRESOLD_MAX);		
	}
}
