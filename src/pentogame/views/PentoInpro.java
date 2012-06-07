package pentogame.views;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.IU;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import pentogame.controllers.WorldController;

public class PentoInpro extends IUModule implements WorldView {

  public PentoInpro() {
    System.out.println("Constructing");
  }
  
  @Override
  public void init(WorldController controller) throws IOException {
  }

  @Override
  public void update() {
  }

  @Override
  public void shutDown() {
  }

  @Override
  protected void leftBufferUpdate(Collection<? extends IU> ius,
      List<? extends EditMessage<? extends IU>> edits) {
        
  } 

}
