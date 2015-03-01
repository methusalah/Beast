package model.universe.beast.neuralnetwork.perception;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;
import math.Angle;
import math.MyRandom;
import model.universe.Tile;
import model.universe.beast.Beast;
import model.universe.beast.neuralnetwork.Brain;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class BeastSensor extends ExternalSensor {
	public BeastSensor(int serial, Brain brain) {
		super(serial, brain);
	}
	public BeastSensor(BeastSensor other, Brain newBrain) {
		super(other, newBrain);
	}

	@Override
	public double getStimulationRate() {
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

		if(closest == null)
			return 0;
		else
			return closest.getRacialDiff(brain.beast);
	}
}
