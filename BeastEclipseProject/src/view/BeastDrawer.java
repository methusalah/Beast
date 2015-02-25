package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

import tools.LogUtil;
import model.Model;
import model.universe.beast.Beast;

public class BeastDrawer {
	final Model model;
	
	int naturalBeatsCount;
	int maxAge;
	int maxGen;
	Beast dino;
	
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
			int size = Math.min(4, (int)(Math.ceil((double)b.gen/10)));

			if(size == 0)
				g.drawLine((int)(b.coord.x*ViewPanel.SCALE), (int)(b.coord.y*ViewPanel.SCALE),
						(int)(b.coord.x*ViewPanel.SCALE), (int)(b.coord.y*ViewPanel.SCALE));
			else
				g.fillOval((int)(b.coord.x*ViewPanel.SCALE)-size, (int)(b.coord.y*ViewPanel.SCALE)-size, 1+size*2, 1+size*2);
			if(!b.coord.equals(b.trail) && b.coord.getDistance(b.trail) < 10){
				g.setStroke(new BasicStroke(Math.min(4, size)));
				g.drawLine((int)(b.coord.x*ViewPanel.SCALE), (int)(b.coord.y*ViewPanel.SCALE),
						(int)(b.trail.x*ViewPanel.SCALE), (int)(b.trail.y*ViewPanel.SCALE));
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
			if(dino == null || b.dinoGen > dino.dinoGen)
				dino = b;
			
		}

		int ceil = 300;
		int i = 0;
		int x = 0;
		for(Integer age : agesTable){
			// ground
			g.setColor(Color.BLACK);
			g.fillRect(x, height-ceil, 2, ceil);
			// gen
			int gen = genTable.get(i);
			gen = Math.min(ceil, gen);
			g.setColor(Color.GREEN);
			g.fillRect(x, height-gen, 2, height-gen);
			x+=2;
			i++;
		}
		
		g.setColor(dino.need.resource.color);
		g.setFont(new Font("Arial",Font.BOLD,15));
		drawString(g, ""+dino.need, 400,  height-ceil+24);
	}
	
	void drawString(Graphics2D g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	        g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

}
