package serverConnection;

public class Main {

	public static void main(String[] args) {
		
		serverConnection.GameServer gameServer = new GameServer(2000);
		gameServer.start();
	}

}
