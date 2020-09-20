package serverConnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//(Theo) This class is a thread called connection to communicate with the client. Each client will be managed by one connection thread.
public class Connection implements Runnable {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public int id;
	private EventListener listener;

	// (Theo) here we use the constructor to set the object stream in and out so we
	// can send and receive object from the connected client.
	public Connection(Socket socket, int id) {
		this.socket = socket;
		this.id = id;

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			listener = new EventListener();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {
		try {
			while (!socket.isClosed()) {
				try {
					// (Theo) The thread will not continue as in it will be blocked, waiting to
					// receive data from the client.
					// this is where the server receive data from the client. The listener is used
					// to differentiate what packets
					// was sended from the client. It will act as the game logic and data management
					Object data = in.readObject();
					listener.received(data, this);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (EOFException e) {
					close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendObject(Object packet) {
		try {
			if (!socket.isClosed()) {
				out.writeObject(packet);
				out.flush();
				out.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (!socket.isClosed()) {
				close();
			}
		}
	}

}
