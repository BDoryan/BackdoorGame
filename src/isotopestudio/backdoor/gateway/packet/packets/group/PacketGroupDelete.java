package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupDelete extends Packet {

	public PacketGroupDelete() {
		super(GROUP_DELETE);
	}

	@Override
	public Packet clone() {
		return new PacketGroupDelete();
	}

	@Override
	public void read() {
	}

	@Override
	public void process(GatewayClient client) {
	}
}
