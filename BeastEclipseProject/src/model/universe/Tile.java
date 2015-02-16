package model.universe;

import java.util.ArrayList;
import java.util.List;

import utils.MyRandom;

public class Tile {

	public final int x, y;
	final Universe universe;
	public final List<UComp> comps = new ArrayList<>();
	
	public Tile(int x, int y, Universe universe) {
		this.x = x;
		this.y = y;
		this.universe = universe;
	}
	
	public void registerOnCreation(UComp comp){
		universe.registerNewComp(comp);
		register(comp);
	}
	public void register(UComp comp){
		if(!comps.contains(comp))
			comps.add(comp);
		universe.setUpdated(this);
	}

	public void unregisterOnDestroy(UComp comp){
		universe.unregisterDestroyedComp(comp);
		unregister(comp);
	}
	public void unregister(UComp comp){
		boolean removed = comps.remove(comp);
		if(removed)
			universe.setUpdated(this);
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

}
