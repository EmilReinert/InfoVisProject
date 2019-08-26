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
	public static int WIDTH = 600;//TODO make adjustable
	public static int HEIGHT = 600;	
	
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	public int[] pixels;
	public View view; //= Screen
	
	public MeshMaker(Model m, CoSystem co) {
		
		
		thread = new Thread(this);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		view = new View(WIDTH,HEIGHT,m,co);
		addMouseListener(co);addMouseMotionListener(co);addMouseWheelListener(co);
		setSize(WIDTH, HEIGHT);
		setTitle("InfoMesh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		
		
		JPanel panel = new JPanel();
	    getContentPane().add(panel);
	    
	    JLabel welcome = new JLabel("Please choose which shape's area/volume you would like to calculate.");
	    add(welcome);
		
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
		final double ns = 1000000000.0;/// / 60.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				view.update( pixels);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	
	/*///FUNCTION FOR POSSIBLE JPANEL??
	public void paint(Graphics g) {
	    super.paint(g); 
	    Graphics2D g2 = (Graphics2D) g;
	    Line2D lin = new Line2D.Float(100, 100, 250, 260);
	    g2.draw(lin);
	  }
	 */
	
	public static void main(String[] args) {
		System.out.println(Color.BLUE.getRGB());
		CoSystem co = new CoSystem(new Vec2(HEIGHT/2,WIDTH/6), HEIGHT, WIDTH, 0.5);
		Model m = new Model(new File("data/Data_Mortality.txt"),co);
		MeshMaker mesh = new MeshMaker(m,co);
	}


}
