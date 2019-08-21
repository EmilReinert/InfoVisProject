package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;
import java.time.OffsetDateTime;


public class View {

	public int mapWidth, mapHeight, width, height;
	public Model model;
	boolean showMidPlane = true;
	CoSystem cosystem;
	
	public View(int w, int h, Model m, CoSystem co) {
		width = w;
		height = h;
		model = m;
		cosystem = co;
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
		if(a.x==b.x) {
			//horizontal line TODO
			return;
		}
		// Vertical Line
		if(a.y == b.y) {}
		
		
		//Greradenfunktion
		double m; //STEIGUNG
		if(first.y>second.y)m=1;
		else m = -1;
		m*=(second.y-first.y)/(second.x-first.x);
		double t= first.y-first.x*m; //x verschiebung		
		///
		float x,y,f_x;
		for(int n=0; n<pixels.length; n++) {
			x = (n-n%width)/width;
			y = n%width;
			f_x = (float) (m*x+t);
			//line starts
			//System.out.println(first.x);
			if (x>=first.x&&x<=second.x) {//System.out.println(x);
				if((int)y==(int)f_x) {pixels[n]= Color.BLACK.getRGB();}
			}
		}	
					
	}
	
	public void drawLine(int[] pixels, int a, int b, int color) {
		Vec2 vec_a = int2Vec(a);
		Vec2 vec_b = int2Vec(b);
		drawLine(pixels, vec_a, vec_b, color);
	}
	
	public void drawData(int[] pixels) {
		//main datavizulization method
		//starting with drawing of points at 3d diagram positions
		
		//bottom plane starting point z = 0
		int pix_start = 0,pix_end =0;
		int origin=vec2Int(cosystem.origin); // = origin start
		
		/// steps in xy plane
		//for x step we need the x axis function
		int x_step = (int) (((height-cosystem.origin.x)/(model.dimX-1)));
		int y_step = (int) ((width-cosystem.origin.y)/(model.dimY-1));
		// x offset from diagonal axis
		int x_off,x; double f_x;
		Vec2 first = cosystem.origin;
		Vec2 second = new Vec2(height,0);
		double m = -1*(second.y-first.y)/(second.x-first.x);
		double t= first.y-first.x*m; //x verschiebung 
		
		int data_count=0; // index for data nodes
		
		//looping throufh X-Y dependent dimensions
		for (int dim_x = 0; dim_x<model.dimY; dim_x++) {
			for (int dim_y = 0; dim_y < model.dimX;dim_y++) {
				//determine pix start
				pix_start = origin+ dim_x*x_step*width+dim_y*y_step;
				x= (pix_start-pix_start%width)/width;
				f_x = m*x+t;
				x_off = (int) (cosystem.origin.y-f_x);
				pix_start+= x_off;
				if(pix_start<pixels.length)
					pixels[pix_start]=Color.WHITE.getRGB();// draw point
				//determine pix end
				
				pix_end = pix_start-model.getZData(data_count)*width;//takes data at given position
				if(pix_end<0) {//pixels out of frame
					pix_end=0; System.out.println("attention, some pixel values are outside of the coordinate system");}
				if(pix_end<pixels.length)
					pixels[pix_end]=Color.RED.getRGB(); // draw point
				// drawing horizontal height line
				//drawLine(pixels, pix_start, pix_end, Color.WHITE.getRGB());
				
				data_count++;
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
		int origin_n = vec2Int(cosystem.origin);
		for(int n=0; n<pixels.length; n++) {
			
			//Z AXis
			if(n%width==(int)cosystem.origin.y&&n<origin_n) pixels[n]= Color.BLACK.getRGB();
			//Y	Axis		
			if(n>origin_n&&n/width<cosystem.origin.x)pixels[n]=Color.BLACK.getRGB();
		}
		//draw origin
		pixels[origin_n]= Color.RED.getRGB();
		//X Axis
		drawLine(pixels,cosystem.origin, new Vec2(height,0), Color.BLACK.getRGB());
		
		
		//DATA
		drawData(pixels);
		
		//cosystem.origin.y+=1;cosystem.origin.x+=1;
		return pixels;
	}
}
