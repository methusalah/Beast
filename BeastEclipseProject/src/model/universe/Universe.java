package model.universe;


import geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;
import model.universe.beast.Beast;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSet;
import model.universe.resource.ResourceSpot;

public class Universe {
	private static final double RESOURCE_RATE = 0.05;
	private static final double BEAST_RATE = 0.05;

	public ResourceSet resourceSet;
	public final int width;
	public final int height;

	public final List<Tile> tiles = new ArrayList<>();
	private final List<Tile> updatedTiles = new ArrayList<>();
	
	public final List<ResourceSpot> toUpdateSpots = new ArrayList<>();
	public final List<Beast> beasts = new ArrayList<>();
	
	public int turn = 0;
	public int turnCounter = 0;
	
	public Universe(int width, int height) {
		this.width = width;
		this.height = height;
		
		resourceSet = new ResourceSet(false);
		
        for(int y=0; y<height; y++)
            for(int x=0; x<width; x++){
            	Tile t = new Tile(x, y, this);
                tiles.add(t);
                if(MyRandom.next() < RESOURCE_RATE)
                	new ResourceSpot(this, new Point2D(x, y), resourceSet.getRandomResource());
            }
        updatedTiles.addAll(tiles);
	}
	
	public void update(){
		turn++;
		turnCounter++;
		// Independent list is needed because on update, comps can register and unregister from the universe.
		List<UComp> indiList = new ArrayList<>();
		indiList.addAll(toUpdateSpots);
		indiList.addAll(beasts);
		for(UComp c : indiList){
			c.update();
		}
		
		int newBeastToCreate = 10;
		if(beasts.size() < width*height*BEAST_RATE)
			newBeastToCreate = (int)(width*height*BEAST_RATE);
		for(int i=0; i<newBeastToCreate; i++)
			new Beast(this, new Point2D(MyRandom.next()*(width-1), MyRandom.next()*(height-1)));
	}
	
	public int grabTurnCounter(){
		int res = turnCounter;
		turnCounter = 0;
		return res;
	}
	
    public Tile getTile(int x, int y) {
    	if(!isInBounds(x, y))
    		throw new IllegalArgumentException("Coords are not in "+Universe.class.getSimpleName()+"'s bounds ("+x+":"+y+")");
        return tiles.get(y*width+x);
    }

    public Tile getTile(Point2D coord) {
    	int x = (int)Math.floor(coord.x);
    	int y = (int)Math.floor(coord.y);
    	return getTile(x, y);
    }
    
    public boolean isInBounds(int x, int y){
    	return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public Point2D getInBounds(Point2D coord){
    	double x = coord.x;
    	while(x<0) x+=width;
    	while(x>=width) x-=width;
    	double y = coord.y;
    	while(y<0) y+=height;
    	while(y>=height) y-=height;
    	return new Point2D(x, y);
    }
	
    public Tile getNeightborTile(Tile t, int x, int y){
    	Point2D p = new Point2D(t.x,  t.y).getAddition(x, y);
    	return getTile(getInBounds(p));
    }


    public ResourceSpot getResourceSpot(Resource resource, Point2D coord){
    	Tile t = getTile(coord);
    	return t.getResourceSpot(resource);
    }
	
	public void register(UComp comp) {
		addToUpdates(comp);
		getTile(comp.coord).register(comp);
	}

	public void unregister(UComp comp) {
		removeFromUpdates(comp);
		getTile(comp.coord).unregister(comp);
	}
	
	public void addToUpdates(UComp comp){
		if(comp instanceof ResourceSpot)
			toUpdateSpots.add((ResourceSpot)comp);
		else
			beasts.add((Beast)comp);
	}
	
	public void removeFromUpdates(UComp comp){
		if(comp instanceof ResourceSpot)
			toUpdateSpots.remove(comp);
		else
			beasts.remove(comp);
	}
	
	public void manageCorpse(Beast b){
		for(Resource r : resourceSet.resources)
			if(r.spontaneousOnCorpse)
            	new ResourceSpot(this, b.coord, r);
	}
}
