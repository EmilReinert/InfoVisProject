package infomesh;

import java.io.File;
import java.util.ArrayList;

public class Model {
	// Data
	ArrayList<Node> nodes;
	Range rangeX;//min and max ranges of dimension
	Range rangeY;
	Range rangeZ;
	int dimX; // length of x data = amount of x vertices 
	int dimY; 
	float field = 0.2f; //Z covered area in percent -> |///|field|///| 
	
	
	public Model(File f) {
		nodes = new ArrayList<>();
		loadNodes(f);
		loadRanges();
		System.out.println(rangeX.toString());System.out.println(rangeY.toString());System.out.println(rangeZ.toString());
		loadDimensions();
	}
	
	
	public void loadNodes(File f) {
		//9 example nodes
		// NEEDS TO BE SORTED
		//TODO
		nodes.add(new Node(1995,1,0.5));
		nodes.add(new Node(1995,2,0.4));
		nodes.add(new Node(1995,3,0.45));
		nodes.add(new Node(1996,1,0.4));
		nodes.add(new Node(1996,2,0.35));
		nodes.add(new Node(1996,3,0.36));
		nodes.add(new Node(1997,1,0.5));
		nodes.add(new Node(1997,2,0.37));
		nodes.add(new Node(1997,3,0.4));
	}
	
	public void loadRanges() {
		// calculating min and max ranges for XYZ from node data
		double min,max;
		min = 100000000; max = -100000000;
		//X ranges
		for(Node n: nodes) {
			if(n.x<min)min = n.x;
			if(n.x>max)max = n.x;
		}
		rangeX = new Range(min,max);
		min = 100000000; max = -100000000;
		//Y ranges
		for(Node n: nodes) {
			if(n.y<min)min = n.y;
			if(n.y>max)max = n.y;
			
		}
		rangeY = new Range(min,max);
		min = 100000000; max = -100000000;
		//Z ranges
		for(Node n: nodes) {
			if(n.z<min)min = n.z;
			if(n.z>max)max = n.z;
		}
		rangeZ = new Range(min,max);
	}
	
	public void loadDimensions(){
		//calculating Dimensions aka step length for xy plane cuts
		// count the amount of same x appearances for y dim and vice versa
		// there are no two same x,y pairs -> otherwise throws exception
		int countX=0;
		int countY=0;
		double X = nodes.get(0).x;
		double Y = nodes.get(0).y;
		for(Node n: nodes) {
			if (n.x==X)countX++;
			if(n.y==Y)countY++;
		}
		//test if findings make sense
		if ((countX*countY)!=nodes.size())System.err.println("The given Data violates the required dimensions for a rectangle XY plane");
		else {
			dimX = countY;
			dimY = countX;
		}
	}
}
