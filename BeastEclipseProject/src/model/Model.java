package model;

import utils.LogUtil;
import model.universe.Universe;


public class Model {

	public final Universe universe;
	
	public Model(int width, int height) {
		universe = new Universe(width, height);
	}
	
	
}
