package pentogame.inproObjects;

import java.util.List;


//import inpro.domains.greifarm.ActionStrength;
import inpro.incremental.unit.IU;
import inpro.incremental.unit.WordIU;

public class ActionIU extends AbstractActionIU{

	private boolean precedesPause = false;


	
	public ActionIU(){
		
	}
	
	public ActionIU(ActionIU sll, List<WordIU> groundingWords,
			ActionType actionType, ActionStrength strengthModifier) {
		// TODO Auto-generated constructor stub
	}

	

	public void setPrecedesPause(boolean b) {
		precedesPause = b;
	}
	
	public  boolean getPrecedesPause(){
		return precedesPause;
	}


	@Override
	protected ActionIU predecessor() {
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
		
		public boolean isWeak() {
			return actionStrength == ActionStrength.WEAK || actionStrength == ActionStrength.NONE;
		}
	private void calculateTarget(){
		
	}
}
