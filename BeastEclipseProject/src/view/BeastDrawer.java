package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import tools.LogUtil;
import model.Model;
import model.universe.beast.Beast;

public class BeastDrawer {
	final Model model;
	
	int naturalBeatsCount;
	int maxAge;
	int maxGen;
	
	ArrayList<Integer> agesTable = new ArrayList<>(); 
	ArrayList<Integer> genTable = new ArrayList<>(); 
	
	public BeastDrawer(Model model) {
		this.model = model;
		for(int i=0; i<501; i++){
			genTable.add(0);
			agesTable.add(0);
		}
	}
	
	public void draw(Graphics2D g, int height){
		
		naturalBeatsCount = 0;
		maxAge = 0;
		maxGen = 0;
		for(int i=0; i<genTable.size(); i++){
			genTable.set(i, 0);
			agesTable.set(i, 0);
		}
		
		for(Beast b : model.universe.beasts){
			g.setColor(b.getColor());
			int size = Math.min(10, Math.max(1, (int)((double)b.gen/10)));
			
			if(b.coord.equals(b.trail) ||
					b.coord.getDistance(b.trail) > 10)
				g.fillRect((int)(b.coord.x*ViewPanel.SCALE)+2-size/2, (int)(b.coord.y*ViewPanel.SCALE)+2-size/2, size, size);
						
			else{
				g.setStroke(new BasicStroke(size));
				g.drawLine((int)(b.coord.x*ViewPanel.SCALE)+2, (int)(b.coord.y*ViewPanel.SCALE)+2,
						(int)(b.trail.x*ViewPanel.SCALE)+2, (int)(b.trail.y*ViewPanel.SCALE)+2);
				g.setStroke(new BasicStroke(1));
			}
			if(b.gen>0)
				naturalBeatsCount++;
			maxGen = Math.max(b.gen, maxGen);
			maxAge = Math.max(maxAge, b.age);
			if(b.gen<genTable.size())
				genTable.set(b.gen, genTable.get(b.gen)+1);
			int scaledAge = (int)Math.floor((double)b.age/10); 
			agesTable.set(scaledAge, genTable.get(scaledAge)+1);
			
		}

		int i = 0;
		int x = 0;
		for(Integer age : agesTable){
			// ground
			int ceil = 300;
			g.setColor(Color.BLACK);
			g.fillRect(x, height-ceil, 2, ceil);
			// age
			age = Math.min(ceil, age);
			g.setColor(Color.WHITE);
			g.fillRect(x, height-age, 2, height-age);
			// gen
			int gen = genTable.get(i);
			gen = Math.min(ceil, gen);
			g.setColor(Color.GREEN);
			g.fillRect(x, height-gen, 2, height-gen);
			x+=2;
			i++;
		}
	}

}
