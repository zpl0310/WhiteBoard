package GUI;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class DOvalModel extends DShapeModel {

	//Rectangle bounds;
	
	public DOvalModel(){
		x = 10;
		y = 10;
		width = 20;
		height = 20;
		color = Color.GRAY;
		//bounds = new Rectangle(x,y,width,height);
	}
	
	@Override 
	public Rectangle getBounds(){
		return new Rectangle(x,y,width,height);	
	}
	
	@Override
	public void mimic(DShapeModel other) {
		if(other instanceof DOvalModel) {
			x=((DOvalModel) other).x;
			y=((DOvalModel) other).y;
			width=((DOvalModel) other).width;
			height=((DOvalModel) other).height;
			color=((DOvalModel) other).color;
			fireEvent();
		}
	}

}