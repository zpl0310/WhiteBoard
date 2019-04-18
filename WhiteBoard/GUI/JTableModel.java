package GUI;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class JTableModel extends AbstractTableModel implements ModelListener{
	private ArrayList <DShapeModel> data; 
	private int rowNum;

	public JTableModel() {
		data = new ArrayList<DShapeModel>();
	}
	public int getRowCount(){
		return data.size();
	}

	public int getColumnCount(){
		return 4;
	}
	 
	@Override
	public String getColumnName(int column) {
		  switch (column) {
          	  case 0:  return "X";
	          case 1:  return "Y";
	          case 2:  return "Width";
	          case 3:  return "Height";
		  }
		  return null;
	}
	
	public Object getValueAt(int row, int column){
		if(!data.isEmpty()) {
			DShapeModel rowData=data.get(row);
			  switch (column) {
	          case 0:  return rowData.getX();
	          case 1:  return rowData.getY();
	          case 2:  return rowData.getWidth();
	          case 3:  return rowData.getHeight();
		  }
		}
		return null;
	}
		
	public void addRow(DShapeModel sm) {
		sm.addListener(this);
		data.add(sm);
		fireTableDataChanged();
	}
	
	public void deleteRow(DShapeModel sm) {
		sm.removeListener(this);
		data.remove(sm);
		fireTableDataChanged();
	}
	
	public void clearTable(){
		data.clear();
		fireTableDataChanged();
	}
	
	@Override
	public void modelChanges(DShapeModel sm) {
		rowNum = data.indexOf(sm);
		fireTableRowsUpdated(rowNum, rowNum);	
	}
}