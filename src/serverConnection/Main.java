package serverConnection;

public class Main {

	public static void main(String[] args) {
		
		GameServer gameServer = new GameServer(8080);
		gameServer.start();
	}

}
