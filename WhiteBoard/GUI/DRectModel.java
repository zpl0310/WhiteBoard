package GUI;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class DRectModel extends DShapeModel {
	
	public DRectModel(){		
		x = 10;
		y = 10;
		width = 20;
		height = 20;
		color = Color.GRAY;
	}
	
	public DRectModel(int x, int y, int width,int height,Color color){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;		
	}
	
	@Override 
	public Rectangle getBounds(){
		return new Rectangle(x,y,width,height);	
	}
	
	@Override
	public void mimic(DShapeModel other) {
		if(other instanceof DRectModel) {
			x=((DRectModel) other).x;
			y=((DRectModel) other).y;
			width=((DRectModel) other).width;
			height=((DRectModel) other).height;
			color=((DRectModel) other).color;
			fireEvent();
		}
	}
}

