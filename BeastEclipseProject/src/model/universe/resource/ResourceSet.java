package model.universe.resource;

import java.util.ArrayList;
import java.util.List;

import math.MyRandom;

public class ResourceSet implements ResourceIDManager {
	public static final int NB_RESOURCE = 100; 
	

	final List<Resource> resources = new ArrayList<>();
	
	public ResourceSet() {
		for(int i = 0; i<NB_RESOURCE; i++)
			resources.add(new Resource(this, this));
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
		return resources.get(MyRandom.between(0, resources.size()-1));
	}
	
	public void deleteResource(Resource r){
		resources.remove(r);
	}
	
	public int getCount(){
		return resources.size();
	}
}
