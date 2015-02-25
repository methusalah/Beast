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
	
	List<Neuron> preSynaptics = new ArrayList<>();
	List<Neuron> postSynaptics = new ArrayList<>();
	List<Neuron> all = new ArrayList<>();
	
	public List<Neuron> polarized = new ArrayList<>();
	int serial=0;
	
	
	public Brain(Beast beast){
		this.beast = beast; 
		addActuator(new Rotator(serial++, this));
		addActuator(new Mover(serial++, this));
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
		if(n instanceof Actuator)
			((Actuator)n).setRandomPower();
		else
			n.setRandomThresold();
	}
	private void deleteNeuron(){
		int i = MyRandom.nextInt(all.size());
		Neuron n = all.get(i);
		if(n instanceof Sensor)
			sensors.remove(n);
		else if(n instanceof Actuator)
			actuators.remove(n);
		else
			neurons.remove(n);
		for(Neuron pre : n.preSynaptics)
			pre.retireAxonOn(n);
	}
	private void addNeuron(){
		Neuron added;
		switch (MyRandom.between(0, 3)){
		case 0:
			added = getRandomSensor();
			sensors.add((Sensor)added);
			break;
		case 1:
			added = getRandomActuator();
			actuators.add((Actuator)added);
			break;
		case 2:
			added = new Neuron(serial++, this);
			neurons.add(added);
			break;
		default : throw new RuntimeException();
		}
		preSynaptics.get(MyRandom.nextInt(preSynaptics.size())).launchAxonOn(added);
		if(!(added instanceof Actuator)){
			int axonCount = MyRandom.between(1, 2);
			for(int i=0; i<axonCount; i++){
				int index = MyRandom.nextInt(postSynaptics.size());
				added.launchAxonOn(postSynaptics.get(index));
			}
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
	
	public Sensor getRandomSensor(){
		switch (MyRandom.between(0, 2)){
		case 0: return new NeedSensor(serial++, this);
		case 1: return new ResourceSensor(serial++, this, beast.need.resource);
		default : throw new RuntimeException();
		}
	}
	public Actuator getRandomActuator(){
		switch (MyRandom.between(0, 3)){
		case 0: return new Harvester(serial++, this, beast.need.resource);
		case 1: return new Mover(serial++, this);
		case 2: return new Rotator(serial++, this);
		default : throw new RuntimeException();
		}
	}
	
	public Brain getMutation(Beast newBeast){
		Brain res = new Brain(this, newBeast);
		res.mutateRandomly();
		res.classifyNeurons();
		return res;
	}
	public Brain getIdentical(Beast newBeast){
		Brain res = new Brain(this, newBeast);
		return res;
	}
	
	public int getSize(){
		return all.size();
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
