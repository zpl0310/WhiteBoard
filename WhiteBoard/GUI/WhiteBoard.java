package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.swing.*;
import java.awt.*;

public class WhiteBoard extends javax.swing.JFrame {
	private canvas can;
	private JButton recBtn;
	private JButton ovalBtn;
	private JButton lineBtn;
	private JButton textBtn;
	private JButton SetColor ;
	private JButton MoveToFront ;
	private JButton MoveToBack ;
	private JButton RemoveShape;
	private JTable table;
	private JPanel control1;
	private JMenuItem Server;
	private JMenuItem Client;
	private JMenuItem save;
	private JMenuItem open;
	private JMenuItem saveImage;
	private JLabel status;
	private Server srv;
	private boolean isServer;
	private boolean isClient;
	private JTextField text;
	private JComboBox<String> font;
	private Semaphore done;
	
	public void showGUI() {
		done = new Semaphore(0);
		JFrame frame = new JFrame("Whiteboard");
		can = new canvas();
		isServer = false;
		isClient = false;
		frame.setLayout(new BorderLayout());
		frame.add(can,BorderLayout.CENTER);
		frame.setJMenuBar(createMenuBar());
		
		JPanel control = new JPanel();
		control.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		control.setPreferredSize(new Dimension(380,400));
		control1 = new JPanel();
		JPanel control3 = new JPanel();
		JPanel control4 = new JPanel();
		JPanel control5 = new JPanel();
		
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
		control1.setLayout(new BoxLayout(control1, BoxLayout.X_AXIS));
		control3.setLayout(new BoxLayout(control3, BoxLayout.X_AXIS));
		control4.setLayout(new BoxLayout(control4,BoxLayout.X_AXIS));
		control5.setLayout(new BoxLayout(control5,BoxLayout.X_AXIS));

		JLabel Add = new JLabel("  Draw  "); control1.add(Add);
		Add.setFont(new Font("Tahoma", Font.BOLD, 14));
		Add.setForeground(new Color(59, 89, 182));

		recBtn = new JButton("  Rect  "); control1.add(recBtn);	
		control1.add(Box.createRigidArea(new Dimension(4,0)));
		ovalBtn = new JButton("  Oval  "); control1.add(ovalBtn);
		control1.add(Box.createRigidArea(new Dimension(4,0)));
		lineBtn = new JButton("  Line  "); control1.add(lineBtn);
		control1.add(Box.createRigidArea(new Dimension(4,0)));
		textBtn = new JButton("  Text  "); control1.add(textBtn);
		control1.add(Box.createRigidArea(new Dimension(4,0)));
		
		SetColor = new JButton("Set Color"); control5.add(SetColor);
		
		MoveToFront = new JButton("Move To Front"); control3.add(MoveToFront);
		control3.add(Box.createRigidArea(new Dimension(2,0)));
		MoveToBack = new JButton("Move To Back"); control3.add(MoveToBack);
		control3.add(Box.createRigidArea(new Dimension(2,0)));
		RemoveShape = new JButton("Remove Shape"); control3.add(RemoveShape);
		
		text = new JTextField(10);control4.add(text);
		text.setEnabled(false);
		text.setMaximumSize(new Dimension(200,30));
		text.setMinimumSize(new Dimension(100,30));
		text.setForeground(new Color(255, 102, 153));
		text.setFont(new Font("Tahoma", Font.PLAIN, 14));
		control4.setMaximumSize(new Dimension(400,30));
		font = new JComboBox<String>();control4.add(font);
		font.setEnabled(false);
		font.setMaximumSize(new Dimension(200,30));
		font.setForeground(new Color(255, 102, 153));
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); 
		for (String f:fonts){font.addItem(f);}
		
		status = new JLabel("          ");
		status.setFont(new Font("Tahoma", Font.PLAIN, 14));
		status.setForeground(new Color(59, 89, 182));
		control5.add(Box.createRigidArea(new Dimension(200,0)));
		control5.add(status);
			
		table = new JTable(can.getTableModel());
		setTableStyle(table);
		JScrollPane scrollpane = new JScrollPane(table);

		control.add(control1); 
		control.add(Box.createVerticalStrut(20));
		control.add(control4);
		control.add(Box.createVerticalStrut(20));
		control.add(control3);
		control.add(Box.createVerticalStrut(20));
		control.add(control5);
		control.add(Box.createVerticalStrut(20));		
		control.add(scrollpane);

		
		for (Component comp : control.getComponents()) {
			((JComponent)comp).setAlignmentX(Box.LEFT_ALIGNMENT);
		}	
		
		for (Component comp:control1.getComponents()){
			if (comp instanceof JButton){
			    setButtonStyle((JButton) comp);
			    }
		}
		
