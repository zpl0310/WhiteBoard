package GUI;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class DShapeModel {
	protected int x, y, width, height;
	protected Color color;
	
	List<ModelListener> listener = new ArrayList<ModelListener>();
	
	public void fireEvent(){
		for (ModelListener l:listener){
			l.modelChanges(this);
		}
	}	
	
	public void addListener(ModelListener l){
		if (l!= null) listener.add(l);
	}
	
	public void removeListener(ModelListener l){
		if (listener.contains(l)) listener.remove(l);
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x,y,width,height);			
	}
	

	/*public void setBounds(int x, int y, int w, int h){
		this.x =x;
		this.y =y;
		this.width=w;
		this.height=h;
	}*/
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
		fireEvent();
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
		fireEvent();
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
		fireEvent();
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
		fireEvent();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
		fireEvent();
	}

	public void mimic(DShapeModel other) {
	}
}
