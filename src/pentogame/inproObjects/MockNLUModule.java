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
         WordIU word = (WordIU) em.getIU();
         if(word.getWord().toLowerCase().equals("stop")) {
           output.add(MockActionIU.newStop());
           return;
         }
         
         if(word.getWord().equals("drop")) {
           System.out.println("Drop");
           return;
         }
         
         if(word.getWord().equals("cancel")) {
           System.out.println("Cancel");
           return;
         }
         
         String[] components = word.getWord().split(",");
         int x = 0;
         int y = 0;
         
         try { x = Integer.parseInt(components[0]); } catch(Exception e) {}
         try { y = Integer.parseInt(components[1]); } catch(Exception e) {}
         output.add(new MockActionIU(x, y));
       } else if(em.getType() == EditType.REVOKE) {
         output.removeLast();
       }
     }
     rightBuffer.setBuffer(output);
   }

}
