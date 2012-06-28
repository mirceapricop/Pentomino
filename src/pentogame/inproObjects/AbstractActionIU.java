package pentogame.inproObjects;

import inpro.incremental.unit.IU;

/** Abstract Action IU
 * 
 * @author Philipp Schlesinger, Maike Paetzel
 * 
 */
public abstract class AbstractActionIU extends IU {
	
	/**
	 *  ActionTypes are Move, Reverse, Stop, Continue, Drop, Cancel
	 */
	protected ActionType type = ActionType.STOP; //Initial ActionType is STOP
	
	/**
	 *  ActionStrength are Weak, Normal, Strong, Max
	 */
	protected ActionStrength actionStrength = ActionStrength.NORMAL; //Initial ActionStrength is NORMAL
	
	/**
	 * Goal of movement on GUI
	 */
	private Point target;
	

	/**
	 * Standard method for IU
	 */
	@Override
	public String toPayLoad() {
		return type + (type.isMotion() ? " / " + actionStrength : "");
	}

	/**
	 * @return ActionType
	 */
	public ActionType getType() {

		return type;
	}

	/**
	 * @return predecessor as a same level link
	 */
	protected AbstractActionIU predecessor() {
		return (AbstractActionIU) previousSameLevelLink;
	}	
	
	/**
	 * @return target as a vector
	 */
	public Point getTarget(){
		
		return target;
	}
		
	/**
	 * Set a target as a vector
	 *
	 * @param newTarget
	 */
	public void setTarget(Point newTarget){
		target = newTarget;
		
	}
	
	/**
	 * Should return a vector of ActionStrength and ActionType
	 */
	public abstract Point getVector(); 
}
