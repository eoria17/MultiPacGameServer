package packets;

import java.io.Serializable;

import game.Position;

public class StartingPositionPacket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Position position;

	public StartingPositionPacket(Position position) {
		this.position = position;
	}
}
