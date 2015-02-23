package view;

import java.awt.Color;
import java.awt.Graphics2D;

import model.Model;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.resource.ResourceSpot;

public class ResourceDrawer {
	final Model model;
	
	public ResourceDrawer(Model model) {
		this.model = model;
	}
	
	public void draw(Graphics2D g){
		for(Tile t : model.universe.tiles){
			double red=0, green=0, blue=0;
			int spotCount=0;
			for(UComp comp : t.comps)
				if(comp instanceof ResourceSpot){
					ResourceSpot spot = (ResourceSpot)comp;
					spotCount++;
					red += (255-spot.resource.color.getRed())*(1-spot.getRate())+spot.resource.color.getRed();
					green += (255-spot.resource.color.getGreen())*(1-spot.getRate())+spot.resource.color.getGreen();
					blue += (255-spot.resource.color.getBlue())*(1-spot.getRate())+spot.resource.color.getBlue();
				}
			if(spotCount != 0){
				red /= spotCount;
				green /= spotCount;
				blue /= spotCount;
				g.setColor(new Color((int)red, (int)green, (int)blue));
			} else
				g.setColor(Color.WHITE);

			g.fillRect(t.x*ViewPanel.SCALE, t.y*ViewPanel.SCALE, ViewPanel.SCALE, ViewPanel.SCALE);
		}
	}
}
