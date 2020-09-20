package packets;

import java.io.Serializable;
import java.util.HashMap;

import game.Player;
import game.Position;

public class PlayersUpdatePacket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public HashMap<Integer,Boolean> readyStatus;
	public HashMap<Integer,Position> clientsPosition;
	public HashMap<Integer,Boolean> deathStatus;
	public HashMap<Integer, Position> foodPositions;
	
	public HashMap<Integer, Player> clientsPlayerInfo = new HashMap<Integer, Player>();

	public PlayersUpdatePacket(HashMap<Integer, Boolean> readyStatus, HashMap<Integer, Position> clientsPosition, HashMap<Integer, Position> foodPosition) {
		this.readyStatus = readyStatus;
		this.clientsPosition = clientsPosition;
		this.foodPositions = foodPosition;
	}
}
