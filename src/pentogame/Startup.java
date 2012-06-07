// Version: 0.7.0
package pentogame;

import java.io.IOException;

import pentogame.controllers.WorldController;
import pentogame.models.BoardModel;
import pentogame.models.HandModel;
import pentogame.models.PieceModel;
import pentogame.views.PentoCanvas;
import pentogame.views.PentoCommand;
import pentogame.views.PentoDialog;

public class Startup {

  public static void main(String[] args) throws IOException {
    WorldController world = new WorldController();
    world.addView(new PentoCanvas());
    //world.addView(new PentoCommand());
    world.addView(new PentoDialog());
    world.addModel(new BoardModel());
    world.addModel(new PieceModel());
    world.addModel(new HandModel());
    
    world.run();
  }

}
