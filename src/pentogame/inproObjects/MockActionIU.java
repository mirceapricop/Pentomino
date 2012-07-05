package pentogame.inproObjects;

import java.util.Random;

import inpro.incremental.unit.IU;

public class MockActionIU extends AbstractActionIU {

  private Point vector;
  
  public MockActionIU() {
    Random r = new Random();
    vector = new Point(r.nextInt(10)-5, r.nextInt(10)-5);
  }
  
  @Override
  public String toPayLoad() {
    return null;
  }

  @Override
  public Point getVector() {
    return vector;
  }
  
  @Override
  public ActionType getType() {
    return ActionType.UP;
  }

}
