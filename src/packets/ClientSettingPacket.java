package packets;

import java.io.Serializable;

public class ClientSettingPacket implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int id;

	public ClientSettingPacket(int id) {
		this.id = id;
	}
}
