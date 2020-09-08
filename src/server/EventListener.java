package server;

import java.sql.ConnectionBuilder;
import java.util.HashMap;

import packets.AddConnectionPacket;
import packets.ClientSettingPacket;
import packets.EmptyPacket;
import packets.PlayerPositionPacket;
import packets.PlayersUpdatePacket;
import packets.ReadyPacket;
import packets.RejectedPacket;
import packets.RemoveConnectionPacket;
import packets.SettingPacket;
import packets.StartGamePacket;
import packets.StartingPositionPacket;

public class EventListener {

	public void received(Object p, Connection connection) {

		// (Theo) If the object receive by the connection is AddConnectionPacket, it
		// will assign and ID to the connection and send the connection ID to all
		// connected user to register it on all clients.
		if (p instanceof AddConnectionPacket) {
			AddConnectionPacket packet = (AddConnectionPacket) p;
			packet.id = connection.id;
			ConnectionHandler.ServersClientReadyStatus.put(packet.id, false);
			ClientSettingPacket cPacket = new ClientSettingPacket(connection.id);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus, ConnectionHandler.clientsStartingPositions);

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				if (c == connection) {
					c.sendObject(cPacket);
					c.sendObject(upPacket);
				} else {
					c.sendObject(packet);
				}
			}

		} else if (p instanceof RemoveConnectionPacket) {
			RemoveConnectionPacket packet = (RemoveConnectionPacket) p;
			System.out.println("Connection: " + packet.id + " has disconnected");
			ConnectionHandler.connections.get(packet.id).close();
			ConnectionHandler.connections.remove(packet.id);
			connection.sendObject(packet);

			// (Theo) This is used as a first person who connect and create a game room with
			// settings. It is still need improvement
		} else if (p instanceof SettingPacket) {

			if (Settings.playerLimit == -1) {
				SettingPacket packet = (SettingPacket) p;
				Settings.playerLimit = packet.limit;
				System.out.println("current player limit set to: " + Settings.playerLimit);
			} else {
				RejectedPacket rp = new RejectedPacket("Room is already created.");
				connection.sendObject(rp);
			}

		} else if (p instanceof ReadyPacket) {
			ReadyPacket packet = (ReadyPacket) p;
			ConnectionHandler.ServersClientReadyStatus.put(packet.id, packet.ready);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus, ConnectionHandler.clientsStartingPositions);

			System.out.println("Players packet to send: " + upPacket.readyStatus);

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);

				if (c != connection) {
					System.out.println("sending to player " + (c.id + 1));
					c.sendObject(upPacket);
					System.out.println(upPacket.readyStatus);
				} else {
					c.sendObject(new EmptyPacket());
				}
			}
			
			
			//(Theo) Start game once all players are ready
			if(Settings.playerLimit == ConnectionHandler.connections.size()) {
				
				boolean startGame = true;
				
				for(boolean b : ConnectionHandler.ServersClientReadyStatus.values()) {
					if(!b) {
						startGame = false;
					}
				}
				
				if(startGame) {
					StartGamePacket startPacket = new StartGamePacket(ConnectionHandler.clientsStartingPositions);
					
					for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
						Connection c = ConnectionHandler.connections.get(i);
						
						c.sendObject(startPacket);
					}
				}
				
			}
			
			

		} else if(p instanceof StartingPositionPacket) {
			StartingPositionPacket packet = (StartingPositionPacket) p;
			
			ConnectionHandler.clientsStartingPositions.put(connection.id, packet.position);
			
			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus, ConnectionHandler.clientsStartingPositions);
			
			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);

				if (c != connection) {
					System.out.println("sending to player " + (c.id + 1));
					c.sendObject(upPacket);
					System.out.println(upPacket.readyStatus);
				} else {
					c.sendObject(new EmptyPacket());
				}
			}
		
		}else if(p instanceof PlayerPositionPacket) {
			PlayerPositionPacket packet = (PlayerPositionPacket) p;
			
			ConnectionHandler.clientsPositions.put(packet.id, packet.position);
			
			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus, ConnectionHandler.clientsPositions);
			
			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);

				if (c != connection) {
					System.out.println("sending to player " + (c.id + 1));
					c.sendObject(upPacket);
					System.out.println(upPacket.readyStatus);
				} else {
					c.sendObject(new EmptyPacket());
				}
			}
		}
	}
}
