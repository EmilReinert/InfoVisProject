package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.css.Rect;

public class Button {
	Rectangle2D shape;
	String name;
	boolean clicked;
	Color select;Color unselect;
	
	
	public Button(int x, int y, String n, Color select, Color unselect) {
		shape = new Rectangle2D.Double(x,y,20,20);
		clicked = false;
		name = n;
		this.select =select;
		this.unselect =unselect;
	}

	public Button(int x, int y, String n) {
		shape = new Rectangle2D.Double(x,y,20,20);
		clicked = false;
		name = n;
		select =Color.GRAY;
		unselect =Color.LIGHT_GRAY;
	}
	
	public void paint(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.fill(shape);
		if(clicked)g2.setColor(select);
		else g2.setColor(unselect);
		
		Rectangle2D small = new Rectangle2D.Double(shape.getMinX()+2,shape.getMinY()+2,16,16);
		g2.fill(small);
		
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
