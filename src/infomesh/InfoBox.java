package infomesh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public class InfoBox {

	Color bg_color;
	int width, height; // width and height for full screen
	Diagram dia;  // holds info about diagram thus has to hold one
	CoSystem co; // can modify co system so it holds one
	Button drawMesh;
	
	Vec2 clickHolder = new Vec2(0, 0); // remembers last click to see if its changed
	
	public InfoBox(int w, int h, Diagram d,CoSystem c, Color bg) {
		bg_color = bg;
		height = h;
		width = w;
		co = c;
		dia = d;
		
		//Buttons
		drawMesh = new Button(20, dia.getHeight()+60,"Draw Mesh");
		
	}
	
	public void paint(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    reset_box(g2);
	    
	    //Test text title
	    String str = "INFORMATION BOX";
	    g2.drawString(str,width/2 -50, dia.getHeight()+30);

	    //Selected Point
	    String point = "Selected Node: " + dia.getSelectedNode().toString();
	    g2.drawString(point, 20, dia.getHeight()+50);
	    
	    //manage buttons
	    updateButtons();
	    drawMesh.paint(g2);
	    dia.setMeshMode(drawMesh.clicked);
	    
	    
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
		if(drawMesh.isInside(co.getClick())) {drawMesh.click();
		}
		//  update clickholder
		clickHolder.x= co.getClick().x;
		clickHolder.y = co.getClick().y;
	}
}
