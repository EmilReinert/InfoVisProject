package infomesh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public class InfoBox implements KeyListener {

	Color bg_color;
	int width, height; // width and height for full screen
	Diagram dia;  // holds info about diagram thus has to hold one
	CoSystem co; // can modify co system so it holds one
	Button drawVert;
	Button drawMesh;
	Button colorEnhance;
	Button resetMesh;

	
	Vec2 clickHolder = new Vec2(0, 0); // remembers last click to see if its changed
	
	public InfoBox(int w, int h, Diagram d,CoSystem c, Color bg) {
		bg_color = bg;
		height = h;
		width = w;
		co = c;
		dia = d;
		
		//Buttons
		drawVert = new Button(20, dia.getHeight()+60,"Draw Verticals");
		drawMesh = new Button(150, dia.getHeight()+60,"Draw Grid");
		colorEnhance = new Button(250, dia.getHeight()+60,"Enhance Colors");
		resetMesh = new Button(20,dia.getHeight()+100,"Reset",Color.RED,Color.GRAY);
		
	}
	
	public void paint(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    reset_box(g2);
	    
	    //Test text title
	    String str = "INFORMATION BOX";
	    g2.drawString(str,width/2 -50, dia.getHeight()+30);

	    //Selected Point
	    String point = "Hover Node: " + dia.getSelectedNode().toString();
	    g2.drawString(point, 20, dia.getHeight()+50);
	    
	    //manage buttons
	    updateButtons();
	    	//vertical lines draw
	    drawVert.paint(g2);
	    dia.setVerticalsMode(drawVert.clicked);
	    	// mesh draw
	    drawMesh.paint(g2);
	    dia.setMeshMode(drawMesh.clicked);
	    	// enhamce colors
	    colorEnhance.paint(g2);
	    dia.setColorMode(colorEnhance.clicked);
	    
	    	//reset mesh
	    resetMesh.paint(g2);
	    
	    
	    //g2.drawLine(0, 0, width, height);
	    
	}	
	
	public void reset_box(Graphics2D g2) {
		g2.setColor(bg_color);
	    g2.fillRect(0, 0, width*3, 3*height);
	    g2.setColor(Color.WHITE);
	    g2.fillRect(10, dia.getHeight()+10, width-20, height-dia.getHeight()-20);
		
		g2.setColor(Color.BLACK);
	}
	
	public void updateButtons() {
		//test if any button was clicked
		if(clickHolder.x==co.getClick().x&&clickHolder.y==co.getClick().y) {
			return;
		}
		// and check if a new click happened for each
		if(drawVert.isInside(co.getClick())) drawVert.click();
		if(drawMesh.isInside(co.getClick())) drawMesh.click();
		if(colorEnhance.isInside(co.getClick()))colorEnhance.click();
		if(resetMesh.isInside(co.getClick())) {
			dia.reset();
			drawVert = new Button(20, dia.getHeight()+60,"Draw Verticals");
			drawMesh = new Button(150, dia.getHeight()+60,"Draw Grid");
		}
		
		//  update clickholder
		clickHolder.x= co.getClick().x;
		clickHolder.y = co.getClick().y;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyCode());
		if(e.getKeyCode()==50)dia.setModelMode(2);
		if(e.getKeyCode()==51)dia.setModelMode(3);
		if(e.getKeyCode()==52)dia.setModelMode(4);
		if(e.getKeyCode()==53)dia.setModelMode(5);
		if(e.getKeyCode()==54)dia.setModelMode(6);
		if(e.getKeyCode()==55)dia.setModelMode(7);
		if(e.getKeyCode()==56)dia.setModelMode(8);
		if(e.getKeyCode()==57)dia.setModelMode(9);
		dia.reset();
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
