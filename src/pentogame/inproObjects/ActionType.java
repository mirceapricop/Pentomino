package pentogame.inproObjects;


/** 
 * enumeration of the different actions in our domain 
 * along with handy test operations and a reversing operation
 */
public enum ActionType {
	/** continue a move-action into the same direction */
	CONTINUE, 
	/** move to the left */
	LEFT, 
	/** move to the right */
	RIGHT, 
	/** continue a move-action into the reverse direction */
	REVERSE, 
	/** stop a motion */
	STOP, 
	/** release the load */
	DROP,
	/** move up	 */
	UP,
	/** move down */
	DOWN,
	/** Cancel	 */
	CANCEL;
	
	public boolean isMotion() {
		return this.isImplicitDirection() || this.isExplicitDirection();
	}
	public boolean isStop(){
		return (this.equals(STOP));
	}
	public boolean isCancel(){
		return (this.equals(CANCEL));
	}
	
	public boolean isImplicitDirection() {
		return this.equals(CONTINUE) || this.equals(REVERSE);
	}
	
	public boolean isExplicitDirection() {
		return this.equals(LEFT) || this.equals(RIGHT) || this.equals(UP) || this.equals(DOWN);
	}
	public boolean isOrthogonal(ActionType predescessor){
		return (((this.equals(LEFT)|| this.equals(RIGHT)) && (predescessor.equals(UP) || predescessor.equals(DOWN))) 
			|| (this.equals(UP)||this.equals(DOWN))&&(predescessor.equals(LEFT)|| predescessor.equals(RIGHT)));
	}
	
	/** return the reverse of this action, if this exists */
	ActionType reverseDirection() {
		switch (this) {
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		case UP: return DOWN;
		case DOWN: return UP;
		default: return this;
		}
	}
}

