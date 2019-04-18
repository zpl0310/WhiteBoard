package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class DLine extends DShape {
	//private DLineModel sm;
	
	public DLine() {
		sm = new DLineModel();
		knobs= new Point[2];
		sm.addListener(this);
	}
	
	public DLine(DLineModel sm){
		this.sm = sm;
		knobs= new Point[2];
		sm.addListener(this);
	}
	
	@Override
	public DLineModel getShapeModel() {
		return (DLineModel) this.sm;
	}

	@Override
	public void draw(Graphics g){
		g2 = (Graphics2D) g;
		g2.setColor(sm.getColor());
		g2.drawLine(((DLineModel)sm).getP1().x, ((DLineModel)sm).getP1().y, ((DLineModel)sm).getP2().x, ((DLineModel)sm).getP2().y);
	}
	
	@Override
	public Point[] getKnobs(){
		knobs[0] = ((DLineModel)sm).getP1();
		knobs[1] = ((DLineModel)sm).getP2();
		return knobs;
	}
	
	@Override
	public void resizing(Point movingP,Point anchorP) {
		if(anchorP.equals(knobs[0]))
			((DLineModel)sm).setP2(movingP);
		if(anchorP.equals(knobs[1]))
			((DLineModel)sm).setP1(movingP);
	}
	
	@Override
	public void moving(int x,int y) {
		this.sm.setX(x);
		this.sm.setY(y);		
	}
	
	@Override
	public Point getAnchorPoint(int index){
		if(index ==0) return knobs[1];
		if(index ==1) return knobs[0];
		return null;
	}
}
