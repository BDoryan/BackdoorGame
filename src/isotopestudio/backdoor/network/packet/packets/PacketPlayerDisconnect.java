package isotopestudio.backdoor.network.packet.packets;

import java.util.UUID;

import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerDisconnect extends Packet {

	public PacketPlayerDisconnect() {
		super(DISCONNECT);
	}

	public PacketPlayerDisconnect(UUID uuid, String reason) {
		super(DISCONNECT, uuid, reason);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerDisconnect();
	}

	private UUID uuid;
	private String reason;
	
	public String getReason() {
		return reason;
	}

	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void read() {
		this.uuid = UUID.fromString(readString());
		this.reason = readString();
	}

	@Override
	public void process(GameClient client) {
		disconnect(client, getUUID(), getReason());
	}
	
	public static void disconnect(GameClient client, UUID uuid, String reason) {
		if (client.getUUID().toString().equalsIgnoreCase(uuid.toString())) {
			client.setIdentified(false);
			client.getPlayers().clear();
			client.disconnect(reason);
		} else {
			client.getPlayers().remove(uuid.toString());
		}
	}
}
