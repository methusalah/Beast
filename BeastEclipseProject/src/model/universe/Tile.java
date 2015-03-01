package model.universe;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;
import model.universe.beast.Beast;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class Tile {

	public final int x, y;
	final Universe universe;
	final List<ResourceSpot> spots = new ArrayList<>();
	final List<Beast> beasts = new ArrayList<>();
	
	public Tile(int x, int y, Universe universe) {
		this.x = x;
		this.y = y;
		this.universe = universe;
	}
	
	public void register(UComp comp){
		if(comp instanceof Beast)
			register((Beast)comp);
		else
			register((ResourceSpot)comp);
	}
	public void unregister(UComp comp){
		if(comp instanceof Beast)
			unregister((Beast)comp);
		else
			unregister((ResourceSpot)comp);
	}
			
	public void register(Beast beast){
		if(!beasts.contains(beast))
			beasts.add(beast);
	}

	public void register(ResourceSpot spot){
		if(!spots.contains(spot))
			spots.add(spot);
	}

	public void unregister(Beast beast){
		if(!beasts.remove(beast))
			throw new RuntimeException("beast doesn't exist in the "+this.getClass().getSimpleName()+" ("+beast+")");
	}
	public void unregister(ResourceSpot spot){
		if(!spots.remove(spot))
			throw new RuntimeException("beast doesn't exist in the "+this.getClass().getSimpleName()+" ("+spot+")");
	}
	
	public Tile getNorth(){
		return universe.getNeightborTile(this, 0, 1);
	}
	public Tile getSouth(){
		return universe.getNeightborTile(this, 0, -1);
	}
	public Tile getEast(){
		return universe.getNeightborTile(this, 1, 0);
	}
	public Tile getWest(){
		return universe.getNeightborTile(this, -1, 0);
	}
	public Tile getAnyNeighbor(){
		switch (MyRandom.nextInt(4)) {
		case 0: return getNorth();
		case 1: return getSouth();
		case 2: return getEast();
		case 3: return getWest();
		default: throw new RuntimeException();
		}
	}
	public List<Tile> getAllNeighbors(){
		List<Tile> res = new ArrayList<>();
		res.add(getNorth());
		res.add(getSouth());
		res.add(getEast());
		res.add(getWest());
		return res;
	}
	
	public boolean contains(Resource r){
		for(ResourceSpot s : spots)
			if(s.resource == r)
				return true;
		return false;
	}
	public ResourceSpot getResourceSpot(Resource r){
		for(ResourceSpot s : spots)
			if(s.resource == r)
				return s;
		return null;
	}
	public List<ResourceSpot> getSpots(){
		return spots;
	}
	public List<Beast> getBeasts(){
		return beasts;
	}

}
