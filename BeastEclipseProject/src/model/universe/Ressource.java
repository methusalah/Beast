package model.universe;

public class Ressource {

	private static double Q_MAX_MAX = 100;
	private static double Q_GROW_MAX = 100;
	private static double Q_HARVEST_MAX = 100;
	private static double CAN_DISAPEAR_PROB = 0.5;
	private static double SPONTANEOUS_PROB = 0.5;
	
	int id;
	double qMax;
	double qGrow;
	double qHarvest;
	boolean canDisapear;
	boolean spontaneous;
	
}
