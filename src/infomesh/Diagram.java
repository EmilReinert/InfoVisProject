package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;
import java.time.OffsetDateTime;


public class Diagram {
 
	private int[] pixels;
	
	private int width, height; // width, height for diagram
	private Model model;
	private CoSystem cosystem;
	private boolean drawVertical = false;
	private boolean drawMesh =false;
	private Node hoverNode; // node closest to mouse
	private Vec2 selectedLine; // selected line x and y values
	Color bg_color;
	
	public Diagram(int w, int h, Model m, CoSystem co, Color bg_color) {
		pixels = new int[w*h];
		width = w;
		height = h;
		model = m;
		cosystem = co;
		this.bg_color = bg_color;
		hoverNode = new Node(0, 0, 0);
		selectedLine = new Vec2(-1,-1);
	}
	
	/// GET AND SETTER ///
	public void reset() {
		hoverNode = new Node(0, 0, 0);
		selectedLine = new Vec2(-1,-1);
		cosystem.reset();
		model.rebuild();
	}
	
	public int getHeight() {
		return height;
	}
	
	public Node getSelectedNode() {
		return hoverNode;
	}
	
	public CoSystem getCoord() {
		return cosystem;
	}
	
	public void setMeshMode(boolean b) {
		drawMesh = b;
	}
	
	public void setVerticalsMode(boolean b) {
		drawVertical = b;
	}
	
	public void setModelMode(int i) {
		model.setZInd(i);
	}
	public void setColorMode(boolean b) {
		model.magnifyColor =b;
	}
	
	/// GEOMETRIC FUNCTIONS ///
	public int vec2Int(Vec2 v) {
		return (int) ((v.x-1)*width+v.y);
	}
	
	public Vec2 int2Vec(int i) {
		int x =(i-i%width)/width;
		int y= i%width;
		return new Vec2(x, y);
	}
	
	public int getFPS() {
		if (selectedLine.x >0)return 5;
		else return 30;
	}
	
	public Vec2 getLinFunction(Vec2 first , Vec2 second) {
		// return m and t for a linear function f = m*x+t with  function a-> b
		//Greradenfunktion
		double m; //STEIGUNG
		if(first.y>second.y)m=-1;
		else m = 1;
		
		if(second.x-first.x!=0)
			m*=Math.abs((second.y-first.y)/(second.x-first.x));
		double t= first.y-first.x*m; //x verschiebung		
		return new Vec2(m, t);
	}
	
	
	/// DRAW FUNCTIONS ///
	public void drawPoint(Vec2 a, int color) {
		drawPoint(pixels,vec2Int(a),color);
	}
	
	
	public void drawPoint(int[] pixels, int a, int color) {
		// draws 6*6 pixels point
		if(a>width+1)pixels[a-width-1]=pixels[a-width]=pixels[a-width+1]=color;
		if(a>1)pixels[a-1]=pixels[a]=pixels[a+1]=color;
		pixels[a+width-1]=pixels[a+width]=pixels[a+width+1]=color;
	}
	

	public void drawLine(int a, int b, int color) {
		// draw line for int values
		Vec2 vec_a = int2Vec(a);
		Vec2 vec_b = int2Vec(b);
		drawLine(vec_a, vec_b, color);
	}
	
	public void drawLine(Vec2 a, Vec2 b,int color ) {
		//draws line on screen by manipulating pixel values
		Vec2 first,second;//System.out.println(a.x+" "+b.x);
		if (a.x<b.x) {first = a;second=b;}
		else {first=b;second=a;}
		
		//Greradenfunktion
		Vec2 mt = getLinFunction(first, second);
		double m = mt.x;
		double t = mt.y;
		float x,y,f_x;
		if(Math.abs(m)<1) {
			for(int n=0; n<pixels.length; n++) {
				x = (n-n%width)/width;
				y = n%width;
				f_x = (float) (m*x+t);
				//line starts
				if (x>=first.x&&x<=second.x) {
					if((int)y==(int)f_x) {pixels[n]=color;}
				}
			}	
		}
		else {
			// inverse line drawing
			if (a.y<b.y) {first = a;second=b;}
			else {first=b;second=a;}
			//steigung
			if(first.x>second.x)m=-1;
			else m = 1;
			m*=Math.abs((second.x-first.x)/(second.y-first.y));
			t= first.x-first.y*m; //x verschiebung
			
			for(int n=0; n<pixels.length; n++) {
				x = (n-n%width)/width;
				y = n%width;
				f_x = (float) (m*y+t);
				if (y>=first.y&&y<=second.y) {
					if((int)x==(int)f_x) {pixels[n]=color;}
				}
			}						 	
		}
	}
	
	
	public void fillQuad( Quad q, int rgb) {
		// fill a quad with given edge points 
		
		//find 
	}
	

	/// DATA VISULIZATION ///
	
