package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class canvas extends javax.swing.JPanel{
		private ArrayList<DShape> shapes;
		DShape selectedShape;
		private int deX = 0;
		private int deY = 0;
		private DRect[] knobs;
		protected boolean isResizing = false;
		private int movingPIndex = -1;
		private Point anchorP;
		private JTableModel model;
		
		public canvas(){
			shapes = new ArrayList<DShape>();
			model = new JTableModel();
			this.setBackground(Color.WHITE);
			this.setMinimumSize(new Dimension(400,400));
		}
		
		
		private void getSelectedShape(Point p){
			for (int i = shapes.size()-1;i>=0;i--){
     		   if (shapes.get(i).getBounds().contains(p)){	            			
     			   selectedShape=shapes.get(i); 
     			   knobs = new DRect[selectedShape.getKnobs().length];
     			   deX = (int) (p.getX() - selectedShape.getShapeModel().getX());
     			   deY = (int) (p.getY() - selectedShape.getShapeModel().getY());
     			   break;
     		   }
     		   else {
     			   selectedShape = null;
     			   knobs = null;
     		   }
     	   }
		}
				
		public int clickedOnKnob(Point p){
			for (int i = 0; i < knobs.length; i++){
				if (knobs[i].getBounds().contains(p)){
					return i;
				}
			}
			return -1;
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			for (DShape s:shapes){
				if (s != selectedShape){
					s.draw(g);}
				else {			
					s.draw(g);
					//knobs = new DRect[s.getKnobs().length];
					for (int i =0;i<knobs.length;i++){
					knobs[i] = new DRect(new DRectModel((int)(selectedShape.getKnobs()[i].getX()-4.5),
							(int)(selectedShape.getKnobs()[i].getY()-4.5),9,9,Color.BLACK));}
					for (DRect knob:knobs){
						knob.draw(g);
					}					
				}
			}			
		}
		
		public void addShape(DShapeModel sm){
			if (sm instanceof DRectModel){
				shapes.add(new DRect((DRectModel)sm));
			} else if(sm instanceof DOvalModel) {
				shapes.add(new DOval((DOvalModel)sm));
			} else if(sm instanceof DLineModel) {
				shapes.add(new DLine((DLineModel)sm));
			}else if(sm instanceof DTextModel){
				shapes.add(new DText((DTextModel)sm));
			}
			model.addRow(sm);
		}
		
		public DShape removeShape() {
			if (selectedShape!=null){
				shapes.remove(selectedShape);
				model.deleteRow(selectedShape.getShapeModel());
				return selectedShape;
			}
			return null;
		}
		
		public void removeShape(int index) {
			if(index >= 0 && index <shapes.size()) {
				selectedShape=shapes.get(index);
			}
			this.removeShape();
		}
		
		public DShape MoveToFront() {
			if (selectedShape!=null){
				DShape copy = selectedShape;
				shapes.remove(selectedShape);
				shapes.add(copy);
				return selectedShape;
			}
			return null;
		}
		
		public void MoveToFront(int index) {
			if(index >= 0 && index <shapes.size()) {
				selectedShape=shapes.get(index);
			}
			this.MoveToFront();
		}
		
		public DShape MoveToBack() {
			if (selectedShape!=null){
				DShape copy = selectedShape;
				shapes.remove(selectedShape);
				shapes.add(0,copy);
				return selectedShape;
			}
			return null;
		}
		
		public void MoveToBack(int index) {
			if(index >= 0 && index <shapes.size()) {
				selectedShape=shapes.get(index);
			}
			this.MoveToBack();
		}
		
		public JTableModel getTableModel() {
			return model;
		}
			
		public void clear(){
			shapes.clear();
			model.clearTable();
			repaint();
		}				
		
	    public ArrayList<DShape> getShapes() {
			return shapes;
		}
	    
	    public void clearKnobs(){
	    	selectedShape = null;
	    	repaint();
	    }
	    
	    public void addRect(){
			DRectModel rm = new DRectModel();
			addShape(rm);
			repaint();
	    }
	    
	    public void addOval(){
	    	DOvalModel om = new DOvalModel();
			addShape(om);
			repaint();
	    }
	    
	    public void addLine(){
			DLineModel lm = new DLineModel();
			addShape(lm);
			repaint();
	    }
	    
	    public void addText(){
			DTextModel tm = new DTextModel();
			addShape(tm);
			repaint();	
	    }

		public void setSelectedShape(DShape selectedShape) {
			this.selectedShape = selectedShape;
		}
 		
		public int getSelectedID(){
			int ID = -1;
			if (selectedShape!=null) 
				 ID= shapes.indexOf(selectedShape);
			return ID;
		}
		
		public int getID(DShape shape){
			return shapes.indexOf(shape);
		}
		
		public void reset(){
			isResizing = false;
    	    movingPIndex=-1;
    	    anchorP=null;
		}
		
		public void mouseDragging(MouseEvent e){
			if (isResizing){
				selectedShape.resizing(e.getPoint(), anchorP);
				repaint();
			   }
			else if (selectedShape != null){
	     	    selectedShape.moving(e.getX()-deX, e.getY()-deY);
	     		repaint();
	     	   }
		}
		
		public void mousePressing(MouseEvent e){
		    Point p =e.getPoint();
		    
     	    if (selectedShape == null){
    		    getSelectedShape(p);
    	    }      	   
    	    else {         		  
    	    	movingPIndex=clickedOnKnob(p);
    			if (movingPIndex != -1){
     			   isResizing = true;
     			   anchorP = selectedShape.getAnchorPoint(movingPIndex);
    			}
    			else {
     			   getSelectedShape(p);
     			   }
		    }   
       	    repaint();
		}
}
