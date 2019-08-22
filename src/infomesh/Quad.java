package infomesh;

public class Quad {
	
	//edgepoints
	// A   B
	// D   C
	Vec2 a,b,c,d;
	
	public Quad(Vec2 v1, Vec2 v2, Vec2 v3, Vec2 v4) {
		//sort vecs to match abcd edges
		a=v1;b=v2;c=v3;d=v4;
		sortVecs();
	}

	private void sortVecs() {
		// TODO Auto-generated method stub
		
	}

}
