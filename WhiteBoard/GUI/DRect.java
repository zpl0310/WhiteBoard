package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class DRect extends DShape {
	
	public DRect(){
		sm = new DRectModel();
		knobs= new Point[4];
		sm.addListener(this);	
	}
	
	public DRect(DRectModel sm){
		this.sm = sm;
		knobs= new Point[4];
		sm.addListener(this);
	}
	
	@Override
	public void draw(Graphics g){
		g2 = (Graphics2D) g;
		g2.setColor(sm.getColor());
		g2.fillRect(sm.getX(), sm.getY(), sm.getWidth(), sm.getHeight());		
	}
	
}
