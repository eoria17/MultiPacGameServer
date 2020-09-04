package packets;

import java.io.Serializable;

public class ReadyPacket implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int id;
	public boolean ready;
	
	public ReadyPacket(int id, boolean ready) {
		this.id = id;
		this.ready = ready;
	}



}
