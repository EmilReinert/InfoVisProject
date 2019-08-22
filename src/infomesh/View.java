package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;
import java.time.OffsetDateTime;


public class View {
 
	private int width, height;
	private Model model;
	private CoSystem cosystem;
	
	public View(int w, int h, Model m, CoSystem co) {
		width = w;
		height = h;
		model = m;
		cosystem = co;
	}
	
	public CoSystem getCoord() {
		return cosystem;
	}
	
	public int vec2Int(Vec2 v) {
		return (int) ((v.x-1)*width+v.y);
	}
	
	public Vec2 int2Vec(int i) {
		int x =(i-i%width)/width;
		int y= i%width;
		return new Vec2(x, y);
	}
	
	
	public void drawLine(int[]pixels,Vec2 a, Vec2 b,int color ) {
		//draws line on screen by manipulating pixel values
		Vec2 first,second;//System.out.println(a.x+" "+b.x);
		if (a.x<b.x) {first = a;second=b;}
		else {first=b;second=a;}
		
		//Greradenfunktion
		double m; //STEIGUNG
		if(first.y>second.y)m=-1;
		else m = 1;
		
		if(second.x-first.x!=0)
			m*=Math.abs((second.y-first.y)/(second.x-first.x));
		double t= first.y-first.x*m; //x verschiebung		
		///

		float x,y,f_x;
		if(Math.abs(m)<1) {
			for(int n=0; n<pixels.length; n++) {
				x = (n-n%width)/width;
				y = n%width;
				f_x = (float) (m*x+t);
				//line starts
				//System.out.println(first.x);
				if (x>=first.x&&x<=second.x) {//System.out.println(x);
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
				//line starts
				//System.out.println(first.x);
				if (y>=first.y&&y<=second.y) {//System.out.println(x);
					if((int)x==(int)f_x) {pixels[n]=color;}
				}
			}	
			
						 	
		}
	}
	
	public void drawLine(int[] pixels, int a, int b, int color) {
		Vec2 vec_a = int2Vec(a);
		Vec2 vec_b = int2Vec(b);
		drawLine(pixels, vec_a, vec_b, color);
	}
	
	public void drawData(int[] pixels) {
		//drawing of points at 3d diagram positions
		
		//bottom plane starting point z = 0
		int pix_start = 0,pix_end =0;
		Vec2 origin = cosystem.getOrigin(); // coordinate systems origin
		
		
		/// steps in xy plane
		//for x step we need the x axis function
		int x_step = (int) (((height-origin.x)/(model.dimX-1)));
		int y_step = (int) ((width-origin.y)/(model.dimY-1));
		// x offset from diagonal axis
		int x_off,x; double f_x;
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
				x_off = (int) (origin.y-f_x);
				pix_start+= x_off;
				// small correcting for edgecases
				if(dim_x==model.dimX-1&&dim_y==0)pix_start+=2;
				if(dim_x==0&&dim_y==model.dimY-1)pix_start-=2;
				if(pix_start<pixels.length)
					pixels[pix_start]=Color.WHITE.getRGB();// draw point
				//determine pix end
				
				pix_end = pix_start-model.getZData(dim_x,dim_y)*width;//takes data at given position
				if(pix_end<0) {//pixels out of frame
					pix_end=0; System.out.println("attention, some pixel values are outside of the coordinate system");}
				if(pix_end<pixels.length)
					pixels[pix_end]=Color.BLUE.getRGB(); // draw point
			/// sending coordinate data to each node
				model.getNode(dim_x,dim_y).setPosition(int2Vec(pix_end));
				
			/// drawing horizontal height lines
				drawLine(pixels, pix_start, pix_end+width, Color.LIGHT_GRAY.getRGB());
			}
		}
	}
	
	public void drawMesh(int[] pixels) {
		//drawing Meshgrid
		
		
		//looping throufh X-Y dependent dimensions
		for (int dim_x = 0; dim_x<model.dimX; dim_x++) {
			for (int dim_y = 0; dim_y < model.dimY;dim_y++) {
				// right neighbor node
				if(dim_y<model.dimY-1)
				drawLine(pixels, model.getNode(dim_x, dim_y).getPosition(), model.getNode(dim_x, dim_y+1).getPosition(), Color.BLUE.getRGB());
				// bottom neighbor node
				if(dim_x<model.dimX-1)
				drawLine(pixels, model.getNode(dim_x, dim_y).getPosition(), model.getNode(dim_x+1, dim_y).getPosition(), Color.BLUE.getRGB());
				// bottom right neighbor node
				//if(dim_x<model.dimX-1&&dim_y<model.dimY-1)
				//drawLine(pixels, model.getNode(dim_x, dim_y).getPosition(), model.getNode(dim_x+1, dim_y+1).getPosition(), Color.BLUE.getRGB());
			}
		}
	}
	
	
	public int[] update(Camera camera, int[] pixels) {
		//draw background
		for(int n=0; n<pixels.length; n++) {
			//if(pixels[n]!= Color.RED.getRGB())
				pixels[n] = Color.DARK_GRAY.getRGB();// Draws line in bg
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
		drawLine(pixels,origin, new Vec2(height,0), Color.BLACK.getRGB());
		
		//DATA
		drawData(pixels);
		drawMesh(pixels);
		
		//cosystem.origin.y+=1;cosystem.origin.x+=1;
		return pixels;
	}
}
