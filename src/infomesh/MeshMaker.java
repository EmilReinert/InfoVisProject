package infomesh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class MeshMaker extends JFrame implements Runnable {
	
	//For Window pixle size
	public static int WIDTH = 500;//TODO make adjustable
	public static int HEIGHT = 500;	
	public static int HEIGHT_legend = 150;
	
	public Color bg_color = Color.GRAY;
	
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	public int[] pixels;
	public Diagram diagram; //= Diagram
	public InfoBox info; // data info text adn gui
	
	
	public MeshMaker(Model m, CoSystem co) {
		
		
		thread = new Thread(this);
		image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		diagram = new Diagram(WIDTH,HEIGHT,m,co, bg_color);
		info = new InfoBox(WIDTH, HEIGHT+HEIGHT_legend,diagram,co, bg_color);
		addMouseListener(co);addMouseMotionListener(co);addMouseWheelListener(co);addKeyListener(info);
		
		setSize(WIDTH, HEIGHT+HEIGHT_legend);
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
		//draws diagram image
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
		//draws info box
		info.paint(g);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0/3;/// / 60.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				diagram.update( pixels);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	
	
	 
	
	public static void main(String[] args) {
		CoSystem co = new CoSystem(HEIGHT, WIDTH);
		Model m_example = new Model(new File("data/Data_Mortality.txt"), co);
		Model m = new Model(new File("data/Data_Mortality.txt"),new File("data/Data_Mortality_male.txt"),
				new File("data/Data_Mortality_female.txt"),co);
		MeshMaker mesh = new MeshMaker(m,co);
	}


}
