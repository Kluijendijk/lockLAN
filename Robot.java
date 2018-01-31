/* Tyler Chen 11/7/17 Robot Class
 * 
 */

import edu.princeton.cs.algs4.*;
import java.util.LinkedList;

public class Robot {

    private Odometry odometer;
    private Pathfinding pathfinder;
    private Motors motors;
    private LinkedList<Move> moveQueue; // queue of moves to be executed
    private int scale;

    private class Turn implements Move {
	private double theta; // the angle to be turned to
	private boolean direction; // true = turning left, false = turning right
	private boolean init;
	
	public void execute() {
	    double adjustedAngle = odometer.getTheta() % (2*Math.PI);
	    if(direction) {
		setRightSpeed(0.25);
		setLeftSpeed(-0.25);
	    }
	    else if(!direction) {
		setRightSpeed(-0.25);
		setLeftSpeed(0.25);
	    }
	}

	public boolean stop() {
	    double adjustedAngle = odometer.getTheta() % (2*Math.PI);
	    if(init) {
		if(adjustedAngle < theta) {
		    direction = true;
		}
		else if(adjustedAngle > theta) {
		    direction = false;
		}
		init = false;
	    }
	    if(direction && adjustedAngle > theta) {
		return true;
	    }
	    else if(!direction && adjustedAngle < theta) {
		return true;
	    }
	    else return false;
	}
	
	public Turn(double theta) {
	    this.theta = theta;
	    init = true;
	    direction = true;
	}
    }
    
    private class Forward implements Move {
	private double distance; // the distance to be moved
	private double initX;
	private double initY;
	private boolean init;
	
	public void execute() {
	    setLeftSpeed(0.5);
	    setRightSpeed(0.5);
	}

	public boolean stop() {
	    if(init) {
		initX = odometer.getX();
		initY = odometer.getY();
		init = false;
	    }
	    return Math.hypot(odometer.getX() - initX, odometer.getY() - initY) > distance;
	}
	
	public Forward(double distance) {
	    this.distance = distance;
	    init = true;
	    initX = 0;
	    initY = 0;
	}
    }
    
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
	moveQueue = new LinkedList<Move>();
	moveQueue.add(new Turn(Math.PI/2));
	moveQueue.add(new Forward(4));
	moveQueue.add(new Turn(Math.PI));
	moveQueue.add(new Forward(4));
	
	StdDraw.setScale(scale*(-1), scale);
	StdDraw.enableDoubleBuffering();
    }

    public void updateDistances() {
	odometer.setRight(motors.getRightDistance());
	odometer.setLeft(motors.getLeftDistance());
    }

    public void checkQueue() {
	if(moveQueue.isEmpty()) return;
	Move currentMove = moveQueue.peek();
	if(!currentMove.stop()) currentMove.execute();
	else {
	    setRightSpeed(0);
	    setLeftSpeed(0);
	    moveQueue.remove();
	    System.out.println("Finished move, current position: ");
	    System.out.println("X: " + odometer.getX() + " Y: " + odometer.getY() + " Angle: " + odometer.getTheta());
	}
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
	/*
	double rightSpeed = 0.4;
	double leftSpeed = 0.7;

        yui.setLeftSpeed(leftSpeed);
	yui.setRightSpeed(rightSpeed);
	*/

	try {
	    while(true) {
		yui.updateDistances();
		yui.updateOdometry();
		yui.checkQueue();
		yui.drawMap();
		Thread.sleep(20);
	    }
	}

	catch(InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