		for (Component comp:control3.getComponents()){
			if (comp instanceof JButton){
			    setButtonStyle((JButton) comp);
			    }
		}
		
		for (Component comp:control5.getComponents()){
			if (comp instanceof JButton){
			    setButtonStyle((JButton) comp);
			    }
		}
		
		can.addMouseListener(new MouseListener(){			
            @Override	
            public void mousePressed(MouseEvent e) { 
               can.mousePressing(e);
               enableFont();
        	   int ID = can.getSelectedID();
			   if(isServer && ID != -1) { 
					srv.send("change", ID, can.selectedShape.getShapeModel());
			   }
           }
            
            @Override 
            public void mouseClicked(MouseEvent e){} 

            @Override
            public void mouseReleased(MouseEvent e) {
        	    can.reset();
           }
            
		   @Override
		   public void mouseEntered(MouseEvent e) {}
		   @Override
		   public void mouseExited(MouseEvent e) {}                              		
		});
		
		frame.add(control, BorderLayout.WEST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(800,500));
		frame.pack();
		frame.setVisible(true);
	}
	
	public void createListener() {	
		can.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				enableFont();
			    can.mouseDragging(e);
	        	int ID = can.getSelectedID();
				if(isServer && ID!=-1) { 
				   srv.send("change", ID, can.selectedShape.getShapeModel());
				}
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {				
			}		
		});
		
		recBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				can.addRect();
				int ID = can.getShapes().size()-1;
				DRectModel rm = (DRectModel) can.getShapes().get(can.getShapes().size()-1).sm;
				if(isServer) { 
					srv.send("add", ID, rm);
				}
			}		
		});
		
		ovalBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				can.addOval();
				int ID = can.getShapes().size()-1;
				DOvalModel om = (DOvalModel) can.getShapes().get(can.getShapes().size()-1).sm;
				if(isServer) { 
					srv.send("add", ID, om);
				}
			}	
		});
		
		lineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				can.addLine();
				int ID = can.getShapes().size()-1;
				DLineModel lm = (DLineModel) can.getShapes().get(can.getShapes().size()-1).sm;
				if(isServer) { 
					srv.send("add", ID, lm);
				}
			}
		});
		
		textBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				can.addText();
				int ID = can.getShapes().size()-1;
				DTextModel tm = (DTextModel) can.getShapes().get(can.getShapes().size()-1).sm;
				if(isServer) { 
					srv.send("add", ID, tm);
				}
			}		
		});
		
		SetColor.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(control1,
						"pick a color", Color.white);
				int ID = can.getSelectedID();
				if (can.selectedShape!=null) {
					can.selectedShape.getShapeModel().setColor(color);}
				can.repaint();
				if(isServer && ID != -1) { 
					srv.send("change", ID, can.selectedShape.getShapeModel());
				}
			}		
		});
		
		MoveToFront.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int ID = can.getSelectedID();
				DShape shape = can.MoveToFront();
				can.repaint();
				if(isServer && ID!=-1) { 
					srv.send("front", ID, shape.sm);
				}
			}		
		});
		
		MoveToBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int ID = can.getSelectedID();
				DShape shape = can.MoveToBack();
				can.repaint();
				if(isServer && ID!=-1) { 
					srv.send("back", ID, shape.sm);
				}
			}		
		});
		
		RemoveShape.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (can.selectedShape instanceof DText){
					text.setEnabled(false);
					font.setEnabled(false);}
				int ID = can.getSelectedID();
				DShape shape =can.removeShape();
				can.repaint();
				if(isServer && ID!=-1) { 
					srv.send("remove", ID, shape.sm);
				}
			}	
		});	
		
		text.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (can.selectedShape!=null && can.selectedShape instanceof DText){
					String t = text.getText();
					((DText) can.selectedShape).setText(t);
					can.repaint();
					int ID = can.getSelectedID();
					if(isServer && ID!=-1) { 
						   srv.send("change", ID, can.selectedShape.getShapeModel());
					}
				}
			}			
		});
		
		font.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (can.selectedShape!=null && can.selectedShape instanceof DText){
					String f = (String) font.getSelectedItem();
					((DText) can.selectedShape).setFont(f);
					can.repaint();
					int ID = can.getSelectedID();
					if(isServer && ID!=-1) { 
						   srv.send("change", ID, can.selectedShape.getShapeModel());
					}
				}			
			}
		});
		
		Server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane.showInputDialog("Run server on port", "39587");
				if (result!=null) {
					try {
						System.out.println("server: start");
						srv = new Server(Integer.parseInt(result.trim()));
						srv.start();
						isServer = true;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setStatus("server mode");
				}
			}
		});
		
		Client.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane.showInputDialog("Connect to host:port", "127.0.0.1:39587");
		        if (result!=null) {
		            String[] parts = result.split(":");
		            System.out.println("client: start");
					try {
						Client clt = new Client(parts[0].trim(), Integer.parseInt(parts[1].trim()), done);
						clt.getClientWB().setStatus("client mode");
						clt.getClientWB().enableClient();
						//Thread.sleep(100);
						clt.start();						
						try {
							done.acquire(1);
							
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						srv.send("clear", 0, null);
						for (int i=0;i<can.getShapes().size();i++){
							DShapeModel sm=can.getShapes().get(i).getShapeModel();							
							srv.send("add",i,sm);
						}

				} catch (IOException e1) {
					e1.printStackTrace();
				}
		      }
		   }
		});
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu netMenu = new JMenu("Network");
		
		save = new JMenuItem("save file");
		open = new JMenuItem("open file");
		saveImage = new JMenuItem("save to png");
		
		Server = new JMenuItem("Server Start");
		Client = new JMenuItem("Client Start");
		
		fileMenu.add(save);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(saveImage);
		
		netMenu.add(Server);
		netMenu.add(Client);
			
		menuBar.add(fileMenu);
		menuBar.add(netMenu);
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		saveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));
		netMenu.setMnemonic(KeyEvent.VK_N);
		Server.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		Client.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		
		for (Component comp:menuBar.getComponents()){
			comp.setForeground(new Color(59, 89, 182));
			comp.setFont(new Font("Tahoma", Font.BOLD, 13));
		}
		
		for (Component comp:fileMenu.getMenuComponents()){
			comp.setForeground(new Color(59, 89, 182));
			comp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		
		for (Component comp:netMenu.getMenuComponents()){
			comp.setForeground(new Color(59, 89, 182));
			comp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane.showInputDialog("File Name", null);
                if (result != null) {
                    File f = new File(result);
                    save(f);
                }			
			}		
		});
		
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!isClient && !isServer) {
	                String result = JOptionPane.showInputDialog("File Name", null);
	                if (result != null) {
	                    File f = new File(result);
	                    open(f);
	                }	
				}
			}		
		});
		
		saveImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane.showInputDialog("File Name", null);
                if (result != null) {
                    File f = new File(result);
                    saveImage(f);
                }
            }									
		});		
		return menuBar;
	}
	
    private void save(File file) {
        try {
        XMLEncoder xmlOut = new XMLEncoder(
            new BufferedOutputStream(
            new FileOutputStream(file)));
            DShapeModel[] ModelArray = new DShapeModel[can.getShapes().size()];
            for (int i=0;i<can.getShapes().size();i++){
            	ModelArray[i] = can.getShapes().get(i).getShapeModel();
            }
            xmlOut.writeObject(ModelArray);
            xmlOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void open(File file) {
        DShapeModel[] ModelArray = null;
        try {
            XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(new FileInputStream(file))); 
            ModelArray = (DShapeModel[]) xmlIn.readObject();
            xmlIn.close();
            can.clear();
            for(DShapeModel sm:ModelArray) {
                can.addShape(sm);
            }           
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImage(File file) { 
    	can.clearKnobs();
        BufferedImage bi = new BufferedImage(can.getWidth(), can.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        can.paintAll(g);
        g.dispose();
        try {
            javax.imageio.ImageIO.write(bi, "PNG", file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
    
    public canvas getCanvas() {
    	return can;
    }
    
    public void setStatus(String str) {
    	status.setText(str);
    }   
    
    public void enableClient() {
    	isClient =true;
    }
    	
	private void enableFont(){
		if (can.selectedShape instanceof DText){
				text.setEnabled(true);
				font.setEnabled(true);
				text.setText(((DTextModel) can.selectedShape.sm).getText());
				font.setSelectedItem(((DTextModel) can.selectedShape.sm).getFont());
		}
		else {
			text.setEnabled(false);
			font.setEnabled(false);
			text.setText(" ");
			font.setSelectedIndex(0);
		}	
	}
	
	private void setButtonStyle(JButton button){
		button.setBackground(new Color(230, 238, 255));
        button.setForeground(new Color(255, 128, 168));
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setOpaque(true);
	}
	
	private void setTableStyle(JTable t){
		t.setFont(new Font("Tahoma", Font.PLAIN, 12));
		t.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
		t.getTableHeader().setForeground(new Color(59, 89, 182));
		t.getTableHeader().setBackground(new Color(255, 230, 238));
		t.setRowHeight(20);
		t.setBackground(new Color(230, 238, 255));
	}
	
	public static void main(String[] args) {
		WhiteBoard WB = new WhiteBoard();
		WB.showGUI();
		WB.createListener();
	}

	
	
}
