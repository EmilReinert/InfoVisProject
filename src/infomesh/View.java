package infomesh;

import java.awt.Color;
import java.awt.Graphics2D;


public class View {

	public int mapWidth, mapHeight, width, height;
	public Model m;
	CoSystem cosystem;
	
	public View(int w, int h) {
		width = w;
		height = h;
		cosystem = new CoSystem(new Vec2( height/2,width/3));
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

	public int[] update(Camera camera, int[] pixels) {
		//draw background
		for(int n=0; n<pixels.length; n++) {
			//if(pixels[n]!= Color.RED.getRGB())
				pixels[n] = Color.DARK_GRAY.getRGB();// Draws line in bg
		}
		//draw coord system
		int origin_n = (int) (cosystem.origin.x*height+cosystem.origin.y);
		for(int n=0; n<pixels.length; n++) {
			// Z AXis
			if(n%width==(int)cosystem.origin.y&&n<=origin_n) pixels[n]= Color.BLACK.getRGB();
			//Y
			if(n>origin_n ) {
				for (int i = 0; i<height-(int)cosystem.origin.y ;i++) {pixels[origin_n+i]=Color.BLACK.getRGB();}
				//X starting from origin

			}
		}
		drawLine(pixels,cosystem.origin, new Vec2(height,0));
		cosystem.origin.y+=1;cosystem.origin.x+=1;
		return pixels;
	}
}
