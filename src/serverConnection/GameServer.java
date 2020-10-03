package serverConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import game.Position;
import packets.RejectedPacket;

//(Theo) This is the main server thread. It will be used to wait for a new incoming connection and create a connection socket and thread for the connected client.
public class GameServer implements Runnable {

	private int port;
	private ServerSocket serverSocket;
	private boolean running = false;
	private int id = 0;

	public GameServer(int port) {
		this.port = port;

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {

		running = true;
		System.out.println("Server started on port: " + port);

		while (running) {
			try {

					Socket socket = serverSocket.accept();
					initSocket(socket);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		shutdown();

	}

	private void initSocket(Socket socket) {
		Connection connection = new Connection(socket, id);
		ConnectionHandler.connections.put(id, connection);
		
		//(Theo) This will send a rejection to the newly connected client if the capacity is full
		System.out.println("Size:" + ConnectionHandler.connections.size());
		if((Settings.playerLimit + 1) == ConnectionHandler.connections.size()) {
			RejectedPacket rp = new RejectedPacket("Room is full, cannot join room.");
			connection.sendObject(rp);
			ConnectionHandler.connections.remove(id);
			connection.close();
			return;
		}
		
		
		new Thread(connection).start();
		id++;
	}

	public void shutdown() {
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Position[] generateGridObstacles() {
		int row = 11;
		int col = 11;

		ArrayList<Position> positions = new ArrayList<>();
		ArrayList<Position> tmpPositions = generatePath();

		for (Position tmpPosition : tmpPositions) {
			int tmpRow = tmpPosition.getRow();
			int tmpCol = tmpPosition.getCol();
			positions.add(new Position(tmpCol + 1, tmpRow + 1));
			positions.add(new Position(tmpCol + 1, 9 - tmpRow));
		}

		tmpPositions = generatePath();
		for (Position tmpPosition : tmpPositions) {
			int tmpRow = tmpPosition.getRow();
			int tmpCol = tmpPosition.getCol();
			positions.add(new Position(tmpCol + 6, tmpRow + 1));
			positions.add(new Position(tmpCol + 6, 9 - tmpRow));
		}

		Position[] results = new Position[positions.size()];
		positions.toArray(results);

		return results;
	}

	private static ArrayList<Position> generatePath() {
		int row = 4;
		int col = 4;
		int upPass = randomChoice() ? 1 : 2;
		int leftPass = randomChoice() ? 1 : 2;
		int rightPass = randomChoice() ? 1 : 2;
		int bottomPass = randomChoice() ? 1 : 2;

		if (bottomPass == 1) {
			rightPass = 1;
		}

		ArrayList<Position> positions = new ArrayList<>();

		for (int i=0; i<col; i++) {
			for (int j=0; j<row; j++) {
				if (!((i == upPass && j <= leftPass) ||
						(i <= upPass && j == leftPass) ||
						(i == bottomPass && j >= rightPass) ||
						(i >= bottomPass && j == rightPass))) {
					positions.add(new Position(j,i));
				}
			}
		}

		return positions;
	}

	private static boolean randomChoice() {
		double max = 10;
		double min = 1;
		double x = (int) (Math.random() * ((max - min) + 1)) + min;

		return x % 2 == 1.0;
	}
}
