package serverConnection;

import java.util.HashMap;

import game.Position;

public class ConnectionHandler {

	//(Theo) This is used to manage or identify the connected clients. This makes it easier to send which data to which clients.
	public static HashMap<Integer,Connection> connections = new HashMap<Integer,Connection>();
	public static HashMap<Integer,Boolean> ServersClientReadyStatus = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Position> clientsStartingPositions = new HashMap<Integer, Position>();
	public static HashMap<Integer, Position> clientsPositions = new HashMap<Integer, Position>();
	public static HashMap<Integer, Boolean> deadPlayers = new HashMap<Integer, Boolean>();
	public static HashMap<Integer, Boolean> rematchPlayers = new HashMap<>();
	public static HashMap<Integer, Position> foodPositions = new HashMap<Integer, Position>();
	public static Position[] gridObstacles;

	public static boolean isPlayerDead(int playerId) {
		if (deadPlayers.get(playerId) != null) {
			return true;
		} else {
			return false;
		}
	}
}
