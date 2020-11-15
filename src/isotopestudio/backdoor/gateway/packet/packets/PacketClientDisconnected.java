package isotopestudio.backdoor.gateway.packet.packets;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketClientDisconnected extends Packet {

	public static final int DISCONNECTED = 0;
	public static final int KICKED = 1;

	/**
	 * @param id
	 * @param datas
	 */
	public PacketClientDisconnected() {
		super(CLIENT_DISCONNECTED);
	}

	private int type;
	private String reason;

	public PacketClientDisconnected(int type, String reason) {
		super(CLIENT_DISCONNECTED, type, reason);
	}

	@Override
	public Packet clone() {
		return new PacketClientDisconnected();
	}

	@Override
	public void read() {
		type = readInt();
		reason = readString();
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	@Override
	public void process(GatewayClient client) {
		if (type == DISCONNECTED)
			System.out.println("You have been disconnected from the gateway.");
		else
			System.out.println("You have been kicked from the gateway. (reason="+getReason()+")");
		
		client.setConnected(false);
	}
}
