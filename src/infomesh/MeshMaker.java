package infomesh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import javax.swing.JFrame;



public class MeshMaker extends JFrame implements Runnable {
	
	//For Window pixle size
	public static int WIDTH = 800;
	public static int HEIGHT = 800;	
	
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	public int[] pixels;
	public Camera camera;
	public View view; //= Screen
	
	public MeshMaker() {
		thread = new Thread(this);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		camera = new Camera(-2, 0, 1, 0, 0, -.66);
		view = new View(WIDTH,HEIGHT);
		setSize(WIDTH, HEIGHT);
		setTitle("InfoMesh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}
	
	private synchronized void start() {
		running = true;
		thread.start();
	}	
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				view.update(camera, pixels);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	
	public static void main(String[] args) {
		Model m = new Model(new File("data/Data_Mortality.txt"));
		MeshMaker mesh = new MeshMaker();
	}


}
