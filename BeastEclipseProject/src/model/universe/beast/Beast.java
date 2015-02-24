package model.universe.beast;

import geometry.Point2D;

import java.awt.Color;

import math.Angle;
import math.MyRandom;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class Beast extends UComp {

	private final Brain brain;
	public final Need need;
	
	private double orientation;
	private double maxSpeed = 2;
	public int age = 0;
	public int gen = 0;
	public int dinoGen = 0;
	public Point2D trail;
	
	int nextReproduction = 0;
	
	
	public Beast(Universe universe, Point2D coord) {
		super(universe, coord);
		need = new Need(universe.resourceSet.getRandomResource(), false);
		brain = new Brain(this);
		orientation = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		trail = coord;
		setNextReproduction();
	}
	
	public Beast(Beast parent, boolean mutate){
		super(parent.universe, parent.coord);
		need = new Need(parent.need);
		if(mutate){
			brain = parent.brain.getMutation(this);
			gen = parent.gen+1;
		} else {
			brain = parent.brain.getIdentical(this);
			gen = parent.gen;
			dinoGen = parent.dinoGen+1;
		}
		orientation = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		move(MyRandom.between(1d, 2d));
		trail = coord;
		setNextReproduction();
	}
	

	@Override
	public void update() {
		trail = coord;
		brain.stimulate();
		need.deplete();
		if(need.getDepletionRate() <= 0 ||
				age > 5000)
			die();
		
		age++;
		if(age == nextReproduction){
			creatChild();
			setNextReproduction();
		}
		if(age == 5000)
			creatClone();
	}
	
	private void die(){
		destroy();
		universe.manageCorpse(this);
	}
	
	public void harvest(Resource resource, double power){
		ResourceSpot spot = universe.getResourceSpot(resource, coord);
		if(spot == null)
			return;
		need.fulfill(spot.harvest(power));
	}
	public void rotate(double power){
		power -= 0.5;
		orientation += Angle.FLAT*power;
		Angle.normalize(orientation);
	}

	public void move(double power){
		Point2D newCoord = universe.getInBounds(coord.getTranslation(orientation, maxSpeed*power));
		need.deplete(5*power);
		
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
		int red = (int)(255*need.getDepletionRate());
		return new Color(red, 0, 0);
	}

	@Override
	public int getSize() {
		return 3;
	}
	
	private void creatChild(){
		new Beast(this, true);
	}
	private void creatClone(){
		new Beast(this, false);
	}
	
	private void setNextReproduction(){
		nextReproduction += MyRandom.between(900, 1000);
	}
}
