package game;

import java.io.Serializable;

/*  This class encapsulates player position and direction  
 */
public class Player extends Moveable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean readyToStart = false;
	private boolean hasFood = true;
	private boolean canDropFood = true;
	
	private Position foodPosition;

	public Player(Grid g, int row, int col) throws Exception {
		super(g);
		currentCell = grid.getCell(row, col);
		currentDirection = ' ';
		foodPosition = new Position(0,0);
	}

	public Position move() {
		currentCell = grid.getCell(currentCell, currentDirection);
		return currentCell;
	}

	public int maxCellsPerMove() {
		return 1;
	}
	
	public boolean dropableFood() {
		return canDropFood;
	}
	
	public void setNoMoreFood() {
		canDropFood = false;
	}

	public boolean hasFood() {
		return hasFood;
	}

	public void setHasFood(boolean hasFood) {
		this.hasFood = hasFood;
	}
	
	public void dropFood() {
		foodPosition = currentCell;
		hasFood = false;
	}
	
	public Position getFoodPosition() {
		return foodPosition;
	}

	public void setReady(boolean val) {
		readyToStart = val;
	}

	public boolean isReady() {
		return readyToStart;
	}
}