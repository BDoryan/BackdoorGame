package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupCreate extends Packet {

	public PacketGroupCreate() {
		super(GROUP_CREATE);
	}

	@Override
	public Packet clone() {
		return new PacketGroupCreate();
	}

	@Override
	public void read() {
	}

	@Override
	public void process(GatewayClient client) {
	}
}
