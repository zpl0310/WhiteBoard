package GUI;

import java.beans.XMLEncoder;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Server extends Thread {
	
	private ServerSocket serverSocket;
    private java.util.List<ObjectOutputStream> outputs =
            new ArrayList<ObjectOutputStream>(); 
	
	public Server(int port) throws IOException{
		serverSocket = new ServerSocket(port);
	}
	
	public Server() throws IOException{
		serverSocket = new ServerSocket(4444);
	}
	
    public synchronized void addOutput(ObjectOutputStream out) {
        outputs.add(out);
    }
	
	public synchronized void send(String verb, int ID, DShapeModel sm) {
		OutputStream memStream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(memStream);
        encoder.writeObject(sm);
        encoder.close();
        String xmlString = memStream.toString();
        // Now write that xml string to all the clients.
        Iterator<ObjectOutputStream> it = outputs.iterator();
        while (it.hasNext()) {
            ObjectOutputStream out = it.next();
            try {
            	out.writeObject(verb);
            	out.writeObject(ID);
                out.writeObject(xmlString);
                out.flush();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                it.remove();
            }
        }
	}
	
	
	public void run() {
        //InetAddress hostIP;
		try {
			while (true) {
				Socket toClient = null;
				toClient = serverSocket.accept();
				System.out.println("server: got client");
				addOutput(new ObjectOutputStream(toClient.getOutputStream()));
			}
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}
	}
}