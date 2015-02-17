package model.universe;


import geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

import model.universe.resource.ResourceSet;
import model.universe.resource.ResourceSpot;
import utils.LogUtil;
import utils.MyRandom;
import utils.StopWatch;

public class Universe {
	private static final double RESOURCE_RATE = 0.05;

	ResourceSet resourceSet;
	final int width, height;
	public final List<Tile> tiles = new ArrayList<>();
	final List<Tile> updatedTiles = new ArrayList<>();
	
	public final List<UComp> comps = new ArrayList<>();
	
	public StopWatch stopwatch;
	
	public int turn;
	
	public Universe(int width, int height) {
		this.width = width;
		this.height = height;
		
		resourceSet = new ResourceSet();
		
        for(int y=0; y<height; y++)
            for(int x=0; x<width; x++){
            	Tile t = new Tile(x, y, this);
                tiles.add(t);
                if(MyRandom.next() < RESOURCE_RATE)
                	t.register(new ResourceSpot(this, new Point2D(x, y), resourceSet.getRandomResource()));
            }
        updatedTiles.addAll(tiles);
	}
	
	public void update(){
		stopwatch = new StopWatch("universe");
		turn++;
		// Independent list is needed because on update, comps can register and unregister from the universe.
		List<UComp> indiList = new ArrayList<>();
		indiList.addAll(comps);
		for(UComp c : indiList){
			c.update();
		}
		
	}
	
    public Tile getTile(int x, int y) {
    	if(!isInBounds(x, y))
    		throw new IllegalArgumentException("Coords are not in "+Universe.class.getSimpleName()+"'s bounds ("+x+":"+y+")");
        return tiles.get(y*width+x);
    }

    public Tile getTile(Point2D coord) {
    	int x = (int)Math.round(coord.x);
    	int y = (int)Math.round(coord.y);
    	return getTile(x, y);
    }
    
    public boolean isInBounds(int x, int y){
    	return x >= 0 && x < width && y >= 0 && y < height;
    }
	
    public Tile getNeightborTile(Tile t, int x, int y){
    	x = t.x+x;
    	while(x<0) x+=width;
    	while(x>width-1) x-=width;
    	y = t.y+y;
    	while(y<0) y+=height;
    	while(y>height-1) y-=height;
    	return getTile(x, y);
    }

    public void setUpdated(Tile t){
		if(!updatedTiles.contains(t))
			updatedTiles.add(t);
	}
	
	public List<Tile> grabUpdated(){
		List<Tile> res = new ArrayList<>();
		res.addAll(updatedTiles);
		updatedTiles.clear();
		return res;
	}

	public void registerNewComp(UComp comp) {
		comps.add(comp);
	}

	public void unregisterDestroyedComp(UComp comp) {
		comps.remove(comp);
	}
	
}
