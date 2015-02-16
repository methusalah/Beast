package model.universe;

import java.util.ArrayList;
import java.util.List;

public class Tile {

	final int x, y;
	final Universe universe;
	final List<UComp> comps = new ArrayList<>();
	
	public Tile(int x, int y, Universe universe) {
		this.x = x;
		this.y = y;
		this.universe = universe;
	}
	
	
	public void register(UComp comp){
		if(!comps.contains(comp))
			comps.add(comp);
		universe.setUpdated(this);
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

}
