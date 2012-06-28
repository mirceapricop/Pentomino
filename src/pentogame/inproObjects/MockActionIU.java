package pentogame.inproObjects;

import java.util.Random;

import inpro.incremental.unit.IU;

public class MockActionIU extends IU {

  private double[] vector;
  
  public class Type {
    public boolean isMotion() {
      return true;
    }
  }
  
  public MockActionIU() {
    Random r = new Random();
    vector = new double[2];
    vector[0] = r.nextInt(10)-5;
    vector[1] = r.nextInt(10)-5;
  }
  
  @Override
  public String toPayLoad() {
    return null;
  }
  
  public Type getType() {
    return new Type();
  }
  
  public double[] getVector() {
    return vector;
  }

}
