package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupCancel extends Packet {

	public PacketGroupCancel() {
		super(GROUP_CANCEL);
	}

	@Override
	public Packet clone() {
		return new PacketGroupCancel();
	}

	@Override
	public void read() {
	}
	
	@Override
	public void process(GatewayClient client) {
	}	
}