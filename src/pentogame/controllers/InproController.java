package pentogame.controllers;

import java.util.ArrayList;
import java.util.List;

import pentogame.inproObjects.Point;
import pentogame.objects.Board;
import pentogame.objects.HandCursor;
import pentogame.objects.Piece;
import pentogame.objects.Target;
import pentogame.views.WorldView;

public class InproController {
  
  private Piece piece;
  private HandCursor hand;
  private Board board;
  private int targetLeft;
  private int targetTop;
  
  private int originalLeft;
  private int originalTop;
  
  private int startLeft;
  private int startTop;
  
  private double realLeft;
  private double realTop;
  
  private Thread mover;
  private int speed = 50; // px / sec
  private long lastMove;
  
  private final int MIN_LEFT = 136;
  private final int MIN_TOP = 260;
  private final int MAX_LEFT = 466;
  private final int MAX_TOP = 530;
  
  private volatile boolean stopMovement;
  
  private List<WorldView> _views;
  
  private final Object inproLock = new Object();
  
  public InproController(Piece p, HandCursor h, Board b, Target t) {
    piece = p;
    hand = h;
    board = b;
    targetLeft = originalLeft = t.left;
    targetTop = originalTop = t.top;
    
    startLeft = p.left;
    startTop = p.top;
    
    realLeft = piece.left;
    realTop = piece.top;
    stopMovement = false;
    _views = new ArrayList<WorldView>();
  }
  
  public void updateViews() {
    for(WorldView v : _views) {
      v.update();
    }
  }// TODO Auto-generated method stub
  
  public void setViews(List<WorldView> l) {
    _views = l;
  }
  
  public void _wait() throws InterruptedException {
    mover = new Thread(new Runnable() {
      @Override
      public void run() {
        boolean finished = false;
        lastMove = System.currentTimeMillis();
        while(!finished) {
          long now = System.currentTimeMillis();
          int elapsed = (int) (now - lastMove);
          lastMove = now;
         
          double distance = ((elapsed)/1000.0)*speed;
          double theta = Math.atan2((targetTop - realTop), (targetLeft - realLeft));
          realLeft += distance * Math.cos(theta);
          realTop += distance * Math.sin(theta);
          
          piece.left = (int) realLeft;
          piece.top = (int) realTop;
          hand.left = piece.left+piece.getTemplateCols()*board.grid_size/2-25;
          hand.top = piece.top+piece.getTemplateRows()*board.grid_size/2-25;
          if(stopMovement) {
            if(Math.abs(piece.left - targetLeft) < 5 && Math.abs(piece.top - targetTop) < 5) {
              finished = true;
              piece.left = targetLeft;
              piece.top = targetTop;
              hand.left = piece.left+piece.getTemplateCols()*board.grid_size/2-25;
              hand.top = piece.top+piece.getTemplateRows()*board.grid_size/2-25;
              synchronized(inproLock) {
                inproLock.notifyAll();
              }
            }
          }
          updateViews();
        }
      }
    });
    mover.start();
    
    synchronized (inproLock) {
      inproLock.wait(); 
    }
  }

  public Point moveTarget(double dx, double dy) {
    targetLeft += dx*board.grid_size;
    targetTop += dy*board.grid_size;
    
    targetLeft = Math.max(MIN_LEFT, targetLeft);
    targetLeft = Math.min(MAX_LEFT, targetLeft);
    targetTop = Math.max(MIN_TOP, targetTop);
    targetTop = Math.min(MAX_TOP, targetTop);
    
    return new Point(targetLeft, targetTop);
  }
  
  public void stopMove() {
    setTarget(closestGridPoint());
  }

  public void setTarget(Point target) {
    targetLeft = target.getX();
    targetTop = target.getY();
  }

  public void resetTarget() {
    targetLeft = originalLeft;
    targetTop = originalTop;
  }
  
  private Point closestGridPoint() {
    int closestX = (int) (Math.round((piece.left - MIN_LEFT)/(1.0*board.grid_size)) * board.grid_size);
    int closestY = (int) (Math.round((piece.top - MIN_TOP)/(1.0*board.grid_size)) * board.grid_size);
    return new Point(closestX+MIN_LEFT, closestY+MIN_TOP);
  }

  public void drop() {
    setTarget(closestGridPoint());
    stopMovement = true;
  }

  public void cancel() {
    setTarget(new Point(startLeft, startTop));
    stopMovement = true;
  }
}
