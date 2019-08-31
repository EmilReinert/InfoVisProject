package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Button {
	Rectangle2D shape;
	String name;
	boolean clicked;
	
	public Button(Rectangle2D rec, String n, boolean b) {
		shape = rec;
		name = n;
		clicked = b;
	}
	public Button(int x, int y, String n) {
		shape = new Rectangle2D.Double(x,y,20,20);
		clicked = false;
		name = n;
	}
	
	public void paint(Graphics2D g2) {
		if(clicked)g2.setColor(Color.GRAY);
		else g2.setColor(Color.LIGHT_GRAY);
		g2.fill(shape);
		
		g2.setColor(Color.BLACK);
		g2.drawString(name, (float)shape.getMinX()+25, (float)shape.getMinY()+15);

	}
	
	public void click() {
		if(clicked) clicked = false;
		else clicked = true;
	}
	
	public boolean isInside(Vec2 pos) {
		return shape.contains(pos.x,pos.y);
	}
}
