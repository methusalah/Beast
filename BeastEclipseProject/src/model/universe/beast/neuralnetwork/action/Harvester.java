package model.universe.beast.neuralnetwork.action;

import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;

public class Harvester extends Actuator{

	final Resource resource;

	public Harvester(int serial, Brain brain, Resource resource) {
		super(serial, brain);
		this.resource = resource;
	}

	@Override
	public void excite() {
		super.excite();
		brain.beast.harvest(resource);
	}
	
}
