package infomesh;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Model implements KeyListener{
	//Files Copy
	File file;
	File male;
	File female;
	
	// Data
	private ArrayList<Node> raw_nodes= new ArrayList<>();// node list
	private Node[][] nodes; //raw nodes after mapping
	private int Z_IND = 6; // index for z value in database (total amount/expectations..)
	
	Range rangeX;//min and max ranges of dimension
	Range rangeY;
	Range rangeZ;
	
	int dimX; // length of x data = amount of x vertices 
	int dimY; 
	
	private CoSystem co;
	boolean magnifyColor = true;
	
	
	public Model(File f, CoSystem co) {
		// Constructor for example nodes
		file = f;this.co = co;
		loadRawExNodes(f);
		loadRanges();
		loadDimensions();
		makeNodes();
	}
	
	public Model(File f, File male, File female, CoSystem co) {
		file = f;
		this.male = male;
		this.female = female;
		this.co = co;
		loadRawNodes(f); // get raw node data
		loadRanges(); // load all min max ranges for X Y Z
		//System.out.println(rangeX.toString());System.out.println(rangeY.toString());System.out.println(rangeZ.toString());
		loadDimensions(); // create X Y plane dimensions
		
		ArrayList<Double> mf_rels = loadMFNodes(male, female); // generate realative value for nodes
		makeColors(mf_rels); // assign relative values as color to rawnodes
		
		makeNodes(); // map raw nodes to X Y plane 

	}
	
	void rebuild(){
		raw_nodes= new ArrayList<>();
		loadRawNodes(file); // get raw node data
		loadRanges(); // load all min max ranges for X Y Z
		//System.out.println(rangeX.toString());System.out.println(rangeY.toString());System.out.println(rangeZ.toString());
		loadDimensions(); // create X Y plane dimensions
		
		ArrayList<Double> mf_rels = loadMFNodes(male, female); // generate realative value for nodes
		makeColors(mf_rels); // assign relative values as color to rawnodes
		
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
		String [] data_line;
		double x, y, z; // data for one node
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line =reader.readLine();
			while (line!=null) {
				// HANDLE LINE
				data_line = line.split("	");
				
				// change "110+" to "110"
				if(data_line[1].length()>3) data_line[1]="110";

				x = Double.parseDouble(data_line[0]);
				y = Double.parseDouble(data_line[1]);
				z = Double.parseDouble(data_line[Z_IND]);
				line = reader.readLine();

				// create and add node
				//if(x>2012&&y<6)
				raw_nodes.add(new Node(x,y,z));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			if(n.getZ()<min)min = n.getZ();
			if(n.getZ()>max)max = n.getZ();
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
	
	public ArrayList<Double> loadMFNodes(File male, File female) {
		// loads the file of male and female values to obtain relative values
		ArrayList<Double>  male_z = new ArrayList<>();// zvalue list
		ArrayList<Double> female_z=new ArrayList<>();// zvalue list
		String [] data_line;
		double z; // actual value
		//MALE
		try {
			BufferedReader reader = new BufferedReader(new FileReader(male));
			String line =reader.readLine();
			while (line!=null) {
				// HANDLE LINE
				data_line = line.split("	");
				z = Double.parseDouble(data_line[Z_IND]);
				line = reader.readLine();
				male_z.add(z);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//FEMALE
		try {
			BufferedReader reader = new BufferedReader(new FileReader(female));
			String line =reader.readLine();
			while (line!=null) {
				// HANDLE LINE
				data_line = line.split("	");
				z = Double.parseDouble(data_line[Z_IND]);
				line = reader.readLine();
				female_z.add(z);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//// loading relative values for raw nodes
		return relMFValues(male_z,female_z);
	}
	
	private ArrayList<Double> relMFValues(ArrayList<Double> male_z, ArrayList<Double> female_z) {
		// calculating relative values to 2 lists of different values from first to second list
		ArrayList<Double> rels= new ArrayList<>();
		// 
		double rel; // relative value holder 
		for(int i = 0;i<male_z.size();i++) {
			// calculating relative value
			rel = male_z.get(i)/(male_z.get(i)+female_z.get(i));
			rels.add(rel);
		}
		return rels;
	}
	
	private void makeColors(ArrayList<Double> rels) {
		//make and assign relative values as color to rawnodes
		
		//range for relative values
		double min,max;
		min = 1; max = 0;
		for(double r: rels ) {
			if(r<min)min = r;
			if(r>max)max = r;
		}
		Range mf_range = new Range(min, max);
		
		double r; // value holder
		int color; // color holder
		for(int i = 0; i<rels.size();i++) {
			// adjust according to min and max value
			r = rels.get(i);
			
			if(magnifyColor)r = (r-min)/mf_range.getDiff(); // magnify difference 
			
			//calculating color from relative value
			color = Color.HSBtoRGB((float) r, 0, 1-(float) r);
			
			//assigning color to node !
			raw_nodes.get(i).a = color;
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
	
	
	public int getAdjustedZData(int x, int y) {
		//returns ADJUSTED VALUE		
		
		// adjust field and multiply relavtive value by z axis length
		double raw = nodes[x][y].getZ();
		double span = rangeZ.getDiff();
		double min = rangeZ.getMin();

		raw = (raw-min)/span;// relative span values
		
		return (int) ((raw*co.getField()+((1-co.getField())/2))*co.getOrigin().x);
	}
	
	public int getColor(int x, int y) {
		return nodes[x][y].getColor();
	}
	
	public void changeFiel(double in) {
		
	}
	
	public Node getNode(int x, int y) {
		//returns node on mesh mapping x and y
		return nodes[x][y];
	}
	
	public Node getActiveNode(Vec2 pos) {
		//returns node closest to position vector
		double min_distance = 10000; Node min_node =null;
		double distance;
		for(Node n: raw_nodes) {
			distance = pos.getDistance(n.getPosition());
			if(distance<min_distance) {
				min_node = n; min_distance = distance;
			}
		}
		return min_node;
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getKeyCode());
		if(e.getKeyCode()==50)Z_IND= 2;
		if(e.getKeyCode()==51)Z_IND= 3;
		if(e.getKeyCode()==52)Z_IND= 4;
		if(e.getKeyCode()==53)Z_IND= 5;
		if(e.getKeyCode()==54)Z_IND= 6;
		if(e.getKeyCode()==55)Z_IND= 7;
		if(e.getKeyCode()==56)Z_IND= 8;
		if(e.getKeyCode()==57)Z_IND= 9;
		rebuild();
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
