package game;

import java.io.Serializable;

/*  This class encapsulates player position and direction  
 */
public class Player extends Moveable implements Serializable{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private boolean readyToStart = false;
   public Player(Grid g, int row, int col) throws Exception
   {
	   super(g);
	   currentCell = grid.getCell(row, col);   
	   currentDirection = ' ';
   }
   public Position move()
   {
       currentCell = grid.getCell(currentCell,currentDirection);	          
       return currentCell;
   }
   public int maxCellsPerMove()
   {
	   return 1;
   }
   public  int pointsRemaining()
   {
	   return -1;  // not implemented
   }
   public void setReady(boolean val)
   {
	   readyToStart = val;
   }
   public boolean isReady()
   {   return readyToStart;
   }
}