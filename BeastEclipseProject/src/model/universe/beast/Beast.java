package model.universe.beast;

import geometry.Point2D;

import java.awt.Color;

import math.Angle;
import math.MyRandom;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.Universe;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.beast.neuralnetwork.BrainFactory;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class Beast extends UComp {
	private static double MOVING_MALUS = 10;
	private static double ATTACKING_MALUS = 10;
	

	public final Brain brain;
	public final Need need;
	public final Color color;
	public int gen = 0;
	public int dinoGen = 0;
	private double maxSpeed = 1;

	
	private double orientation;
	private double harvestPower;
	private double movePower;
	private double rotationPower;
	
	public int age = 0;
	public Point2D trail;
	private int life = 100;
	private boolean attacked = false;
	public double biomasse = 0;
	
	int nextReproduction = 0;
	
	
	public Beast(Universe universe, Point2D coord) {
		super(universe, coord);
		need = new Need(universe.resourceSet.getRandomResource(), false);
		BrainFactory brainFactory = new BrainFactory(this);
		brain = brainFactory.getBrain();
		orientation = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		trail = coord;
		setNextReproduction();
		color = new Color(MyRandom.nextInt(255), MyRandom.nextInt(255), MyRandom.nextInt(255));
	}
	
	public Beast(Beast parent, boolean mutate){
		super(parent.universe, parent.coord);
		need = new Need(parent.need);
		BrainFactory brainFactory = new BrainFactory(this, parent.brain, mutate);
		brain = brainFactory.getBrain();
		if(mutate){
			gen = parent.gen+1;
			int r = Math.min(255, Math.max(0, parent.color.getRed()+MyRandom.between(-10, 10)));
			int g = Math.min(255, Math.max(0, parent.color.getGreen()+MyRandom.between(-10, 10)));
			int b = Math.min(255, Math.max(0, parent.color.getBlue()+MyRandom.between(-10, 10)));
			color = new Color(r, g, b);
		} else {
			gen = parent.gen;
			dinoGen = parent.dinoGen+1;
			color = parent.color;
		}
		orientation = MyRandom.between(-Angle.FLAT, Angle.FLAT);
		addMove(MyRandom.between(0.5, 1));
		applyMovement();
		trail = coord;
		setNextReproduction();
		double needPerTurn = 1+(double)brain.getSize()/50;
		need.change(needPerTurn);
	}
	

	@Override
	public void update() {
		if(destroyed)
			return;

		harvestPower = 0;
		rotationPower = 0;
		movePower = 0;
		trail = coord;
		
		brain.stimulate();
		
		if(harvestPower>movePower)
			applyHarvestPower();
		else if(movePower>harvestPower)
			applyMovement();

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
	
	
	
	
	
	public void addHarvestPower(Resource resource, double power){
		harvestPower = Math.min(1, harvestPower+power);
	}
	public void addMove(double power){
		movePower = Math.min(1, movePower+power);
	}
	public void addRotation(double power){
		rotationPower += power-0.5;
		rotationPower = Math.min(0.5, rotationPower);
		rotationPower = Math.max(-0.5, rotationPower);
	}
		
	public void applyHarvestPower(){
		ResourceSpot spot = universe.getResourceSpot(need.resource, coord);
		if(spot == null)
			return;
		double harvested = spot.harvest(harvestPower, need.getActualNeed());
		need.fulfill(harvested);
		biomasse += harvested;
	}

	private void applyMovement(){
		need.deplete(MOVING_MALUS*movePower);

		orientation += Angle.FLAT*rotationPower;
		Angle.normalize(orientation);
		
		Point2D newCoord = universe.getInBounds(coord.getTranslation(orientation, maxSpeed*movePower));
		Tile previous = universe.getTile(coord); 
		Tile newTile = universe.getTile(newCoord);
		if(previous != newTile){
			previous.unregister(this);
			newTile.register(this);
		}
		coord = newCoord;
	}
	
	public void attack(Beast other, double power){
		other.life -= 10;
		other.attacked = true;
		if(other.life <= 0)
			other.die();
		need.deplete(ATTACKING_MALUS*power);
	}

	
	
	
	
	
	
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getSize() {
		return brain.getSize();
	}
	
	private void creatChild(){
		new Beast(this, true);
	}
	private void creatClone(){
		new Beast(this, false);
	}
	
	private void setNextReproduction(){
		nextReproduction += MyRandom.between(1500, 2500);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	public double getRacialDiff(Beast other){
		double res = 0;
		res += Math.abs(color.getRed()-other.color.getRed());
		res += Math.abs(color.getGreen()-other.color.getGreen());
		res += Math.abs(color.getBlue()-other.color.getBlue());
		res /= 765;
		return res;
	}
	
	public boolean grabAttackedState(){
		boolean res = attacked;
		attacked = false;
		return res;
	}
}
