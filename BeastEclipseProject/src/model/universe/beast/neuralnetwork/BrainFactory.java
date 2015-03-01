package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;
import model.universe.beast.Beast;
import model.universe.beast.Need;
import model.universe.beast.neuralnetwork.action.Actuator;
import model.universe.beast.neuralnetwork.action.Attacker;
import model.universe.beast.neuralnetwork.action.Harvester;
import model.universe.beast.neuralnetwork.action.Mover;
import model.universe.beast.neuralnetwork.action.Rotator;
import model.universe.beast.neuralnetwork.perception.AttackSensor;
import model.universe.beast.neuralnetwork.perception.BeastSensor;
import model.universe.beast.neuralnetwork.perception.NeedSensor;
import model.universe.beast.neuralnetwork.perception.ResourceSensor;
import model.universe.beast.neuralnetwork.perception.Sensor;

public class BrainFactory {
	private final Brain brain;
	List<Neuron> preSynaptics = new ArrayList<>();
	List<Neuron> postSynaptics = new ArrayList<>();
	List<Neuron> all = new ArrayList<>();
	
	public BrainFactory(Beast beast){
		brain = new Brain(beast);
		addActuator(new Rotator(brain.serial++, brain));
		addActuator(new Mover(brain.serial++, brain));
		
		addNeed(beast.need);
		
		classifyNeurons();
		createRandomConnexions();
	}
	

	
	public BrainFactory(Beast beast, Brain modelBrain, boolean mutate) {
		this.brain = new Brain(modelBrain, beast);
		classifyNeurons();
		if(mutate)
			mutateRandomly();
	}
	
	private void classifyNeurons(){
		preSynaptics.addAll(brain.sensors);
		preSynaptics.addAll(brain.neurons);
		postSynaptics.addAll(brain.neurons);
		postSynaptics.addAll(brain.actuators);
		all.addAll(brain.sensors);
		all.addAll(brain.neurons);
		all.addAll(brain.actuators);
	}

	private void addNeed(Need need){
		addSensor(new NeedSensor(brain.serial++, brain));
		addSensor(new ResourceSensor(brain.serial++, brain, need.resource));
		addActuator(new Harvester(brain.serial++, brain, need.resource));
	}
	
	private void addSensor(Sensor s){
		brain.sensors.add(s);
	}
	private void addActuator(Actuator a){
		brain.actuators.add(a);
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

	private void mutateNeuron(){
		Neuron n = all.get(MyRandom.nextInt(all.size()));
		if(n instanceof Actuator && MyRandom.next() < 0.5)
			((Actuator)n).setRandomPower();
		else
			n.setRandomThresold();
	}
	
	private void deleteNeuron(){
		int i = MyRandom.nextInt(all.size());
		Neuron n = all.get(i);
		if(n instanceof Sensor)
			brain.sensors.remove(n);
		else if(n instanceof Actuator)
			brain.actuators.remove(n);
		else
			brain.neurons.remove(n);
		for(Neuron pre : n.preSynaptics)
			pre.retireAxonOn(n);
	}
	
	private void addNeuron(){
		Neuron added;
		switch (MyRandom.between(0, 3)){
		case 0:
			added = getRandomSensor();
			brain.sensors.add((Sensor)added);
			break;
		case 1:
			added = getRandomActuator();
			brain.actuators.add((Actuator)added);
			break;
		case 2:
			added = new Neuron(brain.serial++, brain);
			brain.neurons.add(added);
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
	
	public Sensor getRandomSensor(){
		switch (MyRandom.between(0, 3)){
		case 0: return new NeedSensor(brain.serial++, brain);
		case 1: return new ResourceSensor(brain.serial++, brain, brain.beast.need.resource);
		case 2: return new BeastSensor(brain.serial++, brain);
		case 3: return new AttackSensor(brain.serial++, brain);
		default : throw new RuntimeException();
		}
	}
	public Actuator getRandomActuator(){
		switch (MyRandom.between(0, 3)){
		case 0: return new Harvester(brain.serial++, brain, brain.beast.need.resource);
		case 1: return new Mover(brain.serial++, brain);
		case 2: return new Rotator(brain.serial++, brain);
		case 3: return new Attacker(brain.serial++, brain);
		default : throw new RuntimeException();
		}
	}
	
	public Brain getBrain(){
		return brain;
	}

}
