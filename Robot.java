/* Tyler Chen 11/7/17 Robot Class
 * 
 */

import edu.princeton.cs.algs4.*;

public class Robot {

    private Odometry odometer;
    private Pathfinding pathfinder;
    private int scale;

    public void drawMap() {
	StdDraw.clear();
	pathfinder.drawMap();
	odometer.drawMap();
	StdDraw.show();
    }

    public Robot(int scale, Odometry odometer, Pathfinding pathfinder) {
	this.odometer = odometer;
	this.pathfinder = pathfinder;
	this.scale = scale;
	StdDraw.setScale(scale*(-1), scale);
	StdDraw.enableDoubleBuffering();
    }

    public static void main(String args[]) {
	Robot yui = new Robot(10, new Odometry(1, 0.8, 0, 0), new Pathfinding(10, 2));
	yui.drawMap();
    }
}
