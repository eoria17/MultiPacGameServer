package packets;

import java.io.Serializable;
import java.util.HashMap;

import game.Position;

public class StartGamePacket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public HashMap<Integer,Position> clientsPosition;

	public StartGamePacket(HashMap<Integer, Position> clientsPosition) {
		this.clientsPosition = clientsPosition;
	}
	
	
	
	
}
