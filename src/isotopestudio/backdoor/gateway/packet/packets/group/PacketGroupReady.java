package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupReady extends Packet {

	public PacketGroupReady() {
		super(GROUP_READY);
	}

	public PacketGroupReady(Boolean ready) {
		super(GROUP_READY, ready);
		this.ready = ready;
	}

	@Override
	public Packet clone() {
		return new PacketGroupReady();
	}

	private boolean ready;

	@Override
	public void read() {
		this.ready = readBoolean();
	}
	
	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	@Override
	public void process(GatewayClient client) {
	}
}
