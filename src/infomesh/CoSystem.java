package infomesh;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class CoSystem implements MouseListener,MouseMotionListener,MouseWheelListener{
	private Vec2 origin;
	private Vec2 position;
	private int height, width;
	private double field; //Z covered area in percent -> |///|field|///| 
	
	public CoSystem(Vec2 o, int h, int w, double f) {
		origin = o;
		height=h;
		width=w;
		field = f;
	    position = new Vec2(0,0);
	}
	
	public Vec2 getOrigin() {
		return origin;
	}
	
	public double getField() {
		return field;
	}

	public Vec2 getPosition() {
		return position;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getY();
		int y =e.getX();
		if(x>0&&x<height)origin.x=x;
		if(y>0&&y<width)origin.y=y;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getY();
		int y =e.getX();
		if(x>0&&x<height)position.x=x;
		if(y>0&&y<width)position.y=y;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
		field += 0.01*e.getPreciseWheelRotation();
		if(field>0.9)field = 0.9;
		if(field<0)field = 0;
	}
}
