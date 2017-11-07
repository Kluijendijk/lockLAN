/* Tyler Chen 11/7/17 Robot Class
 * 
 */
public class Robot {

    private Odometry odometer;
    private int scale;

    public void drawMap() {
	odometer.drawMap();
    }

    public Robot(int scale, Odometry odometer) {
	this.odometer = odometer;
	this.scale = scale;
	StdDraw.setScale(scale*(-1), scale);
    }

    public static void main(String args[]) {
	Robot yui = new Robot(10, new Odometry(1, 0.8, 0, 0));
	yui.drawMap();
    }
}
