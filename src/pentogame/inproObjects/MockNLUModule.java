package pentogame.inproObjects;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.EditType;
import inpro.incremental.unit.IU;

public class MockNLUModule extends IUModule {

//Corresponds to GreifarmActor
   
   private Deque<MockActionIU> output;
 
   public MockNLUModule() {
    output = new ArrayDeque<MockActionIU>();
   }
  
   @Override
   protected void leftBufferUpdate(Collection<? extends IU> ius,
       List<? extends EditMessage<? extends IU>> edits) {
     for(EditMessage<? extends IU> em : edits) {
       if(em.getType() == EditType.ADD) {
         output.add(new MockActionIU());
       } else if(em.getType() == EditType.REVOKE) {
         output.pop();
       }
     }
     rightBuffer.setBuffer(output);
   }

}
