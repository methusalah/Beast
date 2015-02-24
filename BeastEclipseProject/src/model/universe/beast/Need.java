package model.universe.beast;

import java.text.DecimalFormat;

import math.MyRandom;
import model.universe.resource.Resource;

public class Need {
	
	private static final double CAPACITY_MAX = 100;
	private static final double DEPLETION_MAX = 10;

	public final Resource resource;
	final double capacity;
	final double depletion;
	
	double level;
	
	public Need(Resource resource, boolean random) {
		this.resource = resource;
		if(random){
			capacity = MyRandom.between(1, CAPACITY_MAX);
			depletion = MyRandom.between(1, DEPLETION_MAX);
		} else {
			capacity = 100;
			depletion = 1;
		}
			
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
	
	public void deplete(double rate){
		level -= depletion*rate;
	}
	public void deplete(){
		deplete(1);
	}
	
	public void fulfill(double val){
		level += val;
		level = Math.min(level, capacity);
	}
	
	@Override
	public String toString() {
		String ls=System.getProperty("line.separator");
		DecimalFormat ds = new DecimalFormat("0.00");
		String res = this.getClass().getSimpleName()+" description : "+ls;
		res = res.concat("    Capacity : "+ds.format(capacity)+ls);
		res = res.concat("    Depletion : "+ds.format(depletion)+ls);
		res = res.concat(resource.toString());
		return res;
	}
}
