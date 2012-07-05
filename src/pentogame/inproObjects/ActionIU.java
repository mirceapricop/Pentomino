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
  public ActionIU predecessor() {
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

  @Override
  public Point getVector() {
	  int x =0;
	  int y=0;
	  //TODO: What happens : "oben links" + "weiter" + "stop" + "zurück" 
	  //TODO: Scaling with the action strenght
	  if(type.isExplicitDirection()){
		  switch(type){
	  		case LEFT: x = -1;
	  		case RIGHT: x = 1;
	  		case UP: y = -1;
	  		case DOWN: y = 1;
		  }
		  return new Point(x,y);
		  
	  }else if(type.isImplicitDirection()){
		//TODO: Count how many times "weiter" and increase ActionStrength	
		  if(this.isDiagonalMovement()){ //TODO:check for weiter vs zurück
			if(firstNOTdiagonal().type.isHorizontal()){
				return new Point(firstNOTdiagonal().getVector().getX(), predecessor().getVector().getY());
			}else{
				return new Point(predecessor().getVector().getX(), firstNOTdiagonal().getVector().getY());
			}
		  }
			return  predecessor().getVector();
		  }
	  
	  else//Type is : Stop, Drop, Cancle
		  {
		    return new Point(0,0); // TODO: Not correct for the use above
	  }

	}
  private ActionIU firstNOTdiagonal(){
	  
	  if(this.isDiagonalMovement()){
		 return predecessor().firstNOTdiagonal();
	  }
	  else{
		  return this;
	  }
  }
}
