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

import model.Model;
import model.universe.Tile;
import model.universe.UComp;
import model.universe.beast.Beast;
import model.universe.resource.ResourceSpot;
import utils.StopWatch;

public class ViewPanel extends JPanel {
	static int SCALE = 7;

	ResourceDrawer resourceDrawer;
	BeastDrawer beastDrawer;
	private Model model;
	StopWatch chrono;
	Long timer = (long) 0.0;
	
	public ViewPanel(Model model) {
		this.model = model;
		resourceDrawer = new ResourceDrawer(model);
		beastDrawer = new BeastDrawer(model);
		setPreferredSize(new Dimension(model.universe.width*SCALE, model.universe.height*SCALE*2));
		chrono = new StopWatch("View");
	}

	int maxAge = 0;
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		synchronized(model) {
			double elapsed = System.currentTimeMillis()-timer;
			int turnPerSec = (int)Math.round(model.universe.grabTurnCounter()/(elapsed/1000));

			resourceDrawer.draw(g);
			beastDrawer.draw(g, getHeight());
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.BOLD,15));
			g.drawString(turnPerSec+" turn/s. Turn "+model.universe.turn+" ResourceSetCount: "+model.universe.resourceSet.getCount(), 4,  getHeight()-300+12);
			g.drawString("Max gen: "+beastDrawer.maxGen, 4,  getHeight()-300+24);
			DecimalFormat df = new DecimalFormat("0");
			g.drawString("Natural Beasts: "+beastDrawer.naturalBeatsCount+"("+df.format(100*beastDrawer.naturalBeatsCount/model.universe.beasts.size())+"%)", 4,  getHeight()-300+36);
		}
		chrono = new StopWatch("View");
		timer = System.currentTimeMillis();
	}
}
