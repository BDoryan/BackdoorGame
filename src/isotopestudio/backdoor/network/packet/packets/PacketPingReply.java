package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPingReply extends Packet {
	
	public PacketPingReply() {
		super(PING_REPLY);
	}
	
	public PacketPingReply(long latency) {
		super(PING_REPLY, latency);
	}

	@Override
	public Packet clone() {
		return new PacketPingReply();
	}

	private long latency;
	
	public long getLatency() {
		return latency;
	}
	
	@Override
	public void read() {
		this.latency = readLong();
	}

	@Override
	public void process(GameClient client) {
		client.setPing(latency);
	}
}
