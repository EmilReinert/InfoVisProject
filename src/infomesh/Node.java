package infomesh;

import java.awt.Color;

public class Node {
	double x;// year
	double y;// age
	private double z;// life expectancy
	double a;//4th dimension color value
	private Vec2 position;//2D position in diagram frame
	
	public Node(double x, double y, double z) {
		this.x = x;
		this.z = z;
		this.y = y;
		this.a = Color.WHITE.getRGB();//default node color value
		this.position = new Vec2(0,0);
	}
	
	public Node(double x, double y, double z, int a) {
		this.x = x;
		this.z = z;
		this.y = y;
		this.a = a;//not in use yet
		this.position = new Vec2(0,0);
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y =y;
	}
	public void setPosition(Vec2 v) {
		position = v;
	}
	public Vec2 getPosition() {
		return position;
	}
	public double getZ() {
		return z;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public Vec2 getXY() {
		return new Vec2(x,y);
	}
	public void setZ(double nz) {
		z = nz;
	}
	public double getA() {
		return a;
	}
	public String toString() {
		return "Year: "+x+", Age: "+y+" Value: "+z+" Relation: "+Math.round(a * 100.0) / 100.0;
	}
}
