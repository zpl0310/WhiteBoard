package GUI;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

public class DText extends DShape {
	public DText(){
		sm = new DTextModel();
		knobs= new Point[4];
		sm.addListener(this);
	}
	
	public DText(DTextModel sm) {
		this.sm = sm;
		knobs= new Point[4];
		sm.addListener(this);
	}
	
	@Override
	public DTextModel getShapeModel() {
		return (DTextModel) this.sm;
	}
	
	@Override
	public void draw(Graphics g){
		g2 = (Graphics2D) g;
		g2.setColor(sm.getColor());
		g2.setFont(computeFont());
		Shape clip = g2.getClip();
		FontMetrics fm = g2.getFontMetrics();
		g2.setClip(clip.getBounds().createIntersection(sm.getBounds()));
		g2.drawString(((DTextModel) sm).getText(), sm.getX(),sm.getY()+(sm.getHeight()+fm.getAscent()-fm.getDescent())/2);	
		g2.setClip(clip);
	}
	
	private Font computeFont(){
		double size = 1.0;
		FontMetrics metric = g2.getFontMetrics(new Font(((DTextModel) sm).getFont(), Font.PLAIN, (int) size));
		while (metric.getHeight() <= sm.getHeight()){
			size = size *1.10+1;
			metric = g2.getFontMetrics(new Font(((DTextModel) sm).getFont(), Font.PLAIN, (int) size));
		}
		return new Font(((DTextModel) sm).getFont(), Font.PLAIN, (int) size);	
	}
	
	public void setText(String text){
		((DTextModel) sm).setText(text);
	}
	
	public void setFont(String font){
		((DTextModel) sm).setFont(font);
		
	}
	
}
