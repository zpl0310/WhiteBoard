package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class DOval extends DShape {
	
	public DOval(){
		sm = new DOvalModel();
		knobs= new Point[4];
		sm.addListener(this);
	}
	
	public DOval(DOvalModel sm) {
		this.sm=sm;
		knobs= new Point[4];
		sm.addListener(this);
	}
	
	@Override
	public void draw(Graphics g){
		g2 = (Graphics2D) g;
		g2.setColor(sm.getColor());
		g2.fillOval(sm.getX(), sm.getY(), sm.getWidth(), sm.getHeight());
	}
}
