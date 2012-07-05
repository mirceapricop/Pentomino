package pentogame.inproObjects;

/**
 * This class is a goal for a movement on the GUI.
 * 
 * @author: Philipp Schlesinger, Maike Paetzel
 */
public class Point {
	
	/**
	 * horizontal, - is left, + is right 
	 */
	private final int x; 
	
	
	/**
	 * vertical, - is up, + is down
	 */
	private final int y; 
	
	/**
	 * Constructor to set a new point
	 * 
	 * @param xInput
	 * @param yInput
	 */
	public Point(int xInput, int yInput){
		
		x = xInput;
		y = yInput;
	}

	/**
	 * 
	 * @return x the horizontal point
	 */
	public int getX(){
		
		return x;
	}
	
	/**
	 * 
	 * @return y the vertical point
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * 
	 * @return the negative point
	 */
	public Point negate(){
		return scale(-1);
	}

	/**
	 * 
	 * @param factor for scale
	 * @return point the new point = old point * factor
	 */
	private Point scale(int factor) {
		Point point = new Point(x*factor,y*factor);
		
		return point;
	}
	
	public String toString() {
	  return "" + x + "-" + y;
	}
}
