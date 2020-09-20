package packets;

import java.io.Serializable;

import game.Position;

public class FoodPositionPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;
	public Position foodPosition;
	
	public FoodPositionPacket(int id, Position foodPosition) {
		this.id = id;
		this.foodPosition = foodPosition;
	}
	
	
}
