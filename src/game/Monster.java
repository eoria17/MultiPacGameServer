package game;

/* This class encapsulates Monster position and direction
 * It also keeps a reference to the player it is tracking
 * The canView attribute can be used to limit monster visibility
 */

import serverConnection.ConnectionHandler;
import serverConnection.Settings;

import java.util.ArrayList;

public class Monster extends Moveable {
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

			if (player.col == currentCell.col && player.row == currentCell.row) {
				continue;
			}

			ArrayList<Position> tmpPath = SearchPath.aStarSearch(currentCell, player);

			if (bestPath == null || tmpPath.size() < bestPath.size()) {
				bestPath = tmpPath;
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
}