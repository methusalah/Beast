package model.universe;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;
import model.universe.resource.Resource;
import model.universe.resource.ResourceSpot;

public class Tile {

	public final int x, y;
	final Universe universe;
	public final List<UComp> comps = new ArrayList<>();
	
	public Tile(int x, int y, Universe universe) {
		this.x = x;
		this.y = y;
		this.universe = universe;
	}
	
	public void register(UComp comp){
		if(!comps.contains(comp))
			comps.add(comp);
	}

	public void unregister(UComp comp){
		if(!comps.remove(comp))
			throw new RuntimeException("comp introuvable dans ce tile"+comp);
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
		for(UComp c : comps)
			if(c instanceof ResourceSpot && ((ResourceSpot)c).resource == r)
				return true;
		return false;
	}
	public ResourceSpot getResourceSpot(Resource r){
		for(UComp c : comps)
			if(c instanceof ResourceSpot && ((ResourceSpot)c).resource == r)
				return (ResourceSpot)c;
		return null;
	}

}
