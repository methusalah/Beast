package model.universe.resource;

import java.awt.Color;

import utils.MyRandom;

public class Resource {

	private static double Q_START_MAX = 1000;
	private static double Q_MAX_MAX = 1000;
	private static double Q_GROWTH_MIN = -50;
	private static double Q_GROWTH_MAX = +50;
	private static double Q_HARVEST_MAX = 100;
	private static double CAN_DISAPEAR_PROB = 0.5;
	private static double CAN_EXPAND_PROB = 0.1;
	private static double EXPAND_PROB_MAX = 0.001;
	private static double SPONTANEOUS_PROB = 0.5;
	private static double SPONTANEOUS_ON_CORPSE_PROB = 0.1;
	
	public final int id;
	public final Color color;
	public final double qStart;
	public final double qMax;
	public final double qGrowth;
	public final double qHarvest;
	public final boolean canDisapear;
	public final boolean canExpand;
	public final double expandProb;
	public final boolean spontaneous;
	public final boolean spontaneousOnCorpse;
	
	public Resource(ResourceIDManager manager){
		id = manager.giveNewID();
		color = new Color(MyRandom.between(100, 255),
				MyRandom.between(100, 255),
				MyRandom.between(100, 255));
		qMax = MyRandom.between(0, Q_MAX_MAX);
		qStart = Math.min(MyRandom.between(0, Q_START_MAX), qMax);
		
		qGrowth = MyRandom.between(Q_GROWTH_MIN, Q_GROWTH_MAX);
		qHarvest = MyRandom.between(0, Q_HARVEST_MAX);
		canDisapear = MyRandom.next()<CAN_DISAPEAR_PROB;
		canExpand = MyRandom.next()<CAN_EXPAND_PROB;
		expandProb = MyRandom.between(0, EXPAND_PROB_MAX);

		spontaneous = MyRandom.next()<SPONTANEOUS_PROB;
		spontaneousOnCorpse = MyRandom.next()<SPONTANEOUS_ON_CORPSE_PROB;
	}
	
}
