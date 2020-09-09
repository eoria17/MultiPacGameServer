package packets;

import java.io.Serializable;

import game.Grid;
import game.Position;

public class PlayerPositionPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;
	public Position position;
	public Grid grid;
	
	public PlayerPositionPacket(int id, Position position, Grid grid) {
		this.id = id;
		this.position = position;
		this.grid = grid;
	}
	
	
}
