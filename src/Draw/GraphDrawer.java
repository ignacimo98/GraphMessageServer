package Draw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import ConnectionLogic.Server;

/**
 * @author John B. Matthews; distribution per GPL.
 */
public class GraphDrawer extends JComponent {
	
	private static final int WIDE = 1860;
	private static final int HIGH = 960;
	private static final int RADIUS = 50;
	private static final Random rnd = new Random();
	private int radius = RADIUS;
	private Kind kind = Kind.Circular;
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	private static GraphDrawer graphDrawer = null;
	
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				JFrame f = new JFrame("GraphDrawer");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				graphDrawer = new GraphDrawer();
				f.add(new JScrollPane(graphDrawer), BorderLayout.CENTER);
				f.pack();
				f.setLocationByPlatform(true);
				f.setVisible(true);
				f.setResizable(false);
				
				int j;
				int numDevices = Server.connections.numDevices();
				int numBots = 5;
				for (j = 0; j < numDevices + numBots; j++) {
					graphDrawer.createNodes();
				}
				
				for (Node node:  graphDrawer.nodes
				     ) {
					graphDrawer.createEdges(node, graphDrawer.nodes.get(rnd.nextInt(j)));
				}
				
				
				
			}
		});
		
		boolean run = true;
		ArrayList Colors = Colors();
		while (run) {
			
			try {
				Thread.sleep(1000);
				for (Node n : graphDrawer.nodes) {
					n.color = (Color) Colors.get(rnd.nextInt(2));
					graphDrawer.repaint();
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDE, HIGH);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(new Color(0x00f0f0f0));
		g.fillRect(0, 0, getWidth(), getHeight());
		for (Edge e : edges) {
			e.draw(g);
		}
		for (Node n : nodes) {
			n.draw(g);
		}
	}
	
	/**
	 * The kinds of node in a graph.
	 */
	private enum Kind {
		Circular;
	}
	
	/**
	 * ArrayList of colors
	 * @return ArrayList with colors
	 */
	private static ArrayList<Color> Colors (){
		ArrayList Colors = new ArrayList<Color>();
		Colors.add(Color.RED);
		Colors.add(Color.GREEN);
		return Colors;
	}
	
	
	private void createNodes() {
		int p_y = rnd.nextInt(getHeight() - radius) + radius;
		int p_x = rnd.nextInt(getWidth() - radius) + radius;
		Point point = new Point(p_x, p_y);
		GraphDrawer.Node n = new Node(point);
		nodes.add(n);
		repaint();
	}
	
	
	/**
	 * An Edge is a pair of Nodes.
	 */
	private static class Edge {
		
		private Node n1;
		private Node n2;
		
		public Edge(Node n1, Node n2) {
			this.n1 = n1;
			this.n2 = n2;
		}
		
		public void draw(Graphics g) {
			Point p1 = n1.getLocation();
			Point p2 = n2.getLocation();
			g.setColor(Color.black);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	private void createEdges(Node n1, Node n2) {
		GraphDrawer.Edge edge = new Edge(n1, n2);
		edges.add(edge);
		repaint();
	}
	
	/**
	 * A Node represents a node in a graph.
	 */
	private static class Node {
		
		private Point p;
		private int r;
		private Color color;
		private Kind kind;
		private Rectangle b = new Rectangle();
		
		/**
		 * Construct a new node.
		 */
		public Node(Point p) {
			this.p = p;
			this.r = RADIUS;
			this.color = Color.red;
			this.kind = Kind.Circular;
			setBoundary(b);
		}
		
		/**
		 * Calculate this node's rectangular boundary.
		 */
		private void setBoundary(Rectangle b) {
			b.setBounds(p.x - r, p.y - r, 2 * r, 2 * r);
		}
		
		/**
		 * Draw this node.
		 */
		public void draw(Graphics g) {
			g.setColor(this.color);
			g.fillOval(b.x, b.y, b.width, b.height);
		}
		
		/**
		 * Return this node's location.
		 */
		public Point getLocation() {
			return p;
		}
		
		/**
		 * Return true if this node contains p.
		 */
		public boolean contains(Point p) {
			return b.contains(p);
		}
	}
}