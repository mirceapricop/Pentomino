package pentogame.inproObjects;

import java.util.List;


import inpro.incremental.unit.IU;
import inpro.incremental.unit.WordIU;

public class ActionIU extends IU{

	private ActionType type = ActionType.STOP;
	private boolean precedesPause = false;
	// store the extent of this action
	private ActionStrength actionStrength = ActionStrength.NORMAL;
	
	public ActionIU(){
		
	}
	
	public ActionIU(ActionIU sll, List<WordIU> groundingWords,
			ActionType actionType, ActionStrength strengthModifier) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toPayLoad() {
		return type + (type.isMotion() ? " / " + actionStrength : "");
	}

	public void setPrecedesPause(boolean b) {
		precedesPause = b;
	}
	
	public  boolean getPrecedesPause(){
		return precedesPause;
	}


	public ActionType getType() {

		return type;
	}

	
	private ActionIU predecessor() {
		return (ActionIU) previousSameLevelLink;
	}
	/**
	 * Compares the 
	 * 
	 * @return True if this ActionIU is part of a diagonal Movement
	 */
	public boolean isDiagonalMovement(){
		if(this.predecessor()!= null){
		if(!this.predecessor().precedesPause){
			if(this.getType().isExplicitDirection()){
				return (this.getType().isOrthogonal(this.predecessor().getType()));
			}else{// implicit Direction Continue and Reverse
				return this.predecessor().isDiagonalMovement();
			}
		}
		}
		return false;
	}
	// this tries to resolve CONTINUE and REVERSE, leaving only explicit directions
		public ActionType realizedDirection() {
			if (type.isImplicitDirection()) {
				if (previousSameLevelLink != null) {
					if (type == ActionType.CONTINUE) {
						return predecessor().realizedDirection();
					} else { // REVERSE
						return predecessor().realizedDirection().reverseDirection();
					}
				} 
			} else if (type == ActionType.STOP) {
				if (previousSameLevelLink != null) {
					return predecessor().realizedDirection();
				}
			}
			return type;
		}
	
}