	public void drawData() {
		//drawing of points at 3d diagram positions
		
		//bottom plane starting point z = 0
		int pix_start = 0,pix_end =0;
		Vec2 origin = cosystem.getOrigin(); // coordinate systems origin
		
		
		/// steps in xy plane
		//for x step we need the x axis function
		int x_step = (int) (((height-origin.x)/(model.dimX-1)));
		int y_step = (int) ((width-origin.y)/(model.dimY-1));
		// x offset from diagonal axis
		double x_off,x; double f_x;
		Vec2 first = origin;
		Vec2 second = new Vec2(height,0);
		double m = -1*(second.y-first.y)/(second.x-first.x);
		double t= first.y-first.x*m; //x verschiebung 
		
		
		//looping throufh X-Y dependent dimensions
		for (int dim_x = 0; dim_x<model.dimX; dim_x++) {
			for (int dim_y = 0; dim_y < model.dimY;dim_y++) {
			/// Drawing Data Nodes
				
				//determine pix start
				pix_start = vec2Int(origin)+ dim_x*x_step*width+dim_y*y_step;
				x= (pix_start-pix_start%width)/width;
				f_x = m*x+t;
				x_off = origin.y-f_x;
				pix_start+= x_off;
				// small correcting for edgecases
				if(dim_x==model.dimX-1&&dim_y==0)pix_start+=4;
				if(dim_x==0&&dim_y==model.dimY-1)pix_start-=4;
				
				//determine pix end
				
				//takes relative z data at given position and multiplies by z axis length
				pix_end = pix_start-model.getAdjustedZData(dim_x,dim_y)*width;
				
				drawPoint(pixels, pix_end,model.getColor(dim_x, dim_y));
				
				if(pix_end<0) {//pixels out of frame
					pix_end=0; System.out.println("attention, some pixel values are outside of the coordinate system");}

			/// sending coordinate data to each node
				model.getNode(dim_x,dim_y).setPosition(int2Vec(pix_end));
				
			/// drawing horizontal height lines
				if(drawVertical)drawLine(pix_start, pix_end+width, model.getColor(dim_x, dim_y));
			}
		}
	}
	
	public void drawMesh( int color) {
		//drawing Meshgrid with hightlighted line
		Node n; //node holder
		
		//looping throufh X-Y dependent dimensions
		for (int dim_x = 0; dim_x<model.dimX; dim_x++) {
			for (int dim_y = 0; dim_y < model.dimY;dim_y++) {
				n = model.getNode(dim_x, dim_y);
				if(drawMesh||n.getX()== selectedLine.x) {
					//draw line to neighbor node
					if(dim_y<model.dimY-1)
						if(n.getX()== selectedLine.x)
							drawLine(n.getPosition(), model.getNode(dim_x, dim_y+1).getPosition(), Color.RED.getRGB());
						else 
							drawLine(n.getPosition(), model.getNode(dim_x, dim_y+1).getPosition(), color);
				}
				
				// bottom right neighbor node
				if (dim_x ==0&&dim_y==0)//if(dim_x<model.dimX-1&&dim_y<model.dimY-1)
				fillQuad(new Quad ( model.getNode(dim_x, dim_y).getPosition(),model.getNode(dim_x, dim_y+1).getPosition(),
						model.getNode(dim_x+1, dim_y+1).getPosition(),model.getNode(dim_x+1, dim_y).getPosition()),color);
			}
		}
		
		//looping throufh Y-X dependent dimensions
		for (int dim_y = 0; dim_y<model.dimY; dim_y++) {
			for (int dim_x = 0; dim_x < model.dimX;dim_x++) {
				n = model.getNode(dim_x, dim_y);
				if(drawMesh||n.getY()== selectedLine.y) {
					// bottom neighbor node
					if(dim_x<model.dimX-1)
						if(n.getY()== selectedLine.y)
							drawLine(n.getPosition(), model.getNode(dim_x+1, dim_y).getPosition(), Color.RED.getRGB());
						else 
							drawLine(n.getPosition(), model.getNode(dim_x+1, dim_y).getPosition(), color);
				}
			}
		}
	}
	
	public void drawColors() {
		// draws color wheel for relative values on last 2 lines
		double r;
		for (int i = 0; i< width;i++) {
			r = 1-(double)i/width;
			pixels[width*height-width+i]=
					pixels[width*height-width-width+i]=
							Color.HSBtoRGB((float)(r/3)+0.55f, 0.7f,0.7f);
		}
	}
	

	
	/// MAIN UPDATE LOOP ///
	public int[] update(int[] p) {
		this.pixels = p;
		//draw background
		for(int n=0; n<pixels.length; n++) {
			//if(pixels[n]!= Color.RED.getRGB())
				pixels[n] = bg_color.getRGB();// Draws line in bg
		}
		//draw coord system
		Vec2 origin = cosystem.getOrigin();
		int origin_n = vec2Int(origin);
		for(int n=0; n<pixels.length; n++) {
			
			//Z AXis
			if(n%width==(int)origin.y&&n<origin_n) pixels[n]= Color.BLACK.getRGB();
			//Y	Axis		
			if(n>origin_n&&n/width<origin.x)pixels[n]=Color.BLACK.getRGB();
		}
		//draw origin
		pixels[origin_n]= Color.RED.getRGB();
		
		//X Axis
		drawLine(origin, new Vec2(height,0), Color.BLACK.getRGB());
		
		//draw DATA
		drawData();
		drawMesh( Color.BLUE.getRGB());
		
		// update active node
		if(hoverNode!=model.getActiveNode(cosystem.getMouse()))
			hoverNode = model.getActiveNode(cosystem.getMouse());

		// draw active node
		drawPoint(hoverNode.getPosition(),Color.RED.getRGB());
		
		
		// draw horizontal node line
		if(hoverNode.getPosition().getDistance(cosystem.getClickInv())<10) 
			selectedLine.make(hoverNode.getXY());
		
		
		// draw color wheel
		drawColors();
		
		// finally returns pixels
		return pixels;
	}


}
