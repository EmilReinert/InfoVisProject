package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;


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
	
	public void drawLine(int[]pixels,Vec2 a, Vec2 b) {
		//draws line on screen by manipulating pixel values
		Vec2 first,second;//System.out.println(a.x+" "+b.x);
		if (a.x<b.x) {first = a;second=b;}
		else {first=b;second=a;}
		if(a.x==b.x) {
			//horizontal line TODO
			return;
		}
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
	
	public void drawData(int[] pixels) {
		//main datavizulization method
		//starting with drawing of points at 3d diagram positions
		
		//bottom plane starting point z = 0
		int pix_start = 0;
		int origin= (int) ((cosystem.origin.x-1)*width+cosystem.origin.y); // = origin start
		
		/// steps in xy plane
		//for x step we need the x axis function
		int x_step = (int) (((height-cosystem.origin.x)/(model.dimX-1))*width);
		int y_step = (int) ((width-cosystem.origin.y-1)/(model.dimY-1));
		// x offset from diagonal axis
		int x_off,x; double f_x;
		Vec2 first = cosystem.origin;
		Vec2 second = new Vec2(height,0);
		double m = -1*(second.y-first.y)/(second.x-first.x);
		double t= first.y-first.x*m; //x verschiebung 
		
		
		//looping throufh X-Y dependent dimensions
		for (int dim_x = 0; dim_x<model.dimY; dim_x++) {
			for (int dim_y = 0; dim_y < model.dimX;dim_y++) {
				//determine pix start
				x= dim_x;
				f_x = m*x+t;
				x_off = (int) (cosystem.origin.y-f_x);
				pix_start = origin+ dim_x*x_step+dim_y*y_step;
				if(pix_start<pixels.length)
					pixels[pix_start]=Color.WHITE.getRGB();
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
		int origin_n = (int) ((cosystem.origin.x-1)*width+cosystem.origin.y);
		for(int n=0; n<pixels.length; n++) {
			
			//Z AXis
			if(n%width==(int)cosystem.origin.y&&n<origin_n) pixels[n]= Color.BLACK.getRGB();
			//Y	Axis		
			if(n>origin_n&&n/width<cosystem.origin.x)pixels[n]=Color.BLACK.getRGB();
		}
		//draw origin
		pixels[origin_n]= Color.RED.getRGB();
		//X Axis
		drawLine(pixels,cosystem.origin, new Vec2(height,0));
		
		
		//DATA
		drawData(pixels);
		
		//cosystem.origin.y+=1;cosystem.origin.x+=1;
		return pixels;
	}
}
