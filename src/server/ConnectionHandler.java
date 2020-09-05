package server;

import java.util.HashMap;

public class ConnectionHandler {

	//(Theo) This is used to manage or identify the connected clients. This makes it easier to send which data to which clients.
	public static HashMap<Integer,Connection> connections = new HashMap<Integer,Connection>();
	public static HashMap<Integer,Boolean> ServersClientReadyStatus = new HashMap<Integer, Boolean>();
	
}
