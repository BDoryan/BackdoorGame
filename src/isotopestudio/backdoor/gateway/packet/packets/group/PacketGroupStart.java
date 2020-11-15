package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupStart extends Packet {

	public PacketGroupStart() {
		super(GROUP_START);
	}

	@Override
	public Packet clone() {
		return new PacketGroupStart();
	}

	@Override
	public void read() {
	}
	
	@Override
	public void process(GatewayClient client) {

	}
}
