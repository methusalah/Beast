package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import tools.LogUtil;
import utils.StopWatch;
import model.Model;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.beast.Beast;
import model.universe.resource.ResourceSpot;

public class ViewPanel extends JPanel {

	private Model model;
	StopWatch chrono;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(model.universe.width*4, model.universe.height*4));
		chrono = new StopWatch("View");
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		synchronized(model) {
			for(Tile t : model.universe.tiles){
				double red=0, green=0, blue=0;
				int spotCount=0;
				int size = 0;
				boolean beast = false;
				
				for(UComp comp : t.comps)
					if(comp instanceof Beast){
						g.setColor(comp.getColor());
						g.fillRect(t.x*4+1, t.y*4+1, 2, 2);
//						g.fillRect(t.x*4+1, t.y*4, 2, 4);
//						g.fillRect(t.x*4, t.y*4+1, 4, 2);
						beast = true;
						break;
					} else if(comp instanceof ResourceSpot){
						ResourceSpot spot = (ResourceSpot)comp;
						spotCount++;
						red += spot.resource.color.getRed()*spot.getRate();
						green += spot.resource.color.getGreen()*spot.getRate();
						blue += spot.resource.color.getBlue()*spot.getRate();
					}
				if(beast)
					continue;
				if(spotCount != 0){
					red /= spotCount;
					green /= spotCount;
					blue /= spotCount;
					g.setColor(new Color((int)red, (int)green, (int)blue));
				} else
					g.setColor(Color.WHITE);

				g.fillRect(t.x*4, t.y*4, 4, 4);
			}
			g.setColor(Color.BLACK);
			g.fillRect(4, getHeight()-4-12, getWidth()-8, 12);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.PLAIN,12));
			g.drawString(chrono.toString()+" "+model.universe.chrono+" turn "+model.universe.turn+" active spots : "+model.universe.toUpdateSpots.size()+" beasts : "+model.universe.beasts.size(), 0+4,  getHeight()-4);
		}
		chrono = new StopWatch("View");
//		Recorder.record(chrono);
//		Recorder.record(model.universe.stopwatch);
	}
}
