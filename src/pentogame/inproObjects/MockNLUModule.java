package pentogame.inproObjects;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.EditType;
import inpro.incremental.unit.IU;
import inpro.incremental.unit.WordIU;

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
         MockActionIU iu;
         WordIU word = (WordIU) em.getIU();
         if(word.getWord().equals("stop")) {
           iu = MockActionIU.newStop();
         } else if(word.getWord().equals("drop")) {
           iu = MockActionIU.newDrop();
         } else if(word.getWord().equals("cancel")) {
           iu = MockActionIU.newCancel();
         } else {         
           String[] components = word.getWord().split(",");
           int x = 0;
           int y = 0;
         
           try { x = Integer.parseInt(components[0]); } catch(Exception e) {}
           try { y = Integer.parseInt(components[1]); } catch(Exception e) {}
           iu = new MockActionIU(x, y);
         }
         iu.setSameLevelLink(output.peekLast());
         output.add(iu);
       } else if(em.getType() == EditType.REVOKE) {
         output.removeLast();
       }
     }
     rightBuffer.setBuffer(output);
   }

}
