package packets;

import java.io.Serializable;
import java.util.HashMap;

public class RematchPlayersPacket implements Serializable{

	private static final long serialVersionUID = 1L;

	public HashMap<Integer, Boolean> rematchPlayers;

	public RematchPlayersPacket(HashMap<Integer, Boolean> rematchPlayers) {
		this.rematchPlayers = rematchPlayers;
	}
}
