package pentogame.inproObjects;

import java.util.Random;

public class MockActionIU extends AbstractActionIU {

  private Point vector;
  public ActionType type;
  
  public MockActionIU() {
    Random r = new Random();
    vector = new Point(r.nextInt(10)-5, r.nextInt(10)-5);
    type = ActionType.UP;
  }
  
  public MockActionIU(int x, int y) {
    vector = new Point(x, y);
    type = ActionType.UP;
  }
  
  @Override
  public String toPayLoad() {
    return null;
  }

  @Override
  public Point getVector() {
    return vector;
  }
  
  public static MockActionIU newStop() {
    MockActionIU result = new MockActionIU();
    result.type = ActionType.STOP;
    return result;
  }
  
  @Override
  public ActionType getType() {
    return type;
  }

}
