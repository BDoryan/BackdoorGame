package isotopestudio.backdoor.gateway.packet.packets.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupKick extends Packet {

	public PacketGroupKick() {
		super(GROUP_KICK);
	}

	public PacketGroupKick(String playerUUID) {
		super(GROUP_KICK, playerUUID);
		this.playerUUID = playerUUID;
	}

	@Override
	public Packet clone() {
		return new PacketGroupKick();
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
