package packets;

import java.io.Serializable;
import java.util.HashMap;

public class PlayersUpdatePacket implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public HashMap<Integer,Boolean> readyStatus;

	public PlayersUpdatePacket(HashMap<Integer, Boolean> readyStatus) {
		this.readyStatus = readyStatus;
	}

}
