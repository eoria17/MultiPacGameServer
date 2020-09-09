package packets;

import java.io.Serializable;
import java.util.HashMap;

import game.Grid;
import game.Player;
import game.Position;

public class PlayersUpdatePacket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public HashMap<Integer,Boolean> readyStatus;
	public HashMap<Integer,Position> clientsPosition;
	
	public HashMap<Integer, Player> clientsPlayerInfo = new HashMap<Integer, Player>();
	
	public Grid grid;

	public PlayersUpdatePacket(HashMap<Integer, Boolean> readyStatus, HashMap<Integer, Position> clientsPosition) {
		this.readyStatus = readyStatus;
		this.clientsPosition = clientsPosition;
		
		grid = new Grid();
		
	}
	
	public PlayersUpdatePacket(HashMap<Integer, Boolean> readyStatus, HashMap<Integer, Position> clientsPosition, Grid grid) {
		this.readyStatus = readyStatus;
		this.clientsPosition = clientsPosition;
		this.grid = grid;
		
		updateClientPos();
	}
	
	public void updateClientPos() {
		for(int i : clientsPosition.keySet()) {
			try {
				Player p = new Player(grid, clientsPosition.get(i).getRow(), clientsPosition.get(i).getCol());
				
				clientsPlayerInfo.put(i, p);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
