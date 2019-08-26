package infomesh;

public class Vec2 {
	double x;
	double y;
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getDistance(Vec2 v2) {
		// distance from self to v2 connection vector
		Vec2 ba = new Vec2(v2.x-x,v2.y-y);
		return Math.sqrt(ba.x*ba.x+ba.y*ba.y);
	}
}
