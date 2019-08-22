package infomesh;

import java.io.File;
import java.util.ArrayList;

public class Model {
	// Data
	private ArrayList<Node> raw_nodes;// node list
	private Node[][] nodes; //node mapping
	
	Range rangeX;//min and max ranges of dimension
	Range rangeY;
	Range rangeZ;
	int dimX; // length of x data = amount of x vertices 
	int dimY; 
	
	private CoSystem co;
	
	
	public Model(File f, CoSystem co) {
		raw_nodes = new ArrayList<>();
		this.co = co;
		loadRawExNodes(f);//loadRawNodes(f); // get raw node data
		loadRanges(); // load all min max ranges for X Y Z
		System.out.println(rangeX.toString());System.out.println(rangeY.toString());System.out.println(rangeZ.toString());
		loadDimensions(); // create X Y plane dimensions
		adjustValues(); // adjust z to be percentage values 
		makeNodes(); // map raw nodes to X Y plane 

	}


	public void loadRawExNodes(File f) {
		//9 example nodes
		raw_nodes.add(new Node(1995,1,0.5));
		raw_nodes.add(new Node(1995,2,0.4));
		raw_nodes.add(new Node(1995,3,0.45));
		raw_nodes.add(new Node(1996,1,0.5));
		raw_nodes.add(new Node(1996,2,0.35));
		raw_nodes.add(new Node(1996,3,0.36));
		raw_nodes.add(new Node(1997,1,0.5));
		raw_nodes.add(new Node(1997,2,0.37));
		raw_nodes.add(new Node(1997,3,0.4));
	}
	
	public void loadRawNodes(File f) {
		
	}
	
	public void loadRanges() {
		// calculating min and max ranges for XYZ from node data
		double min,max;
		min = 100000000; max = -100000000;
		//X ranges
		for(Node n: raw_nodes) {
			if(n.x<min)min = n.x;
			if(n.x>max)max = n.x;
		}
		rangeX = new Range(min,max);
		min = 100000000; max = -100000000;
		//Y ranges
		for(Node n: raw_nodes) {
			if(n.y<min)min = n.y;
			if(n.y>max)max = n.y;
			
		}
		rangeY = new Range(min,max);
		min = 100000000; max = -100000000;
		//Z ranges
		for(Node n: raw_nodes) {
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
		double X = raw_nodes.get(0).x;
		double Y = raw_nodes.get(0).y;
		for(Node n: raw_nodes) {
			if (n.x==X)countX++;
			if(n.y==Y)countY++;
		}
		//test if findings make sense
		if ((countX*countY)!=raw_nodes.size())System.err.println("The given Data violates the required dimensions for a rectangle XY plane");
		else {
			dimX = countY;
			dimY = countX;
		}
	}
	
	public void adjustValues() {
		// adjust z to be percentage values 
		//based on ranges
		double span = rangeZ.getDiff();
		double min = rangeZ.getMin();
		for(Node n: raw_nodes) {
			n.z = (n.z-min)/span;// relative span values
		}
	}

	public void makeNodes() {
		//raw to mapped nodes
		//TODO handle possible errors
		nodes = new Node[this.dimX][this.dimY];
		int dimx =0,dimy =0;
		for(int i = 0;i<raw_nodes.size();i++) {
			//System.out.println(raw_nodes.get(i).z+" "+dimx+" "+dimy);
			nodes[dimx][dimy]=raw_nodes.get(i);
			dimy++;
			if(dimy>=this.dimY) {dimy=0;dimx++;}
		}
	}
	
	
	public int getZData(int x, int y) {
		// adjust field and multiply relavtive value by z axis length
		return (int) ((nodes[x][y].z*co.getField()+((1-co.getField())/2))*co.getOrigin().x);
	}
	
	public void changeFiel(double in) {
		
	}
	
	public Node getNode(int x, int y) {
		return nodes[x][y];
	}
}
