package infomesh;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


public class MeshMaker extends JFrame implements Runnable {
	
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	public int[] pixels;
	public Camera camera;
	public View view; //= Screen
	
	
	public static void main(String[] args) {
		Model m = new Model(new File("data/Data_Mortality.txt"));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
