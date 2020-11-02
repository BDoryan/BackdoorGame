package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerKick extends Packet {

	public PacketPlayerKick() {
		super(KICK);
	}

	public PacketPlayerKick(String reason) {
		super(KICK, reason);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerKick();
	}

	private String reason;

	public String getUUID() {
		return reason;
	}

	@Override
	public void read() {
		this.reason = readString();
	}

	@Override
	public void process(GameClient client) {
		System.out.println("You have been kicked "+getUUID()+".");
	}
}
