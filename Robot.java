/* Tyler Chen 11/7/17 Robot Class
 * 
 */

import edu.princeton.cs.algs4.*;

public class Robot {

    private Odometry odometer;
    private Pathfinding pathfinder;
    private Motors motors;
    private int scale;

    /*
    private class Turn implements Move {
	private double theta; // the angle to be turned to
	public Turn(double theta) {
	    this.theta = theta;
	}
    }

    private class Forward implements Move {
	private double distance; // the distance to be moved. This should be the node distance	
	public Forward(double distance) {
	    this.distance = distance;
	}
    }
    */

    public void drawMap() {
	StdDraw.clear();
	pathfinder.drawMap();
	odometer.drawMap();
	StdDraw.show();
    }

    public Robot(int scale, Odometry odometer, Pathfinding pathfinder, Motors motors) {
	this.odometer = odometer;
	this.pathfinder = pathfinder;
	this.scale = scale;
	this.motors = motors;
	StdDraw.setScale(scale*(-1), scale);
	StdDraw.enableDoubleBuffering();
    }

    public void updateDistances() {
	odometer.setRight(motors.getRightDistance());
	odometer.setLeft(motors.getLeftDistance());
    }

    public void setRightSpeed(double speed) {
	motors.setRightSpeed(speed);
    }

    public void setLeftSpeed(double speed) {
	motors.setLeftSpeed(speed);
    }

    public void updateOdometry() {
	odometer.updateOdometry();
    }

    public static void main(String args[]) {
	Robot yui = new Robot(10, new Odometry(1, 0.8, 0, 0), new Pathfinding(10, 2), new Motors());
	
	double rightSpeed = 0.5;
	double leftSpeed = -0.5;

        yui.setLeftSpeed(leftSpeed);
	yui.setRightSpeed(rightSpeed);
	
	try {
	    while(true) {
		yui.updateDistances();
		yui.updateOdometry();
		yui.drawMap();
		Thread.sleep(20);
	    }
	}

	catch(InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
