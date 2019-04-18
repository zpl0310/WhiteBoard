package GUI;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class DTextModel extends DShapeModel {
	private String text;
	private String font;
	
	public DTextModel(){
		x=10;
		y=10;
		width=20;
		height=20;
		text = "Hello";
		font = "Dialog";
		color = Color.GRAY;
	}
	
	public String getText() {
		return text;
	}
	
	
	public void setText(String text) {
		this.text = text;
		fireEvent();
	}
	
	public String getFont() {
		return font;
	}
	
	
	public void setFont(String font) {
		this.font = font;
		fireEvent();
	}
	
	@Override
	public void mimic(DShapeModel other) {
		if(other instanceof DTextModel) {
			String newText=((DTextModel) other).getText();
			String newFont=((DTextModel) other).getFont();
			this.text=newText;
			this.font=newFont;
			this.x=((DTextModel) other).x;
			this.y=((DTextModel) other).y;
			this.height= ((DTextModel) other).height;
			this.width=((DTextModel) other).width;
			this.color = ((DTextModel) other).getColor();
			fireEvent();
		}
	}
}
