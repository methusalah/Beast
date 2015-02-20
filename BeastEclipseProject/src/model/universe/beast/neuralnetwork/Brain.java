package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import tools.LogUtil;
import utils.MyRandom;
import math.Angle;
import model.universe.Universe;
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
	final List<Sensor> sensors = new ArrayList<>();
	final List<Neuron> neurons = new ArrayList<>();
	final List<Actuator> actuators = new ArrayList<>();
	
	List<Neuron> preSynaptics = new ArrayList<>();
	List<Neuron> postSynaptics = new ArrayList<>();
	List<Neuron> all = new ArrayList<>();
	
	int serial=0;
	
	
	public Brain(Beast beast){
		this.beast = beast; 
		addActuator(new Rotator(serial++, this, Angle.toRadians(10)));
		addActuator(new Rotator(serial++, this, Angle.toRadians(-10)));
		addActuator(new Rotator(serial++, this, Angle.toRadians(30)));
		addActuator(new Rotator(serial++, this, Angle.toRadians(-30)));
		addActuator(new Mover(serial++, this, 1));
		addNeed(beast.need);
		classifyNeurons();
		createRandomConnexions();
	}

	private void addNeed(Need need){
		addSensor(new NeedSensor(serial++, this));
		addSensor(new ResourceSensor(serial++, this, need.resource));
		addActuator(new Harvester(serial++, this, need.resource));
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
		classifyNeurons();
		for(Neuron n : serializedNeurons.values())
			for(Axon a : n.axons){
				Neuron postSynaptic = serializedNeurons.get(a.getPostSynapticSerial()); 
				a.postSynaptic = postSynaptic;
				postSynaptic.preSynaptics.add(n);
			}
	}
	
	private void classifyNeurons(){
		preSynaptics.addAll(sensors);
		preSynaptics.addAll(neurons);
		postSynaptics.addAll(neurons);
		postSynaptics.addAll(actuators);
		all.addAll(sensors);
		all.addAll(neurons);
		all.addAll(actuators);
	}
	
	public void stimulate(){
		for(Sensor s : sensors)
			s.stimulate();
		List<Neuron> all = new ArrayList<>();
		all.addAll(sensors);
		all.addAll(neurons);
		all.addAll(actuators);
		for(Neuron n : all)
			n.calm();
	}
	
	private void addSensor(Sensor s){
		sensors.add(s);
	}
	private void addActuator(Actuator a){
		actuators.add(a);
	}
	
	private void createRandomConnexions(){
		for(Neuron pre : preSynaptics){
			int axonCount = MyRandom.between(1, 2);
			for(int i=0; i<axonCount; i++){
				int index = MyRandom.nextInt(postSynaptics.size());
				pre.launchAxonOn(postSynaptics.get(index));
			}
		}
	}
	
	private void mutateNeuron(){
		all.get(MyRandom.nextInt(all.size())).setRandomThresold();
	}
	private void deleteNeuron(){
		if(!neurons.isEmpty()){
			int i = MyRandom.nextInt(neurons.size());
			Neuron n = neurons.get(i);
			neurons.remove(i);
			for(Neuron pre : n.preSynaptics)
				pre.retireAxonOn(n);
		}
	}
	private void addNeuron(){
		Neuron n = new Neuron(serial++);
		neurons.add(n);
		preSynaptics.get(MyRandom.nextInt(preSynaptics.size())).launchAxonOn(n);
		int axonCount = MyRandom.between(1, 2);
		for(int i=0; i<axonCount; i++){
			int index = MyRandom.nextInt(postSynaptics.size());
			n.launchAxonOn(postSynaptics.get(index));
		}
	}
	private void mutateAxon(){
		List<Axon> axons = preSynaptics.get(MyRandom.nextInt(preSynaptics.size())).axons; 
		Axon a = axons.get(MyRandom.nextInt(axons.size()));
		a.setRandomPolarisationValue();
	}
	private void deleteAxon(){
		List<Axon> axons = preSynaptics.get(MyRandom.nextInt(preSynaptics.size())).axons; 
		axons.remove(MyRandom.nextInt(axons.size()));
	}
	private void addAxon(){
		preSynaptics.get(MyRandom.nextInt(preSynaptics.size())).launchAxonOn(postSynaptics.get(MyRandom.nextInt(postSynaptics.size())));
	}
	
	
	
	public Brain getMutation(Beast beast){
		Brain res = new Brain(this, beast);
		int mutation = MyRandom.nextInt(6);
		switch(mutation){
		case 0: res.mutateNeuron(); break;
		case 1: res.deleteNeuron(); break;
		case 2: res.addNeuron(); break;
		case 3: res.mutateAxon(); break;
		case 4: res.deleteAxon(); break;
		case 5: res.addAxon(); break;
		default: throw new RuntimeException();
		}
		
		classifyNeurons();
		return res;
	}
}
