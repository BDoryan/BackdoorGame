package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerDisconnectCurrentElement extends Packet {

	public PacketPlayerDisconnectCurrentElement() {
		super(DISCONNECT_ENTITY);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerDisconnectCurrentElement();
	}
	
	@Override
	public void read() {
	}

	@Override
	public void process(GameClient client) {
		client.setTargetAddress(null);
		TerminalApplication.closeApplication();
	}
}
