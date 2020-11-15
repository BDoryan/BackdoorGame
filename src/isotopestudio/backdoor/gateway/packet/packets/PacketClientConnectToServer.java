package isotopestudio.backdoor.gateway.packet.packets;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketClientConnectToServer extends Packet {
	
	private String address;
	private int port;
	
	public PacketClientConnectToServer(String address, int port) {
		super(CLIENT_CONNECT_TO_SERVER, address, port);
		this.address = address;
		this.port = port;
	}
	
	public PacketClientConnectToServer() {
		super(CLIENT_CONNECT_TO_SERVER);
	}
	
	@Override
	public Packet clone() {
		return new PacketClientConnectToServer();
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	@Override
	public void read() {
		this.address = readString();
		this.port = readInt();
	}

	@Override
	public void process(GatewayClient client) {
		BackdoorGame.connect(address, port, null);
	}
}
