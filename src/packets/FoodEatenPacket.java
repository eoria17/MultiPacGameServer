package packets;

import java.io.Serializable;

import game.Position;

public class FoodEatenPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;

	public FoodEatenPacket(int id) {
		this.id = id;
	}
	
	

}
