package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private Graphics2D graphics;
	private Boolean reset;
	private ArrayList<Float> data;
	
	public GraphicPanel(ArrayList<Float> _data, Boolean _reset) {
		this.reset = _reset;
		this.data = _data;
	}
	
	@Override
    public void paintComponent(Graphics g) {
        if(reset == true) super.paintComponent(g);
        this.graphics = (Graphics2D) g;
        this.graphics.setPaint(Color.black);
        this.graphics.setStroke(new BasicStroke());
        this.graphics.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));

        this.graphics.setPaint(Color.black);
        if(reset == true) {
	        for(Float x = 0f; x <= 500; x += 25) {
	        	this.graphics.draw(new Line2D.Float(x, 250, x, 5));
	        }
        }
        else {
        	for(Float x = 0f; x <= 500; x += 25) {
	        	this.graphics.draw(new Line2D.Float(400, x, 5, x));
	        }
        }
//        for (Float x = 50f; x <= 450; x += 400 * xInterval / dx)
//            g2d.draw(new Line2D.Float(x, 450, x, 50));
//        for (Float y = 50f; y <= 450; y += 400 * yInterval / dy)
//            g2d.draw(new Line2D.Float(45, y, 450, y));
    }
	
	public void drawTest(int x1, int y1, int x2, int y2, Paint color) {
		this.graphics.setPaint(color);
		this.graphics.draw(new Line2D.Float(x1, y1, x2, y2));
	}
}
