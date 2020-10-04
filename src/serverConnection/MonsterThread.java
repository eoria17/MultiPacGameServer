package serverConnection;

import static java.lang.Thread.sleep;

import game.Monster;
import game.Position;
import game.SearchPath;
import packets.FoodEatenPacket;
import packets.MonsterPositionPacket;

import java.util.ArrayList;

public class MonsterThread implements Runnable {
	Monster[] monsters;
	private ArrayList<Position> monsterPositions;

	public int timePlay = 0;

	// the time limit is the same as the client
	private final int TIMEALLOWED = 100;

	public MonsterThread(Monster[] monsters) {
		this.monsters = monsters;
	}

	@Override
	public void run() {
		boolean stopRunning = false;

		while (!stopRunning) {
			this.monsterPositions = new ArrayList<>();

			for (int i = 0; i < monsters.length; i++) {
				Monster monster = monsters[i];
				if (monster.delay != 0) {
					monster.delay--;
					monster.setPathTocurrentPosition();
					monsterPositions.add(monster.getCell());
					continue;
				}
				SearchPath.otherMonsterPath = i == 0 ? null : monsters[i - 1].getPath();
				Position monsterPosition = monster.move();
				monsterPositions.add(monsterPosition);

				for (int p : ConnectionHandler.foodPositions.keySet()) {
					if (monsterPosition.getRow() == ConnectionHandler.foodPositions.get(p).getRow() && monsterPosition.getCol() == ConnectionHandler.foodPositions.get(p).getCol()) {
						monster.delay = 2;

						FoodEatenPacket packet = new FoodEatenPacket(p);
						for (int j = 0; j < ConnectionHandler.connections.size(); j++) {
							Connection c = ConnectionHandler.connections.get(j);
							c.sendObject(packet);
						}

						ConnectionHandler.foodPositions.remove(p);
						break;
					}
				}
			}

			Position[] positions = new Position[monsterPositions.size()];
			monsterPositions.toArray(positions);
			MonsterPositionPacket positionPacket = new MonsterPositionPacket(positions);
			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				c.sendObject(positionPacket);
			}

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// if only one player is left
			// or if the time is up
			// then stop send packages.
			if (timePlay >= TIMEALLOWED) {
				stopRunning = true;
			} else if (Settings.playerLimit > 1) {
				stopRunning = ConnectionHandler.deadPlayers.size() == Settings.playerLimit - 1;
			} else {
				stopRunning = ConnectionHandler.deadPlayers.size() == Settings.playerLimit;
			}

			timePlay ++;
		}
	}
}
