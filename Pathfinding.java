/* Tyler Chen 11/8/18 Pathfinding Class
 * Pathfinding based on A*
 */

import java.util.ArrayList;
//import edu.princeton.cs.algs4.*;
import net.sf.javaml.core.kdtree.KDTree;
import java.util.PriorityQueue;

public class Pathfinding {

    // Point class
    private class Point {

	private int hitCount;
	private int status; // 0 = undiscovered, 1 = empty, 2 = filled
	private double x;
	private double y;
	private double gScore;
	private double fScore;
	
	public void incrementHitCount() {
	    hitCount++;
	}

	public void setStatus(int status) {
	    this.status = status;
	}

	public void setgScore(double gScore) {
	    this.gScore = gScore;
	}

	public void setfScore(double fScore) {
	    this.fScore = fScore;
	}

	public int getStatus() {
	    return status;
	}
	
	public double getgScore() {
	    return gScore;
	}

	public double getfScore() {
	    return fScore;
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
	    status = 0;
	    gScore = Double.MAX_VALUE;
	    fScore = Double.MAX_VALUE;
	}
    }
    
    private int scale; // Scale of the map
    private int resolution; // Map has 4 x (scale x resolution) number of points
    private double levelCost; // Distance between points horizontally and vertically
    private double diagonalCost; // Distance between points diagonally
    private KDTree points; // KDTree of all points
    //private Comparator<Points> gscorecomparator;
    //private Comparator<Points> fscorecomparator;
    
    public ArrayList<Double[]> path(double initX, double initY, double finalX, double finalY) {
	ArrayList<Point> closedSet = new ArrayList<Point>(); // Set of nodes already visited
	ArrayList<Point> openSet = new ArrayList<Point>(); // Set of nodes to be visited
	openSet.add((Point) points.search(new double[] {initX, initY})); // Add the initial coordinates to openSet
	KDTree cameFrom = new KDTree(2); 
	
	// Set fScore and gScore of every point to Double.MAX_VALUE
	for(Object o: points.range(new double[] {-scale, -scale}, new double[] {scale, scale})) {
	    Point currentPoint = (Point) o;
	    currentPoint.setfScore(Double.MAX_VALUE);
	    currentPoint.setgScore(Double.MAX_VALUE);
	    points.insert(new double[] {currentPoint.getX(), currentPoint.getY()}, currentPoint);
	}

	Point start = (Point) points.search(new double[] {initX, initY});
	start.setgScore(0);
	start.setfScore(0);
	points.insert(new double[] {initX, initY}, start);
	
	while(!openSet.isEmpty()) {
	    Point current = new Point(0, 0);
	    double lowest = Double.MAX_VALUE;
	    for(Point p: openSet) {
		double fScore = p.getfScore();
		if(fScore < lowest) {
		    lowest = fScore;
		    current = p;
		}
	    }
	    //System.out.println(current.getX() + " " + current.getY());
	    if(current.getX() == finalX && current.getY() == finalY)
		return reconstructPath(cameFrom, current);

	    openSet.remove(current);
	    closedSet.add(current);

	    double currentX = current.getX();
	    double currentY = current.getY();
	    ArrayList<Double[]> neighbors = new ArrayList<Double[]>();
	    neighbors.add(new Double[] {currentX + levelCost, currentY});
	    neighbors.add(new Double[] {currentX - levelCost, currentY});
	    neighbors.add(new Double[] {currentX, currentY + levelCost});
	    neighbors.add(new Double[] {currentX, currentY - levelCost});
	    neighbors.add(new Double[] {currentX + levelCost, currentY + levelCost});
	    neighbors.add(new Double[] {currentX - levelCost, currentY + levelCost});
	    neighbors.add(new Double[] {currentX + levelCost, currentY - levelCost});
	    neighbors.add(new Double[] {currentX - levelCost, currentY - levelCost});

	    for(Double[] d: neighbors) {
		double[] p = new double[d.length];
		for(int i = 0; i <= d.length - 1; i++) {
		    p[i] = (double) d[i];
		}
		if((Point) points.search(p) == null)
		    continue;
		if(((Point) points.search(p)).getStatus() != 1) {
		    closedSet.add((Point) points.search(p));
		}
		if(closedSet.contains(points.search(p)))
		   continue;
		if(!openSet.contains(points.search(p)))
		    openSet.add((Point) points.search(p));
		double tentativegScore = current.getgScore() + distanceTo(new double[] {current.getX(), current.getY()}, p);
		if(tentativegScore >= ((Point) points.search(p)).getgScore())
		    continue;
		cameFrom.insert(p, current);
		Point newPoint = (Point) points.search(p);
		newPoint.setgScore(tentativegScore);
		newPoint.setfScore(tentativegScore); // This will include adding the heuristic cost estimate
		points.insert(new double[] {newPoint.getX(), newPoint.getY()}, p);
	    }
	}
	return null; // Change this to an exception at some point
    }

