package pentogame.views;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.IU;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import pentogame.Startup;
import pentogame.controllers.WorldController;

public class PentoInpro extends IUModule implements WorldView {

  private static PentoInpro instance;
  
  public PentoInpro() {
    try {
      instance = this;
      Startup.main(null);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public static PentoInpro getInstance() {
    return instance;
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
    System.out.println("Got IU update:" + ius);
  } 

}
