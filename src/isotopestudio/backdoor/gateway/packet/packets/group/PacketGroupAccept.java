package isotopestudio.backdoor.gateway.packet.packets.group;

import java.io.IOException;
import java.util.UUID;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class PacketGroupAccept extends Packet {

	public PacketGroupAccept() {
		super(GROUP_ACCEPT);
	}

	public PacketGroupAccept(String playerUUID) {
		super(GROUP_ACCEPT, playerUUID);
		this.playerUUID = playerUUID;
	}

	@Override
	public Packet clone() {
		return new PacketGroupAccept();
	}

	private String playerUUID;

	@Override
	public void read() {
		this.playerUUID = readString();
	}

	@Override
	public void process(GatewayClient client) {
			
	}
}
