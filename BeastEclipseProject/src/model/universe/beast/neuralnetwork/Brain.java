package model.universe.beast.neuralnetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.MyRandom;
import model.universe.beast.Beast;
import model.universe.beast.Need;
import model.universe.beast.neuralnetwork.action.Actuator;
import model.universe.beast.neuralnetwork.action.Harvester;
import model.universe.beast.neuralnetwork.action.Mover;
import model.universe.beast.neuralnetwork.action.Rotator;
import model.universe.beast.neuralnetwork.perception.NeedSensor;
import model.universe.beast.neuralnetwork.perception.ResourceSensor;
import model.universe.beast.neuralnetwork.perception.Sensor;

public class Brain {

	public final Beast beast;
	public final List<Sensor> sensors = new ArrayList<>();
	public final List<Neuron> neurons = new ArrayList<>();
	public final List<Actuator> actuators = new ArrayList<>();
	
	public List<Neuron> polarized = new ArrayList<>();
	int serial=0;
	
	
	public Brain(Beast beast){
		this.beast = beast; 
	}

	public Brain(Brain other, Beast newBeast){
		beast = newBeast;
		serial = other.serial;
		HashMap<Integer, Neuron> serializedNeurons = new HashMap<>();
		for(Sensor s : other.sensors){
			Sensor copy = (Sensor) NeuronFactory.getCopy(s, this);
			if(serializedNeurons.put(copy.serial, copy) != null)
				throw new RuntimeException("the serial is used twice "+copy.serial);
			sensors.add(copy);
		}
		for(Neuron n : other.neurons){
			Neuron copy = NeuronFactory.getCopy(n, this);
			if(serializedNeurons.put(copy.serial, copy) != null)
				throw new RuntimeException("the serial is used twice "+copy.serial);
			neurons.add(copy);
		}
		for(Actuator a : other.actuators){
			Actuator copy = (Actuator) NeuronFactory.getCopy(a, this);
			if(serializedNeurons.put(copy.serial, copy) != null)
				throw new RuntimeException("the serial is used twice "+copy.serial);
			actuators.add(copy);
		}
		
		for(Neuron n : serializedNeurons.values())
			for(Axon a : n.axons){
				Neuron postSynaptic = serializedNeurons.get(a.getPostSynapticSerial()); 
				a.postSynaptic = postSynaptic;
				postSynaptic.preSynaptics.add(n);
			}
	}
	
	public void stimulate(){
		for(Sensor s : sensors)
			s.stimulate();
		while (!polarized.isEmpty()) {
			List<Neuron> polarizedThisTurn = new ArrayList<>();
			polarizedThisTurn.addAll(polarized);
			polarized.clear();
			for(Neuron n : polarizedThisTurn)
				n.finalizePolarisation();
		}
		
		for(Neuron n : sensors)
			n.calm();
		for(Neuron n : neurons)
			n.calm();
		for(Neuron n : actuators)
			n.calm();
	}
	
	
	public int getSize(){
		return neurons.size()+sensors.size()+actuators.size();
	}
	
	@Override
	public String toString() {
		String ls = System.lineSeparator();
		DecimalFormat df = new DecimalFormat("0");
		String res = this.getClass().getSimpleName()+" Decription :"+ls;
		res = res.concat("Sensors        Neurons        Actuators"+ls);
		for(Sensor s : sensors)
			res = res.concat(""+s.getClass().getSimpleName()+"("+df.format(s.thresold)+")"+ls);
		for(Neuron n : neurons)
			res = res.concat("               "+n.getClass().getSimpleName()+"("+df.format(n.thresold)+")"+ls);
		for(Actuator a : actuators)
			res = res.concat("                              "+a.getClass().getSimpleName()+"("+df.format(a.thresold)+")"+ls);
			
		return res ;
	}
}
