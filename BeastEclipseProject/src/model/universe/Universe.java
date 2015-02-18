package model.universe;


import geometry.Point2D;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;

import model.universe.beast.Beast;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSet;
import model.universe.resource.ResourceSpot;
import utils.LogUtil;
import utils.MyRandom;
import utils.StopWatch;

public class Universe {
	private static final double RESOURCE_RATE = 0.05;

	public ResourceSet resourceSet;
	public final int width;
	public final int height;

	public final List<Tile> tiles = new ArrayList<>();
	private final List<Tile> updatedTiles = new ArrayList<>();
	
	public final List<UComp> toUpdateSpots = new ArrayList<>();
	public final List<UComp> beasts = new ArrayList<>();
	
	public StopWatch stopwatch;
	public String chrono;
	
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
                	new ResourceSpot(this, new Point2D(x, y), resourceSet.getRandomResource());
            }
        updatedTiles.addAll(tiles);
		stopwatch = new StopWatch("universe");
	}
	
	public void update(){
		stopwatch = new StopWatch("universe");
		turn++;
		// Independent list is needed because on update, comps can register and unregister from the universe.
		List<UComp> indiList = new ArrayList<>();
		indiList.addAll(toUpdateSpots);
		indiList.addAll(beasts);
		for(UComp c : indiList){
			c.update();
		}
		
		if(beasts.size() < 1000)
			for(int i=0; i<1000; i++)
				new Beast(this, new Point2D(MyRandom.next()*(width-1), MyRandom.next()*(height-1)));
		chrono = stopwatch.toString();
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
//    	if(t == null)
//    		throw new NullPointerException();
//		if(!updatedTiles.contains(t))
//			updatedTiles.add(t);
	}
    
    public ResourceSpot getResourceSpot(Resource resource, Point2D coord){
    	Tile t = getTile(coord);
    	return t.getResourceSpot(resource);
    }
	
	public List<Tile> grabUpdated(){
		List<Tile> res = new ArrayList<>();
		res.addAll(updatedTiles);
		updatedTiles.clear();
		return res;
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
			toUpdateSpots.add(comp);
		else
			beasts.add(comp);
	}
	
	public void removeFromUpdates(UComp comp){
		if(comp instanceof ResourceSpot)
			toUpdateSpots.remove(comp);
		else
			beasts.remove(comp);
	}
	
}
