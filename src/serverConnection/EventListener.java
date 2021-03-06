package serverConnection;

import java.util.HashMap;

import game.Monster;
import game.Position;
import packets.*;

public class EventListener {

	public synchronized void received(Object p, Connection connection) {

		// (Theo) If the object receive by the connection is AddConnectionPacket, it
		// will assign and ID to the connection and send the connection ID to all
		// connected user to register it on all clients.
		if (p instanceof AddConnectionPacket) {
			AddConnectionPacket packet = (AddConnectionPacket) p;
			packet.id = connection.id;
			ConnectionHandler.ServersClientReadyStatus.put(packet.id, false);
			ClientSettingPacket cPacket = new ClientSettingPacket(connection.id);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus,
					ConnectionHandler.clientsStartingPositions, ConnectionHandler.foodPositions);

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				if (c == connection) {
					c.sendObject(cPacket);
					c.sendObject(upPacket);
				} else {
					c.sendObject(packet);
					c.sendObject(upPacket);
				}
			}

		} else if (p instanceof RemoveConnectionPacket) {
			RemoveConnectionPacket packet = (RemoveConnectionPacket) p;
			System.out.println("Connection: " + packet.id + " has disconnected");
			connection.sendObject(packet);
			ConnectionHandler.connections.get(packet.id).close();
			ConnectionHandler.connections.remove(packet.id);

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
				ConnectionHandler.connections.remove(connection.id);
				connection.close();
			}

		} else if (p instanceof ReadyPacket) {
			ReadyPacket packet = (ReadyPacket) p;
			ConnectionHandler.ServersClientReadyStatus.put(packet.id, packet.ready);
			System.out.println(ConnectionHandler.ServersClientReadyStatus);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus,
					ConnectionHandler.clientsStartingPositions, ConnectionHandler.foodPositions);

			System.out.println("Players packet to send: " + upPacket.readyStatus);

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);

				if (c != connection) {
					System.out.println("sending to player " + (c.id + 1));
					c.sendObject(upPacket);
					System.out.println(upPacket.readyStatus);
				}
			}

			// (Theo) Start game once all players are ready
			if (Settings.playerLimit == ConnectionHandler.connections.size()) {

				boolean startGame = true;

				for (boolean b : ConnectionHandler.ServersClientReadyStatus.values()) {
					if (!b) {
						startGame = false;
					}
				}

				if (startGame) {
					Position[] gridObstacles = GameServer.generateGridObstacles();
					ConnectionHandler.gridObstacles = gridObstacles;
					StartGamePacket startPacket = new StartGamePacket(ConnectionHandler.clientsStartingPositions,
							gridObstacles);

					for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
						Connection c = ConnectionHandler.connections.get(i);

						c.sendObject(startPacket);
					}
					
					Monster[] monsters = new Monster[]{new Monster(5, 5), new Monster(5, 5)};
					MonsterThread monsterThread = new MonsterThread(monsters);
					new Thread(monsterThread).start();
				}

			}

		} else if (p instanceof StartingPositionPacket) {
			StartingPositionPacket packet = (StartingPositionPacket) p;

			ConnectionHandler.clientsStartingPositions.put(connection.id, packet.position);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus,
					ConnectionHandler.clientsStartingPositions, ConnectionHandler.foodPositions);

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);

				c.sendObject(upPacket);
			}

		} else if (p instanceof PlayerPositionPacket) {
			PlayerPositionPacket packet = (PlayerPositionPacket) p;

			// the player who was eaten by the monster should not be updated
			if (ConnectionHandler.isPlayerDead(connection.id)) {
				return;
			}

			ConnectionHandler.clientsPositions.put(packet.id, packet.position);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus,
					ConnectionHandler.clientsPositions, ConnectionHandler.foodPositions);

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				c.sendObject(upPacket);
			}

		} else if (p instanceof FoodPositionPacket) {
			FoodPositionPacket packet = (FoodPositionPacket) p;

			ConnectionHandler.foodPositions.put(packet.id, packet.foodPosition);

			PlayersUpdatePacket upPacket = new PlayersUpdatePacket(ConnectionHandler.ServersClientReadyStatus,
					ConnectionHandler.clientsPositions, ConnectionHandler.foodPositions);
			

			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				c.sendObject(upPacket);
			}
		} else if (p instanceof RematchPacket) {
			RematchPacket packet = (RematchPacket) p;
			ConnectionHandler.rematchPlayers.put(packet.id, true);

			RematchPlayersPacket upPacket = new RematchPlayersPacket(ConnectionHandler.rematchPlayers);
			for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				c.sendObject(upPacket);
			}

			// restart the game
			if (ConnectionHandler.rematchPlayers.size() == Settings.playerLimit) {
				//reinitialize
				ConnectionHandler.deadPlayers = new HashMap<>();
				ConnectionHandler.rematchPlayers = new HashMap<>();
				ConnectionHandler.foodPositions = new HashMap<>();
				Position[] gridObstacles = GameServer.generateGridObstacles();
				ConnectionHandler.gridObstacles = gridObstacles;
				StartGamePacket startPacket = new StartGamePacket(ConnectionHandler.clientsStartingPositions,
						gridObstacles);

				for (int i : ConnectionHandler.clientsStartingPositions.keySet()) {
					ConnectionHandler.clientsPositions.put(i,
							ConnectionHandler.clientsStartingPositions.get(i));
				}

				for (int i = 0; i < ConnectionHandler.connections.size(); i++) {
					Connection c = ConnectionHandler.connections.get(i);

					c.sendObject(startPacket);
				}

				Monster[] monsters = new Monster[]{new Monster(5, 5), new Monster(5, 5)};
				MonsterThread monsterThread = new MonsterThread(monsters);
				new Thread(monsterThread).start();
			}
		}
	}
}
