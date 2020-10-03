package serverConnection;

import static java.lang.Thread.sleep;

import game.Monster;
import game.Position;
import packets.FoodEatenPacket;
import packets.MonsterPositionPacket;

public class MonsterThread implements Runnable {
	Monster monster;
	private Position monsterPosition;

	public int timeDelay = 0;

	public MonsterThread(Monster monster) {
		this.monster = monster;
		monsterPosition = monster.getCell();
	}

	@Override
	public void run() {
		boolean stopRunning = false;

		while (!stopRunning) {

			if (timeDelay == 0) {
				monsterPosition = monster.move();

				MonsterPositionPacket positionPacket = new MonsterPositionPacket(monsterPosition);
				for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
					Connection c = ConnectionHandler.connections.get(i);
					c.sendObject(positionPacket);
				}

			} else {
				timeDelay--;
			}

			for (int p : ConnectionHandler.foodPositions.keySet()) {
				if (monsterPosition.getRow() == ConnectionHandler.foodPositions.get(p).getRow() && monsterPosition.getCol() == ConnectionHandler.foodPositions.get(p).getCol()) {
					timeDelay = 2;

					FoodEatenPacket packet = new FoodEatenPacket(p);
					for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
						Connection c = ConnectionHandler.connections.get(i);
						c.sendObject(packet);
					}
					
					ConnectionHandler.foodPositions.remove(p);
					break;
				}
			}

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// if all players are eaten by the monster then stop send packages.
			stopRunning = ConnectionHandler.deadPlayers.size() == Settings.playerLimit;
		}
	}
}
