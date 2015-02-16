package view;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JPanel;

import utils.Recorder;
import utils.StopWatch;
import model.Model;

public class ViewPanel extends JPanel {

	private static final Stroke SELECT_STROKE = new BasicStroke(2);
	private static final Color SELECT_COLOR = new Color(50, 50, 50, 250);
	private static final int BEAM_GLOW_RADIUS = 3;
	
	private Model model;
	private long frameNb = 0;
	
	public ViewPanel(Model model) {
		this.model = model;
		setPreferredSize(new Dimension(model.width, model.height));
	}

	@Override
	public void paint(Graphics g1) {
		StopWatch chrono = new StopWatch("View");
		frameNb++;
		Graphics2D g = (Graphics2D)g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(),  getHeight());


		Recorder.record(chrono);
		
	}
}
