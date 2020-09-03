package server;

import packets.AddConnectionPacket;
import packets.RemoveConnectionPacket;
import packets.SettingPacket;

public class EventListener {
	public void received(Object p, Connection connection) {

		//(Theo) If the object receive by the connection is AddConnectionPacket, it will assign and ID to the connection and send the connection ID to all
		//connected user to register it on all clients.
		if (p instanceof AddConnectionPacket) {
			AddConnectionPacket packet = (AddConnectionPacket) p;
			packet.id = connection.id;
			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				if (c != connection) {
					c.sendObject(packet);
				}
			}

		} else if (p instanceof RemoveConnectionPacket) {
			RemoveConnectionPacket packet = (RemoveConnectionPacket) p;
			System.out.println("Connection: " + packet.id + " has disconnected");
			ConnectionHandler.connections.get(packet.id).close();
			ConnectionHandler.connections.remove(packet.id);
		
			//(Theo) This is used as a first person who connect and create a game room with settings. It is still need improvement
		}else if(p instanceof SettingPacket) {
			SettingPacket packet = (SettingPacket) p;
			settings.playerLimit = packet.limit;
			System.out.println("current player limit set to: " + settings.playerLimit);
		}
	}
}
