package game;

/* This class encapsulates Monster position and direction
 * It also keeps a reference to the player it is tracking
 * The canView attribute can be used to limit monster visibility
 */

import packets.PlayersUpdatePacket;
import serverConnection.Connection;
import serverConnection.ConnectionHandler;
import serverConnection.Settings;

import java.util.ArrayList;

public class Monster extends Moveable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean canView = true;  // allows
	public Monster(int row, int col)
	{
	   setCell(new Position(row,col));
	}
	public Position move()
	{
		if (Settings.playerLimit < 0 || ConnectionHandler.clientsPositions.size() < Settings.playerLimit) {
			return currentCell;
		}

		ArrayList<Position> bestPath = null;

		for (int i = 0;i < ConnectionHandler.clientsPositions.size();i ++) {
			Position player = ConnectionHandler.clientsPositions.get(i);

			if (ConnectionHandler.isPlayerDead(i)) {
				continue;
			}

			if (player.col == currentCell.col && player.row == currentCell.row) {
				ConnectionHandler.deadPlayers.put(i, true);
				syncPlayerDeathStatus(i);
				continue;
			}

			ArrayList<Position> tmpPath = SearchPath.aStarSearch(currentCell, player);

			if (bestPath == null) {
				bestPath = tmpPath;
			} else if (tmpPath.size() < bestPath.size()) {
				bestPath = tmpPath;
			} else if (tmpPath.size() == bestPath.size()) {
				double max = 10;
				double min = 1;
				double x = (int)(Math.random()*((max-min)+1))+min;

				if (x % 2 == 1.0) {
					bestPath = tmpPath;
				}
			}
		}

		if (bestPath != null) {
			currentCell = bestPath.get(bestPath.size() - 2);
		}

        return currentCell;
	}
	public boolean viewable()  // can be used for hiding
	{
		return canView;
	}

	public void syncPlayerDeathStatus (int clientId) {
		PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus, ConnectionHandler.clientsPositions);
		upPacket.deathStatus = ConnectionHandler.deadPlayers;

		for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
			Connection c = ConnectionHandler.connections.get(i);
			c.sendObject(upPacket);
		}
	}
}