package view;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

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
	Long timer = (long) 0.0;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(model.universe.width*4, model.universe.height*4));
		chrono = new StopWatch("View");
	}

	int maxAge = 0;
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		synchronized(model) {
			List<Beast> beasts = new ArrayList<>();
			for(Tile t : model.universe.tiles){
				double red=0, green=0, blue=0;
				int spotCount=0;
				
				for(UComp comp : t.comps)
					if(comp instanceof Beast){
						beasts.add((Beast)comp);
					} else if(comp instanceof ResourceSpot){
						ResourceSpot spot = (ResourceSpot)comp;
						spotCount++;
						red += spot.resource.color.getRed()*spot.getRate();
						green += spot.resource.color.getGreen()*spot.getRate();
						blue += spot.resource.color.getBlue()*spot.getRate();
					}
				if(spotCount != 0){
					red /= spotCount;
					green /= spotCount;
					blue /= spotCount;
					g.setColor(new Color((int)red, (int)green, (int)blue));
				} else
					g.setColor(Color.WHITE);

				g.fillRect(t.x*4, t.y*4, 4, 4);
			}
			for(Beast b : beasts){
				g.setColor(b.getColor());
				g.fillRect((int)(b.coord.x*4)+1, (int)(b.coord.y*4)+1, 2, 2);
				if(b.age>90){
					g.setColor(Color.GREEN);
					g.setStroke(new BasicStroke(3));
					int ageRadius = b.age/10;
					g.drawOval(((int)b.coord.x-ageRadius)*4, ((int)b.coord.y-ageRadius)*4, ageRadius*2, ageRadius*2);
					g.setStroke(new BasicStroke(1));
				}
				maxAge = Math.max(maxAge, b.age);
			}
			g.setColor(Color.BLACK);
			g.fillRect(4, getHeight()-4-12, getWidth()-8, 12);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.PLAIN,12));
			double elapsed = System.currentTimeMillis()-timer;
			int turnPerSec = (int)Math.round(model.universe.grabTurnCounter()/(elapsed/1000));
			g.drawString("  "+turnPerSec+" turn/s. Turn "+model.universe.turn+" MaxAge:"+maxAge+" active spots : "+model.universe.toUpdateSpots.size()+" beasts : "+model.universe.beasts.size(), 0+4,  getHeight()-4);
		}
		chrono = new StopWatch("View");
		timer = System.currentTimeMillis();
	}
}
