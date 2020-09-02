package packets;

import java.io.Serializable;

public class SettingPacket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int limit;

	public SettingPacket(int limit) {
		this.limit = limit;
	}
	
}
