package model;

import model.universe.Universe;


public class Model {

	public int width;
	public int height;
	
	Universe universe;
	
	public Model(int width, int height) {
		this.width = width;
		this.height = height;
		
		universe = new Universe(width, height);

	}
}
