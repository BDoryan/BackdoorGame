package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.game.applications.terminal.interfaces.InterfaceType;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerAttackElement extends Packet {

	public PacketPlayerAttackElement() {
		super(ATTACK_ELEMENT);
	}

	public PacketPlayerAttackElement(String command) {
		super(ATTACK_ELEMENT, command);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerAttackElement();
	}

	private String command;

	public String getCommand() {
		return command;
	}

	@Override
	public void read() {
		command = readString();
	}

	@Override
	public void process(GameClient client) {
		boolean first_command = client.getCommand() == null;
		client.setCommand(getCommand());
		if (TerminalApplication.main != null && !first_command) {
			if (TerminalApplication.main.getInterface() == InterfaceType.ATTACK)
				TerminalApplication.main.load(InterfaceType.ATTACK);
		}
	}
}
