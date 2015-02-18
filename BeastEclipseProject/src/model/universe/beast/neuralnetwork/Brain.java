package model.universe.beast.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import utils.MyRandom;
import model.universe.beast.neuralnetwork.action.Actuator;
import model.universe.beast.neuralnetwork.perception.Sensor;

public class Brain {

	final List<Sensor> sensors = new ArrayList<>();
	final List<Neuron> neurons = new ArrayList<>();
	final List<Actuator> actuators = new ArrayList<>();
	
	public Brain(){
		
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
	
	public void addNeuron(Sensor s){
		sensors.add(s);
	}
	public void addNeuron(Actuator a){
		actuators.add(a);
	}
	public void addNeuron(Neuron n){
		neurons.add(n);
	}
	
	public void createRandomConnexions(){
		for(int i=0; i<MyRandom.nextInt(5); i++)
			addNeuron(new Neuron());
		
		List<Neuron> from = new ArrayList<>();
		from.addAll(sensors);
		from.addAll(neurons);
		List<Neuron> to = new ArrayList<>();
		to.addAll(neurons);
		to.addAll(actuators);
		
		for(Neuron pre : from){
			int axonCount = MyRandom.between(1, 2);
			for(int i=0; i<axonCount; i++){
				int index = MyRandom.nextInt(to.size());
				pre.launchAxonOn(to.get(index));
			}
		}
	}
	
}
