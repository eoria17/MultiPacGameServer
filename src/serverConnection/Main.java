package serverConnection;

import game.Monster;
import serverConnection.GameServer;

public class Main {

	public static void main(String[] args) {
		
		GameServer gameServer = new GameServer(2000);
		gameServer.start();

		Monster monster = new Monster(5, 5);
		MonsterThread monsterThread = new MonsterThread(monster);
		new Thread(monsterThread).start();
	}

}
