package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupLeave extends Packet {

	public PacketGroupLeave() {
		super(GROUP_LEAVE);
	}

	@Override
	public Packet clone() {
		return new PacketGroupLeave();
	}

	@Override
	public void read() {
	}

	@Override
	public void process(GatewayClient client) {
	}
}
