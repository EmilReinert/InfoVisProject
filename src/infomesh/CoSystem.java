package infomesh;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CoSystem implements MouseListener,MouseMotionListener,MouseWheelListener{
	private Vec2 origin; // coord origin
	private Vec2 position;// mosue position
	Vec2 lastclick; // last position mouse was clicked

	private int time =0;
	boolean enable=true;
	boolean disable=false;
	
	private int height, width;
	private double field; //Z covered area in percent -> |///|field|///| 

	public CoSystem(int h, int w) {
		height=h;
		width=w;
		origin=new Vec2(width/2,height/6);
		field = 0.5;
	    position = new Vec2(1,1);
	    lastclick = new Vec2(-100,-100);
	}
	
	public Vec2 getOrigin() {
		return origin;
	}
	
	public double getField() {
		return field;
	}

	public Vec2 getMouse() {
		return position;
	}
	
	public Vec2 getClick() {
		return lastclick;
	}
	public Vec2 getClickInv() {
		//inverse click for data visu
		return new Vec2(lastclick.y,lastclick.x);
	}
	
	public void reset() {
		origin=new Vec2(width/2,height/6);
		field = 0.5;
	    position = new Vec2(1,1);
	    lastclick = new Vec2(-100,-100);
	}
	
	/// specials ///
	public void startUp() {
		if(enable) {
			if(time<100){
				origin.x+=3;
				origin.y+=1;
				field+=0.005;
				time++;
			}
		}
	}
	
	public void end() {
		if(disable) {
			if(time>0){
				origin.x-=3;
				origin.y-=1;
				field-=0.005;
				time--;
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//for INFOBOX so normal xy coords
		//maybe line selection
		int y = (int) (e.getY()+Math.random()*4); //random so we dont have the same click twice
		int x =(int) (e.getX()+Math.random()*4); // i mean its less likely 
		if(x>0)lastclick.x=x;
		if(y>0)lastclick.y=y;
		if(SwingUtilities.isRightMouseButton(e)) {lastclick = new Vec2(0,0);position= new Vec2(0,0);System.out.println("reset");}
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
		// for origin moving
		if(enable) {
			int x = e.getY();
			int y =e.getX();
			if(x>0&&x<height)origin.x=x;
			if(y>0&&y<width)origin.y=y;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//for point selection
		int x = e.getY();
		int y =e.getX();
		if(x>0&&x<height)position.x=x;
		if(y>0&&y<width)position.y=y;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
		field += 0.03*e.getPreciseWheelRotation();
		if(field>0.9)field = 0.9;
		if(field<0)field = 0;
	}
}
