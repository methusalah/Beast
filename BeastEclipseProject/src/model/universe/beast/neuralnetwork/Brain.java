package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tools.LogUtil;
import math.Angle;
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
	final List<Sensor> sensors = new ArrayList<>();
	final List<Neuron> neurons = new ArrayList<>();
	final List<Actuator> actuators = new ArrayList<>();
	
	List<Neuron> preSynaptics = new ArrayList<>();
	List<Neuron> postSynaptics = new ArrayList<>();
	List<Neuron> all = new ArrayList<>();
	
	public List<Neuron> polarized = new ArrayList<>();
	int serial=0;
	
	
	public Brain(Beast beast){
		this.beast = beast; 
		addActuator(new Rotator(serial++, this));
		addActuator(new Rotator(serial++, this));
		addActuator(new Rotator(serial++, this));
		addActuator(new Rotator(serial++, this));
		addActuator(new Mover(serial++, this));
		addActuator(new Mover(serial++, this));
		addActuator(new Mover(serial++, this));
		addActuator(new Mover(serial++, this));
		addNeed(beast.need);
		classifyNeurons();
		createRandomConnexions();
	}

	private void addNeed(Need need){
		addSensor(new NeedSensor(serial++, this));
		addSensor(new ResourceSensor(serial++, this, need.resource, 0, 0));
		addSensor(new ResourceSensor(serial++, this, need.resource, 2, Angle.toRadians(10)));
		addSensor(new ResourceSensor(serial++, this, need.resource, 2, Angle.toRadians(-10)));
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
		while (!polarized.isEmpty()) {
			List<Neuron> polarizedThisTurn = new ArrayList<>();
			polarizedThisTurn.addAll(polarized);
			polarized.clear();
			for(Neuron n : polarizedThisTurn)
				n.finalizePolarisation();
		}
		
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
		Neuron n = all.get(MyRandom.nextInt(all.size()));
		if(n instanceof Actuator && MyRandom.next()<0.5)
			((Actuator)n).setRandomPower();
		else
			n.setRandomThresold();
	}
	private void deleteNeuron(){
		if(neurons.isEmpty()){
			mutateRandomly();
			return;
		}
		int i = MyRandom.nextInt(neurons.size());
		Neuron n = neurons.get(i);
		neurons.remove(i);
		for(Neuron pre : n.preSynaptics)
			pre.retireAxonOn(n);
	}
	private void addNeuron(){
		Neuron n = new Neuron(serial++, this);
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
		if(axons.isEmpty()){
			mutateRandomly();
			return;
		}
		Axon a = axons.get(MyRandom.nextInt(axons.size()));
		a.setRandomPolarisationValue();
	}
	private void deleteAxon(){
		Neuron n = preSynaptics.get(MyRandom.nextInt(preSynaptics.size()));
		List<Axon> axons = n.axons;
		if(axons.isEmpty()){
			mutateRandomly();
			return;
		}
		axons.remove(MyRandom.nextInt(axons.size()));
	}
	private void addAxon(){
		preSynaptics.get(MyRandom.nextInt(preSynaptics.size())).launchAxonOn(postSynaptics.get(MyRandom.nextInt(postSynaptics.size())));
	}
	
	private void mutateRandomly(){
		int mutation = MyRandom.nextInt(6);
		switch(mutation){
		case 0: mutateNeuron(); break;
		case 1: deleteNeuron(); break;
		case 2: addNeuron(); break;
		case 3: mutateAxon(); break;
		case 4: deleteAxon(); break;
		case 5: addAxon(); break;
		default: throw new RuntimeException();
		}
	}
	
	public Brain getMutation(Beast newBeast){
		Brain res = new Brain(this, newBeast);
		res.mutateRandomly();
		classifyNeurons();
		return res;
	}
	public Brain getIdentical(Beast newBeast){
		Brain res = new Brain(this, newBeast);
		return res;
	}
	
	public int getSize(){
		return neurons.size();
	}
}
