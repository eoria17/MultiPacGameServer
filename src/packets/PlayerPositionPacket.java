package packets;

import java.io.Serializable;

import game.Position;

public class PlayerPositionPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;
	public Position position;
	
	public PlayerPositionPacket(int id, Position position) {
		this.id = id;
		this.position = position;
	}
	
	
}
