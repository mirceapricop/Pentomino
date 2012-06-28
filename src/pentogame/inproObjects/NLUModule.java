package pentogame.inproObjects;

import java.util.Collection;
import java.util.List;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.IU;

/*
 * Wir sammeln ActionIUs in einer Liste, diese haben nur 4 Himmelsrichtungen, 
 * die Pragmatik macht da die Diagonalität raus. 
 * Sollte es bei Diagonalität aber zu einem "Weiter" kommen müssen wir das aber bemerken, mit einer if schleife, die bei jedem "weiter" zwei vorgänger überprüft
 * Modifikatoren müssen bei diagonalität übergeben werden
 * Pausen sind Teil der ActionIUs und werden von den Pragmatikern interpretiert
 * 
 */

public class NLUModule extends IUModule {

  // Corresponds to GreifarmActor
  
  @Override
  protected void leftBufferUpdate(Collection<? extends IU> ius,
      List<? extends EditMessage<? extends IU>> edits) {
    // TODO Auto-generated method stub
    
  }
  
}
