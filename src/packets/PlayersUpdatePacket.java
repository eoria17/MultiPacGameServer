package packets;

import java.io.Serializable;
import java.util.HashMap;

public class PlayersUpdatePacket implements Serializable{
	
	public HashMap<Integer,Boolean> readyStatus;
	public int test;

	public PlayersUpdatePacket(HashMap<Integer, Boolean> readyStatus) {
		this.readyStatus = readyStatus;
	}

}
