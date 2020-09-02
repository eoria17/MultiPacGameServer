package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import packets.RejectedPacket;

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
		
		if(settings.playerLimit + 1 != ConnectionHandler.connections.size()) {
			RejectedPacket rp = new RejectedPacket("Room is full, cannot join room.");
			connection.sendObject(rp);
			return;
		}
		
		ConnectionHandler.connections.put(id, connection);
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