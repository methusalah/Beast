package model.universe.beast.neuralnetwork.action;

import model.universe.beast.Beast;
import model.universe.resource.Resource;

public class Harvester extends Actuator{

	final Resource resource;

	public Harvester(Beast beast, Resource resource) {
		super(beast);
		this.resource = resource;
	}

	@Override
	public void excite() {
		super.excite();
		beast.harvest(resource);
	}
	
}
