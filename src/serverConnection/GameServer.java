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
		HashMap<String, String> connectedCells = new HashMap<>();

		connectedCells.put("5_5", "5_5");

		for (int i=1; i<=row - 1; i+=2) {
			for (int j=1; j<=col - 1; j+=2) {
				connectCells(i, j, connectedCells, positions, i+"_"+j);
			}
		}


		Position results[] = new Position[positions.size()];
		positions.toArray(results);

		return results;
	}

	private static void connectCells(int i, int j, HashMap<String, String> connectedCells,
									 ArrayList<Position> positions,
									 String group) {
		boolean turn = false;
		int nextI = i;
		int nextJ = j;
		int count = 5;
		boolean stop = false;

		while (!stop) {
			if (isCellAvailable(nextI, nextJ, connectedCells, group)) {
				connectedCells.put(nextI + "_" + nextJ, group);
				positions.add(new Position(nextI, nextJ));
			}

			if (count < 4) {
				if (count == 0) {
					break;
				} else {
					stop = randomChoice();
				}
			}

			if (!turn) {
				turn = randomChoice();
			}

			if (turn) {
				nextI = nextI+1;
			} else {
				nextJ = nextJ+1;
			}

//			if (isCellAvailable(nextI, nextJ, connectedCells, group)) {
//				connectedCells.put(nextI + "_" + nextJ, group);
//				positions.add(new Position(nextI, nextJ));
//			}

			if (count == 5) {
				turn = false;
			}

			count --;
		}
	}

	private static boolean isCellAvailable(int i, int j, HashMap<String, String> connectedCells, String group) {
		int row = 11 - 1;
		int col = 11 - 1;

		int nextI = i;
		int nextJ = j;
		String key = nextI + "_" + nextJ;
		if ((nextI == 5 && nextJ == 5) || nextI > row || nextJ > col || connectedCells.containsKey(key)) {
			return false;
		}
		nextI = i-1;
		nextJ = j;
		key = nextI + "_" + nextJ;
		if (connectedCells.containsKey(key) && !connectedCells.get(key).equals(group)) {
			return false;
		}
		nextI = i+1;
		nextJ = j;
		key = nextI + "_" + nextJ;
		if (i == row || (connectedCells.containsKey(key) && !connectedCells.get(key).equals(group))) {
			return false;
		}
		nextI = i;
		nextJ = j-1;
		key = nextI + "_" + nextJ;
		if (connectedCells.containsKey(key) && !connectedCells.get(key).equals(group)) {
			return false;
		}
		nextI = i;
		nextJ = j+1;
		key = nextI + "_" + nextJ;
		if (j == col || (connectedCells.containsKey(key) && !connectedCells.get(key).equals(group))) {
			return false;
		}
		return true;
	}

	private static boolean randomChoice() {
		double max = 10;
		double min = 1;
		double x = (int) (Math.random() * ((max - min) + 1)) + min;

		if (x % 2 == 1.0) {
			return true;
		} else {
			return false;
		}
	}
}
