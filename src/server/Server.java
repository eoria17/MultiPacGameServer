package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import packets.RejectedPacket;

//(Theo) This is the main server thread. It will be used to wait for a new incoming connection and create a connection socket and thread for the connected client.
public class Server implements Runnable {

	private int port;
	private ServerSocket serverSocket;
	private boolean running = false;
	private int id = 0;

	public Server(int port) {
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
		System.out.println("Size before:" + ConnectionHandler.connections.size());
		if((settings.playerLimit + 1) == ConnectionHandler.connections.size()) {
			RejectedPacket rp = new RejectedPacket("Room is full, cannot join room.");
			connection.sendObject(rp);
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
}