    private double distanceTo(double[] init, double[] fin) {
	return Math.sqrt(Math.pow(init[0] + fin[0], 2) + Math.pow(init[1] + fin[1], 2));
    }

    private ArrayList<Double[]> reconstructPath(KDTree cameFrom, Point current) {
	ArrayList<Double[]> totalPath = new ArrayList<Double[]>();
	totalPath.add(new Double[] {current.getX(), current.getY()});
	while(true) {
	    double newX = ((Point) cameFrom.search(new double[] {current.getX(), current.getY()})).getX();
	    double newY = ((Point) cameFrom.search(new double[] {current.getX(), current.getY()})).getY();
	    current = (Point) points.search(new double[] {newX, newY});
	    totalPath.add(new Double[] {current.getX(), current.getY()});
	    if(cameFrom.search(new double[] {current.getX(), current.getY()}) == null)
		break;
	}
	return totalPath;
    }
    
    public void drawMap() {
	
	Object[] pointsArray = points.range(new double[] {-scale, -scale}, new double[] {scale, scale});
	
	for(int i = 0; i <= pointsArray.length - 1; i++) {
	    Point p = (Point) pointsArray[i];
	    if(p.getStatus() == 1)
		StdDraw.setPenColor(211, 211, 211);
	    else if(p.getStatus() == 2)
		StdDraw.setPenColor(StdDraw.BLACK);
	    else
		StdDraw.setPenColor(StdDraw.WHITE);
	    StdDraw.filledCircle(p.getX(), p.getY(), 0.2);
	}
	/*
	pointsArray = points.range(new double[] {-scale, -scale}, new double[] {scale, scale});
	StdDraw.setPenColor(StdDraw.BLACK);
	
	for(int i = 0; i <= pointsArray.length - 1; i++) {
	    Point p = (Point) points[i];
	    StdDraw.filledCircle(p.getX(), p.getY(), 0.2);
	}
	*/
    }
    
    public Pathfinding(int scale, int resolution) {
	this.scale = scale;
	this.resolution = resolution;
	points = new KDTree(2);
	levelCost = (double) 1/resolution;
	diagonalCost = Math.sqrt(2*Math.pow(levelCost, 2));
        
	// Set all nodes to empty. TEMPORARY
	for(double i = -scale; i <= scale; i += levelCost) {
	    for(double j = -scale; j <= scale; j += levelCost) {
		Point currentPoint = new Point(i, j);
		currentPoint.setStatus(1);
		points.insert(new double[] {i, j}, currentPoint);
	    }
	}

	int j = -5;
	// Create a wall of filled nodes to test pathing. TEMPORARY
	for(double i = -scale + 5; i <= scale - 5; i += levelCost) {
	    Point p = (Point) points.search(new double [] {i, j});
	    p.setStatus(2);
	    points.insert(new double[] {p.getX(), p.getY()}, p);
	}
    }

    public static void main(String[] args) {
	Pathfinding pathfinder = new Pathfinding(10, 2);
	StdDraw.setScale(-10, 10);
	StdDraw.enableDoubleBuffering();
	pathfinder.drawMap();
	StdDraw.setPenColor(StdDraw.RED);
	ArrayList<Double[]> d = pathfinder.path(0, 0, 0, -6);
	for(int i = d.size() - 2; i >= 0; i--) {
	    StdDraw.line(d.get(i + 1)[0], d.get(i + 1)[1], d.get(i)[0], d.get(i)[1]);
	}
	StdDraw.show();
    }
}
