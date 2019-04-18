package GUI;
import java.beans.XMLDecoder;
import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;

import javax.swing.SwingUtilities;

public class Client extends Thread {
	
	private WhiteBoard clientWB;
	private Socket toServer; 
	protected static final int CAP = 100000;
	private Semaphore isdone;
	
	public Client(String address, int port, Semaphore isdone) throws UnknownHostException,IOException{
		clientWB = new WhiteBoard();
        clientWB.showGUI();
        toServer = new Socket(address, port);
        this.isdone = isdone;
	}
	
	public Client() throws UnknownHostException, IOException{
		clientWB = new WhiteBoard();
        clientWB.showGUI();
		toServer = new Socket(InetAddress.getLocalHost(),4444);
	}
	
	public void receive() {
		try {
			ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
	        System.out.println("client: connected!");
	        isdone.release();
	        while (true) {
	        	String verb =(String) in.readObject();
	        	int ID = (Integer) in.readObject();
	            String xmlString = (String) in.readObject();
	            XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
	            DShapeModel sm= (DShapeModel) decoder.readObject();
	            //ClientAction(verb, ID, sm);
	            invoketoGui(verb, ID, sm); 
	        }
		}catch (Exception ex) { // IOException and ClassNotFoundException
                ex.printStackTrace();
        }          
	}
	
	private void invoketoGui(String verb, int ID, DShapeModel sm){
		final String verb_tmp = verb;
		final int ID_tmp = ID;
		final DShapeModel sm_tmp = sm;
		SwingUtilities.invokeLater( new Runnable() {
	          public void run() {
	              ClientAction(verb_tmp,ID_tmp,sm_tmp);
	          }
	      });
	}
	
	private void ClientAction(String verb, int ID, DShapeModel sm) {
		try {
		if(verb.equals("clear")){
			clientWB.getCanvas().clear();
		}
		
		if(verb.equals("add")) {
			clientWB.getCanvas().addShape(sm);
			clientWB.getCanvas().repaint();	    
		}
		
		if(verb.equals("remove")) {
			clientWB.getCanvas().removeShape(ID);
			clientWB.getCanvas().repaint();
		}
			
		if(verb.equals("front")) {
			clientWB.getCanvas().MoveToFront(ID);
			clientWB.getCanvas().repaint();
		}
			
		if(verb.equals("back")) {
			clientWB.getCanvas().MoveToBack(ID);
			clientWB.getCanvas().repaint();
		}
			
		if(verb.equals("change")) {
			clientWB.getCanvas().getShapes().get(ID).sm.mimic(sm);
			clientWB.getCanvas().repaint();
		}
		} catch (Exception ex) { 
            ex.printStackTrace();
            Thread running = Thread.currentThread();
            System.out.println(running.getName());
    }          
		
	}
	
	public WhiteBoard getClientWB() {
		return clientWB;
	}
	
	public void run() {
       this.receive(); 
       
	}
}