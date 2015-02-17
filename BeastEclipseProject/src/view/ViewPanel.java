package view;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

import utils.Recorder;
import utils.StopWatch;
import model.Model;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.resource.ResourceSpot;

public class ViewPanel extends JPanel {

	private Model model;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(model.width, model.height));
	}

	@Override
	public void paint(Graphics g1) {
		StopWatch chrono = new StopWatch("View");
		Graphics2D g = (Graphics2D)g1;
		synchronized(model) {
			for(Tile t : model.universe.grabUpdated()){
				double red=0, green=0, blue=0;
				int spotCount=0;
				for(UComp comp : t.comps)
					if(comp instanceof ResourceSpot){
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
					g.setColor(Color.BLACK);
					
				g.drawLine(t.x, t.y, t.x, t.y);
			}
			g.setColor(Color.BLACK);
			g.fillRect(4, getHeight()-4-12, getWidth()-8, 12);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.PLAIN,12));
			g.drawString(Recorder.str()+"turn "+model.universe.turn+" active spots : "+model.universe.toUpdateSpots.size()+" beasts : "+model.universe.beasts.size(), 0+4,  getHeight()-4);
		}
		Recorder.record(chrono);
		Recorder.record(model.universe.stopwatch);
	}
}
