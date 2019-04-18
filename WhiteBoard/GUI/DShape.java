package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class DShape implements ModelListener{
	protected DShapeModel sm;
	Graphics2D g2;
	Point [] knobs;
	
	public DShapeModel getShapeModel() {
		return this.sm;
	}
	
	public void draw(Graphics g){
		draw(g2);
	}
	
	public void modelChanges(DShapeModel sm){
		draw(g2);
	} 
	
	public Point[] getKnobs(){
		knobs[0] = new Point(sm.getX(),sm.getY());
		knobs[1] = new Point(sm.getX()+sm.getWidth(),sm.getY());
		knobs[2] = new Point(sm.getX(),sm.getY()+sm.getHeight());
		knobs[3] = new Point(sm.getX()+sm.getWidth(),sm.getY()+sm.getHeight());
		return knobs;
	}
	
	
	public Rectangle getBounds(){
		return sm.getBounds();
	}
	
	public int getX(){
		return sm.getX();
	}
	
	public int getY(){
		return sm.getY();
	}
	
	public int getWidth(){
		return sm.getWidth();
	}
	
	public int getHeight(){
		return sm.getHeight();
	}
	
	public void resizing(Point movingP,Point anchorP){
		int newX = (int) Math.min(movingP.getX(), anchorP.getX());
		int newY = (int) Math.min(movingP.getY(), anchorP.getY());
		int newWidth = (int) Math.abs(movingP.getX()-anchorP.getX());
		int newHeight = (int) Math.abs(movingP.getY()-anchorP.getY());
		this.sm.setX(newX);
		this.sm.setY(newY);
		this.sm.setWidth(newWidth);
		this.sm.setHeight(newHeight);
	}
	
	public Point getAnchorPoint(int index){
		if (index == 0)	return knobs[3];	
		if (index == 1)	return knobs[2];
		if (index == 2) return knobs[1];
		if (index == 3) return knobs[0];
		return null;
	}
	
	public void moving(int x,int y){
		this.sm.setX(x);
		this.sm.setY(y);
	}
}
