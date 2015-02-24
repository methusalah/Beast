package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;

public class Harvester extends Actuator{

	final Resource resource;

	public Harvester(int serial, Brain brain, Resource resource) {
		super(serial, brain);
		this.resource = resource;
	}
	public Harvester(Harvester other, Brain newBrain) {
		super(other, newBrain);
		this.resource = other.resource;
	}

	@Override
	public void act() {
		brain.beast.harvest(resource, power);
	}
}
