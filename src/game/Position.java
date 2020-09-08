package game;

import java.io.Serializable;

/* This class represents the individual cell in the grid.
 */

public class Position implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int row;
	protected int col;

	boolean gotGold = true;

	public Position(int i, int j) {
		row = i;
		col = j;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}
