package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerConnectToElement extends Packet {

	public PacketPlayerConnectToElement() {
		super(CONNECT_ENTITY);
	}

	public PacketPlayerConnectToElement(String adress) {
		super(CONNECT_ENTITY, adress);
	}
	
	@Override
	public Packet clone() {
		return new PacketPlayerConnectToElement();
	}

	private String address;
	
	public String getAddress() {
		return address;
	}
	
	@Override
	public void read() {
		this.address = readString();
	}

	@Override
	public void process(GameClient client) {
		if(getAddress() == null) {
			System.err.println("You cannot connect to server!");
			return;
		}
					
		client.setTargetAddress(getAddress());
		TerminalApplication.showApplication();
	}
}
