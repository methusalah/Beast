package view;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
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
			int nbChild = 0;
			int averageAge = 0;
			int averageGen = 0;
			for(Beast b : beasts){
				g.setColor(b.getColor());
				if(b.coord.getDistance(b.trail) == 0 ||
						b.coord.getDistance(b.trail) > 4)
					g.fillRect((int)(b.coord.x*4)+1, (int)(b.coord.y*4)+1, 2, 2);
							
				else					
					g.drawLine((int)(b.coord.x*4)+2, (int)(b.coord.y*4)+2,
							(int)(b.trail.x*4)+2, (int)(b.trail.y*4)+2);
//				if(b.age>90){
//					g.setStroke(new BasicStroke(3));
//					if(b.age > 1000){
//						g.setColor(Color.BLUE);
//						g.drawOval(((int)b.coord.x)*4-3, ((int)b.coord.y)*4-3, 6, 6);
//					} else {
////						g.setColor(Color.GREEN);
////						int ageRadius = b.age/10;
////						g.drawOval(((int)b.coord.x)*4-ageRadius, ((int)b.coord.y)*4-ageRadius, ageRadius*2, ageRadius*2);
//					}
//					g.setStroke(new BasicStroke(1));
//				}
				if(b.gen>0)
					nbChild++;
				averageAge += b.age;
				averageGen += b.gen;
				
				maxAge = Math.max(maxAge, b.age);
			}
			averageAge /= beasts.size();
			averageGen /= beasts.size();
//			g.setColor(Color.BLACK);
//			g.fillRect(4, getHeight()-4-12, getWidth()-8, 12);
			
			g.setColor(Color.black);
			g.setFont(new Font("Arial",Font.BOLD,15));
			double elapsed = System.currentTimeMillis()-timer;
			int turnPerSec = (int)Math.round(model.universe.grabTurnCounter()/(elapsed/1000));
			g.drawString(turnPerSec+" turn/s. Turn "+model.universe.turn, 4,  getHeight()-24);
			g.drawString("Average age: "+averageAge+"; average gen: "+averageGen, 4,  getHeight()-12);
			DecimalFormat df = new DecimalFormat("0");
			g.drawString("Natural Beasts: "+nbChild+"("+df.format(100*nbChild/model.universe.beasts.size())+"%)", 4,  getHeight());
		}
		chrono = new StopWatch("View");
		timer = System.currentTimeMillis();
	}
}
