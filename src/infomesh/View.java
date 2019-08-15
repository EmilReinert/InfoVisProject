package infomesh;

import java.awt.Color;


public class View {

	public int mapWidth, mapHeight, width, height;
	public Model m;
	
	public View(int w, int h) {
		width = w;
		height = h;
	}

	public int[] update(Camera camera, int[] pixels) {
		for(int n=0; n<pixels.length*0.2; n++) {
			pixels[n] = Color.DARK_GRAY.getRGB();// Draws line in bg
			if (n%width==1)pixels[n]= Color.BLACK.getRGB();
		}
		for(int i=(int) (pixels.length*0.2); i<pixels.length; i++){
			pixels[i] = Color.gray.getRGB();
		}
		return pixels;
	}
}
