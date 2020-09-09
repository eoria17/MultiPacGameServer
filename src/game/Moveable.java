package game;

import java.io.Serializable;

/* The abstract base class for Moster and Player
 * The abstract method move() must be overridden by Player and Monster classes
 */

public abstract class Moveable implements Serializable{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
protected char currentDirection;  
   protected Position currentCell;
   protected Grid grid;
   public Moveable(Grid g)
   {
	   grid = g;
   }
   public void setDirection(char d)
   {
	   currentDirection = d;
   }
   public char getDirection()
   {
      return currentDirection;	    
   }
   public void setCell(Position c)
   {
	   currentCell = c;
   }
   public Position getCell()
   {
      return currentCell;	    
   }
   public abstract Position move();
}