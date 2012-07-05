package pentogame.inproObjects;

import java.util.List;


//import inpro.domains.greifarm.ActionStrength;
import inpro.incremental.unit.IU;
import inpro.incremental.unit.WordIU;



public class ActionIU extends AbstractActionIU{
	
	
	/**
	 *  ActionTypes are Move, Reverse, Stop, Continue, Drop, Cancel
	 */
	protected ActionStrength strength = ActionStrength.NORMAL; //Initial ActionStrength is NORMAL

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
	  Point returnpoint = new Point(0,0);
	  
	  //TODO: What happens : "oben links" + "weiter" + "stop" + "zurück" 
	  
	  //The following piece of higher art is about ActionTypes 
	  if(type.isExplicitDirection()){
		  switch(type){
	  		case LEFT: x = -1;
	  		case RIGHT: x = 1;
	  		case UP: y = -1;
	  		case DOWN: y = 1;
		  }
		  returnpoint =  new Point(x,y);
		  
	  }
	  
	  	else if(type.isImplicitDirection()){
		  
	  		if(type == ActionType.CONTINUE) // User said "Weiter" 
	  		{
	  			if(this.isDiagonalMovement()){ 
	  				if(firstNOTdiagonal().type.isHorizontal()){
	  					returnpoint =  new Point(firstNOTdiagonal().getVector().getX(), predecessor().getVector().getY());
	  				}
	  				else{
	  					returnpoint = new Point(predecessor().getVector().getX(), firstNOTdiagonal().getVector().getY());
	  				}
	  			}
	  			else{
	  				returnpoint =  predecessor().getVector();
	  			}	
	  		}
	  		else if(type == ActionType.REVERSE) //User said "zurück"
	  		{
	  			if(this.isDiagonalMovement()){ 
	  				if(predecessor().precedesPause){
	  					if(firstNOTdiagonal().type.isHorizontal()){
	  						returnpoint = new Point(firstNOTdiagonal().getVector().getX(), predecessor().getVector().getY()).negate();
	  					}
	  						else{
	  							returnpoint = new Point(predecessor().getVector().getX(), firstNOTdiagonal().getVector().getY()).negate();
	  						}
	  				}
	  				else{ // This Reverse is not after a pause, so it is interpreted like continue
	  					if(firstNOTdiagonal().type.isHorizontal()){
	  						returnpoint = new Point(firstNOTdiagonal().getVector().getX(), predecessor().getVector().getY());
	  					}
	  						else{
	  							returnpoint = new Point(predecessor().getVector().getX(), firstNOTdiagonal().getVector().getY());
	  						}
	  				}
	  			}
	  			else{
	  				if(predecessor().precedesPause){ 
	  					returnpoint =  predecessor().getVector().negate();
	  				}
	  				else{ //This is just in case some says "links zurück" - we consider nobody is reverting his expression the same second he makes it 
	  					returnpoint =  predecessor().getVector();
	  				}
	  			}
	  			
	  		}
	  	}
	  	else//Type is : Stop, Drop, Cancle
	  	{
	  		returnpoint = new Point(0,0); //NOTE: They ask 4 ismotion() if not so they do not ask getVector() 
	  	}
	  
	  
	  //The following piece of higher art is about ActionStrength
	  if(type.isExplicitDirection()){
		  returnpoint.scale(strength.getDistance());	
	  }
		  else
		  {
			  if(type == predecessor().getType() && strength != ActionStrength.WEAK )
			  {
				  strength = ActionStrength.STRONG;
				  returnpoint.scale(strength.getDistance());
			  }
			  else if (type == predecessor().getType() && strength == ActionStrength.WEAK )
			  {
				  strength = ActionStrength.WEAK;
				  returnpoint.scale(strength.getDistance());
			  }
		  }
	  
	  	return returnpoint;
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
