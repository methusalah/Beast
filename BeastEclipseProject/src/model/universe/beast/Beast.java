package model.universe.beast;

import java.awt.Color;

import tools.LogUtil;
import utils.MyRandom;
import geometry.Point2D;
import math.Angle;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.action.Harvester;
import model.universe.beast.neuralnetwork.action.Mover;
import model.universe.beast.neuralnetwork.action.Rotator;
import model.universe.beast.neuralnetwork.perception.NeedSensor;
import model.universe.beast.neuralnetwork.perception.ResourceSensor;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class Beast extends UComp {

	private final Brain brain;
	public final Need need;
	
	private double orientation;
	private double maxSpeed = 2;
	public int age = 0;
	public int gen = 0;
	public Point2D trail;
	
	public Beast(Universe universe, Point2D coord) {
		super(universe, coord);
		need = new Need(universe.resourceSet.getRandomResource());
		brain = new Brain(this);
		orientation = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		trail = coord;
	}
	
	public Beast(Beast parent){
		super(parent.universe, parent.coord);
		gen = parent.gen+1;
		need = new Need(parent.need);
		brain = parent.brain.getMutation(this);
		orientation = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		move(MyRandom.between(1d, 2d));
		trail = coord;
	}
	

	@Override
	public void update() {
		trail = coord;
		brain.stimulate();
		need.deplete();
		if(need.getDepletionRate() <= 0)
			destroy();
		age++;
		if(age==1000||
				age==2000 ||
				age==3000 ||
				age==4000 ||
				age==5000)
			reproduce();
		if(age > 5000)
			destroy();
	}
	
	public void harvest(Resource resource){
		ResourceSpot spot = universe.getResourceSpot(resource, coord);
		if(spot == null)
			return;
		need.fulfill(spot.harvest());
	}
	public void rotate(Double angle){
		orientation += angle;
		Angle.normalize(orientation);
	}

	public void move(Double speedRate){
		Point2D newCoord = universe.getInBounds(coord.getTranslation(orientation, speedRate*maxSpeed));
		Tile previous = universe.getTile(coord); 
		Tile newTile = universe.getTile(newCoord);
		if(previous != newTile){
			previous.unregister(this);
			newTile.register(this);
		}
		coord = newCoord;
	}
	

	@Override
	public Color getColor() {
		return Color.RED;
	}

	@Override
	public int getSize() {
		return 3;
	}
	
	private void reproduce(){
		new Beast(this);
	}
}
