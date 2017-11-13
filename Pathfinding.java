/* Tyler Chen 11/8/18 Pathfinding Class
 * Pathfinding based on A*
 */

import java.util.ArrayList;
//import edu.princeton.cs.algs4.*;
import net.sf.javaml.core.kdtree.KDTree;

public class Pathfinding {

    // Point class
    private class Point {

	private int hitCount;
	private double x;
	private double y;
	
	public void incrementHitCount() {
	    hitCount++;
	}

	public double getX() {
	    return x;
	}

	public double getY() {
	    return y;
	}

	public Point(double x, double y) {
	    this.x = x;
	    this.y = y;
	    hitCount = 0;
	}
    }
    
    private int scale; // Scale of the map
    private int resolution; // Map has 4 x (scale x resolution) number of points
    private double levelCost; // Distance between points horizontally and vertically
    private double diagonalCost; // Distance between points diagonally
    private KDTree empty;  // Stores empty points
    private KDTree filled; // Stores points occupied by an obstacle

    public void drawMap() {
	StdDraw.setPenColor(211, 211, 211);
	Object[] points = empty.range(new double[] {-10, -10}, new double[] {10, 10});
	
	for(int i = 0; i <= points.length - 1; i++) {
	    Point p = (Point) points[i];
	    StdDraw.filledCircle(p.getX(), p.getY(), 0.2);
	}

	points = filled.range(new double[] {-10, -10}, new double[] {10, 10});
	StdDraw.setPenColor(StdDraw.BLACK);
	
	for(int i = 0; i <= points.length - 1; i++) {
	    Point p = (Point) points[i];
	    StdDraw.filledCircle(p.getX(), p.getY(), 0.2);
	}
    }
    
    public Pathfinding(int scale, int resolution) {
	this.scale = scale;
	this.resolution = resolution;
	empty = new KDTree(2);
	filled = new KDTree(2);
	levelCost = (double) 1/resolution;
	diagonalCost = Math.sqrt(2*Math.pow(levelCost, 2));

	// Add all points to tree
	for(double i = -scale; i <= scale; i += levelCost) {
	    for(double j = -scale; j <= scale; j += levelCost) {
		empty.insert(new double[] {i, j}, new Point(i, j));
	    }
	}
    }

    public static void main(String[] args) {
	Pathfinding pathfinder = new Pathfinding(10, 2);
	StdDraw.setScale(-10, 10);
	pathfinder.drawMap();
    }
}
