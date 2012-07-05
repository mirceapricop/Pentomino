package pentogame.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pushingpixels.trident.Timeline;

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
  
  private double realLeft;
  private double realTop;
  
  private Thread mover;
  private int speed = 50; // px / sec
  private long lastMove;
  
  private List<WorldView> _views;
  
  private final Object inproLock = new Object();
  
  public InproController(Piece p, HandCursor h, Board b, Target t) {
    piece = p;
    hand = h;
    board = b;
    targetLeft = originalLeft = t.left;
    targetTop = originalTop = t.top;
    realLeft = piece.left;
    realTop = piece.top;
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
  
  public void doStuff() {
    targetLeft = (int) (Math.random()*330) + 136;
    targetTop = (int) (Math.random()*270) + 260;
  }
  
  public void _wait() throws InterruptedException {
    mover = new Thread(new Runnable() {
      @Override
      public void run() {
        lastMove = System.currentTimeMillis();
        while(true) {
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
    
    targetLeft = Math.max(136, targetLeft);
    targetLeft = Math.min(466, targetLeft);
    targetTop = Math.max(260, targetTop);
    targetTop = Math.min(530, targetTop);
    
    return new Point(targetLeft, targetTop);
  }

  public void setTarget(Point target) {
    targetLeft = target.getX();
    targetTop = target.getY();
  }

  public void resetTarget() {
    targetLeft = originalLeft;
    targetTop = originalTop;
  }
}
