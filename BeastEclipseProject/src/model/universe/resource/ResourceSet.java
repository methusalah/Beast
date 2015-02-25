package model.universe.resource;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.MyRandom;

public class ResourceSet implements ResourceIDManager {
	public static final int NB_RESOURCE = 100; 
	

	public final List<Resource> resources = new ArrayList<>();
	
	public ResourceSet(boolean random) {
		if(random)
			for(int i = 0; i<NB_RESOURCE; i++)
				resources.add(new Resource(this, this));
		else {
			resources.add(new Resource(this, this,
					new Color(100, 250, 100),
					500,
					1,
					50,
					40,
					true,
					false));
			resources.add(new Resource(this, this,
					new Color(50, 150, 80),
					500,
					1,
					50,
					10,
					true,
					false));
//			resources.add(new Resource(this, this,
//					new Color(20, 200, 200),
//					1000,
//					500,
//					80,
//					1,
//					true,
//					false));
//			resources.add(new Resource(this, this,
//					new Color(250, 150, 150),
//					2000,
//					2000,
//					-100,
//					50,
//					false,
//					true));
		}
	}
	
	
	
	@Override
	public int giveNewID() {
		int id = 0;
		while(true){
			boolean available = true;
			for(Resource r : resources)
				if(r.id == id){
					available = false;
					break;
				}
			if(available)
				return id;
			else
				id++;
		}
	}
	@Override
	public Resource getRessource(int id) {
		for(Resource r : resources)
			if(r.id == id)
				return r;
		throw new RuntimeException(Resource.class.getSimpleName()+" with id "+id+" doesn't exist.");
	}
	
	public Resource getRandomResource(){
		
		return resources.get(MyRandom.between(0, resources.size()));
	}
	
	public void deleteResource(Resource r){
		resources.remove(r);
	}
	
	public int getCount(){
		return resources.size();
	}
}
