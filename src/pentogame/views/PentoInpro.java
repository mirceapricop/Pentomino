package pentogame.views;

import inpro.incremental.IUModule;
import inpro.incremental.unit.EditMessage;
import inpro.incremental.unit.EditType;
import inpro.incremental.unit.IU;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pentogame.Startup;
import pentogame.controllers.InproController;
import pentogame.controllers.WorldController;
import pentogame.inproObjects.MockActionIU;
import pentogame.inproObjects.Point;

public class PentoInpro extends IUModule implements WorldView {

  private static PentoInpro instance;
  private InproController controller;
  
  public PentoInpro() {
    try {
      instance = this;
      controller = null;
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
  
  public void setController(InproController c) {
    controller = c;
  }

  @Override
  protected void leftBufferUpdate(Collection<? extends IU> ius,
      List<? extends EditMessage<? extends IU>> edits) {
    if(controller != null) {
      for(EditMessage<? extends IU> em : edits) {
        MockActionIU iu = (MockActionIU) em.getIU();
        if(em.getType() == EditType.ADD) {
          if(iu.getType().isStop()) {
            controller.stopMove();
          }
          
          if(iu.getType().isMotion()) {
            Point actionTarget = controller.moveTarget(iu.getVector().getX(), iu.getVector().getY());
            iu.setTarget(actionTarget);
          }
        } else if (em.getType() == EditType.REVOKE) {
          if(iu.predecessor() != null) {
            controller.setTarget(iu.predecessor().getTarget());
          } else {
            controller.resetTarget();
          }
        }
      }
    }
  } 

}
