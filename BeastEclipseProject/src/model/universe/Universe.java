package model.universe;


import java.util.ArrayList;
import java.util.List;

public class Universe {

	RessourceSet ressourceSet;
	final int width, height;
	final List<Tile> tiles = new ArrayList<>();
	final List<Tile> updatedTiles = new ArrayList<>();
	
	public Universe(int width, int height) {
		this.width = width;
		this.height = height;
        for(int y=0; y<height; y++)
            for(int x=0; x<width; x++)
                tiles.add(new Tile(x, y, this));
	}
	
    public Tile getTile(int x, int y) {
    	if(!isInBounds(x, y))
    		throw new IllegalArgumentException("coord are not in universe bounds"+x+":"+y);
        return tiles.get(y*width+x);
    }
    
    public boolean isInBounds(int x, int y){
    	return x < 0 || x>width-1 || y<0 || y>height-1;
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
	
}
