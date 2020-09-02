package packets;

import java.io.Serializable;

public class RejectedPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String message;
	
	public RejectedPacket(String message) {
		this.message = message;
	}
	
	

}
