package model.universe.beast;

import tools.LogUtil;
import geometry.Point2D;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.action.Harvester;
import model.universe.beast.neuralnetwork.perception.NeedSensor;
import model.universe.beast.neuralnetwork.perception.ResourceSensor;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class Beast extends UComp {

	private final Brain brain;
	private final Need need;
	
	
	public Beast(Universe universe, Point2D coord) {
		super(universe, coord);
		need = new Need(universe.resourceSet.getRandomResource());
		brain = new Brain();
		brain.addNeuron(new NeedSensor(need));
		brain.addNeuron(new ResourceSensor(universe, this, need.resource));
		brain.addNeuron(new Harvester(this, need.resource));
		brain.createRandomConnexions();
	}

	@Override
	public void update() {
		brain.stimulate();
		need.deplete();
		if(need.getDepletionRate() <= 0)
			destroy();
	}
	
	public void harvest(Resource resource){
		ResourceSpot spot = universe.getResourceSpot(resource, coord);
		if(spot == null)
			return;
		need.fulfill(spot.harvest());
	}

}
