package pentogame.inproObjects;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.IU;

public class NluModule extends IUModule {

  // Corresponds to GreifarmActor
   
  private Deque<ActionIU> output;
	
  public NluModule() {
	  output = new ArrayDeque<ActionIU>();
  }
  
  @Override
  protected void leftBufferUpdate(Collection<? extends IU> ius,
      List<? extends EditMessage<? extends IU>> edits) {
	System.out.println("NluActor got IU");
	output.push(new ActionIU());
    rightBuffer.setBuffer(output);
  }
  
}
