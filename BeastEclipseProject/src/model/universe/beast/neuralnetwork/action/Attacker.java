package model.universe.beast.neuralnetwork.action;

import geometry.Point2D;
import math.Angle;
import math.MyRandom;
import model.universe.Tile;
import model.universe.beast.Beast;
import model.universe.beast.neuralnetwork.Brain;

public class Attacker extends Actuator {
	private static final double MAX_VIEW_ANGLE = Angle.RIGHT;
	private static final int MAX_VIEW_DISTANCE = 2;

	final double distance;
	final double angle;

	public Attacker(int serial, Brain brain) {
		super(serial, brain);
		distance = MyRandom.nextInt(MAX_VIEW_DISTANCE);
		angle = (MAX_VIEW_ANGLE/5)*MyRandom.between(-5, +5);
				
	}
	public Attacker(Attacker other, Brain newBrain) {
		super(other, newBrain);
		this.distance = other.distance;
		this.angle = other.angle;
	}

	@Override
	public void excite() {
		Point2D target = getTarget();
		Beast closest = null;
		double dist = Double.MAX_VALUE;
		for(Tile t : brain.beast.universe.get9Neighbors(target))
			for(Beast b : t.getBeasts()){
				if(b == brain.beast)
					continue;
				double thisDist = b.coord.getDistance(target);
				if(closest == null || thisDist<dist){
					closest = b;
					dist = thisDist;
				}
			}

		if(closest != null)
			brain.beast.attack(closest, power);
			
	}
	
	protected Point2D getTarget(){
		Point2D res = Point2D.ORIGIN;
		if(distance > 0)
			res = brain.beast.universe.getInBounds(brain.beast.coord.getTranslation(angle, distance));
		return res;
	}
	

}
