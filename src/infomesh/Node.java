package infomesh;

public class Node {
	double x;// year
	double y;// age
	double z;// life expectancy
	double a;//4th dimension color value
	Vec2 position;//2D position in diagram frame
	
	public Node(double x, double y, double z) {
		this.x = x;
		this.z = z;
		this.y = y;
		this.a = 0;//not in use yet
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
}
