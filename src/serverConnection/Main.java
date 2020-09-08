package serverConnection;

import serverConnection.GameServer;

public class Main {

	public static void main(String[] args) {
		
		GameServer gameServer = new GameServer(2000);
		gameServer.start();
	}

}
