package GUI;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class DLineModel extends DShapeModel {
	private Point p1, p2;
	
	public DLineModel(){
		x=10;
		y=10;
		width=20;
		height=20;
		color = Color.GRAY;
		p1= new Point(x,y);
		p2= new Point(x+width,y+height);
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}
	
	public void setP1(Point p) {
		p1.x=p.x;
		p1.y=p.y;
		this.x=Math.min(p1.x, p2.x);
		this.y=Math.min(p1.y, p2.y);
		this.height= Math.abs(p1.y-p2.y);
		this.width=Math.abs(p1.x-p2.x);
		fireEvent();
	}
	
	public void setP2(Point p) {
		p2.x=p.x;
		p2.y=p.y;
		this.x=Math.min(p1.x, p2.x);
		this.y=Math.min(p1.y, p2.y);
		this.height= Math.abs(p1.y-p2.y);
		this.width=Math.abs(p1.x-p2.x);
		fireEvent();
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
		if(p1.x<p2.x) {
			p1.x=x;
			p2.x=x+width;
		} else {
			p2.x=x;
			p1.x=x+width;
		}
		fireEvent();
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
		if(p1.y<p2.y) {
			p1.y=y;
			p2.y=y+height;
		} else {
			p2.y=y;
			p1.y=y+height;
		}
		fireEvent();
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
		if(p1.x<p2.x) p2.x=p1.x+width;
		else p1.x=p2.x+width;
		fireEvent();
	}
	
	public void setHeight(int height) {
		this.height = height;
		if(p1.y<p2.y) p2.y=p1.y+height;
		else p1.y=p2.y+height;
		fireEvent();
	}
	
	public void mimic(DShapeModel other) {
		if(other instanceof DLineModel) {
			Point newP1=((DLineModel) other).getP1();
			Point newP2=((DLineModel) other).getP2();
			this.p1=newP1;
			this.p2=newP2;
			this.x=Math.min(newP1.x, newP2.x);
			this.y=Math.min(newP1.y, newP2.y);
			this.height= Math.abs(newP1.y-newP2.y);
			this.width=Math.abs(newP1.x-newP2.x);
			this.color = other.getColor();
			fireEvent();
		}
	}
}
