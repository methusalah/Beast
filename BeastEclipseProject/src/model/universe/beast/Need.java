package model.universe.beast;

import math.MyRandom;
import model.universe.resource.Resource;

public class Need {
	
	private static final double CAPACITY_MAX = 100;
	private static final double DEPLETION_MAX = 10;

	public final Resource resource;
	final double capacity;
	final double depletion;
	
	double level;
	
	public Need(Resource resource) {
		this.resource = resource;
		capacity = MyRandom.between(1, CAPACITY_MAX);
		depletion = MyRandom.between(1, DEPLETION_MAX);
		level = capacity;
	}
	
	public Need(Need other){
		this.resource = other.resource;
		capacity = other.capacity;
		depletion = other.depletion;
		level = capacity;
	}
	
	
	public double getDepletionRate(){
		return level/capacity;
	}
	
	public void deplete(){
		level -= depletion;
	}
	
	public void fulfill(double val){
		level += val;
		level = Math.min(level, capacity);
	}
}
