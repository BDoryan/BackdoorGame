package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.core.gamescript.GameScript;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerExecScript extends Packet {

	public PacketPlayerExecScript() {
		super(PLAYER_EXEC_SCRIPT);
	}

	public PacketPlayerExecScript(GameScript gameScript) {
		super(PLAYER_EXEC_SCRIPT, gameScript.getName());
	}

	@Override
	public Packet clone() {
		return new PacketPlayerExecScript();
	}
	
	@Override
	public void read() {
	}
	
	@Override
	public void process(GameClient client) {
	}
}
