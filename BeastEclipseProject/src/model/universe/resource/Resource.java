package model.universe.resource;

import java.awt.Color;
import java.text.DecimalFormat;

import math.MyRandom;

public class Resource {

	private static double Q_START_MAX = 1000;
	private static double Q_MAX_MAX = 1000;
	private static double Q_GROWTH_MIN = -50;
	private static double Q_GROWTH_MAX = +50;
	private static double Q_HARVEST_MAX = 100;
	private static double CAN_EXPAND_PROB = 0.1;
	private static double EXPAND_PROB_MAX = 0.1;
	private static double SPONTANEOUS_PROB = 0.5;
	private static double SPONTANEOUS_ON_CORPSE_PROB = 0.1;
	
	public final ResourceSet set;
	public final int id;
	public final Color color;
	public final double qStart;
	public final double qMax;
	public final double qGrowth;
	public final double qHarvest;
	public final boolean canExpand;
	public final double expandProb;
	public final boolean spontaneous;
	public final boolean spontaneousOnCorpse;
	
	public int spotCount = 0;
	
	public Resource(ResourceSet set, ResourceIDManager manager){
		this.set = set;
		id = manager.giveNewID();
		color = new Color(MyRandom.between(100, 255),
				MyRandom.between(100, 255),
				MyRandom.between(100, 255));
		qMax = MyRandom.between(0, Q_MAX_MAX);
		qStart = Math.min(MyRandom.between(0, Q_START_MAX), qMax);
		
		qGrowth = MyRandom.between(Q_GROWTH_MIN, Q_GROWTH_MAX);
		qHarvest = MyRandom.between(0, Q_HARVEST_MAX);
		canExpand = MyRandom.next()<CAN_EXPAND_PROB;
		expandProb = MyRandom.between(0, EXPAND_PROB_MAX);

		spontaneous = MyRandom.next()<SPONTANEOUS_PROB;
		spontaneousOnCorpse = MyRandom.next()<SPONTANEOUS_ON_CORPSE_PROB;
	}
	
	public Resource(ResourceSet set, ResourceIDManager manager,
			Color color,
			double qMax,
			double qStart,
			double qGrowth,
			double qHarvest,
			boolean canExpand,
			boolean spontaneousOnCorpse){
		this.set = set;
		id = manager.giveNewID();
		this.color = color;
		this.qMax = qMax;
		this.qStart = qStart;
		this.qGrowth = qGrowth;
		this.qHarvest = qHarvest;
		this.canExpand = canExpand;
		
		expandProb = EXPAND_PROB_MAX;
		spontaneous = MyRandom.next()<SPONTANEOUS_PROB;

		this.spontaneousOnCorpse = spontaneousOnCorpse;
	}
	
	public void registerSpot(){
		spotCount++;
	}
	public void unregisterSpot(){
		spotCount--;
		if(spotCount == 0 && !spontaneousOnCorpse)
			set.deleteResource(this);
	}
	
	@Override
	public String toString() {
		String ls=System.getProperty("line.separator"); 
		DecimalFormat ds = new DecimalFormat("0.00");
		
		String res = this.getClass().getSimpleName()+" description : "+ls;
		res = res.concat("    start/growth/max : "+ds.format(qStart)+"/"+ds.format(qGrowth)+" per turn/"+ds.format(qMax)+ls);
		res = res.concat("    max harvest : "+ds.format(qHarvest)+ls);
		if(canExpand)
			res = res.concat("    expand chance : "+ds.format(expandProb)+"%");
		return res;
	}
	
}
