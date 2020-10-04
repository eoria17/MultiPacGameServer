package packets;

import java.io.Serializable;

public class RematchPacket implements Serializable{

	private static final long serialVersionUID = 1L;

	public int id;

	public RematchPacket(int id) {
		this.id = id;
	}
}
