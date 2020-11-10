package isotopestudio.backdoor.gateway.packet.packets.group;

import java.io.IOException;

import isotopestudio.backdoor.core.gamemode.GameMode;
import isotopestudio.backdoor.core.server.configuration.GameServerConfiguration;
import isotopestudio.backdoor.core.versus.Versus;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class PacketGroupStart extends Packet {

	public PacketGroupStart() {
		super(GROUP_START);
	}

	private Versus versus;
	private GameMode gameMode;

	public PacketGroupStart(Versus versus, GameMode gameMode) {
		super(GROUP_START, versus.getText(), gameMode.toString());
		this.versus = versus;
		this.gameMode = gameMode;
	}

	@Override
	public Packet clone() {
		return new PacketGroupStart();
	}

	@Override
	public void read() {
		this.versus = Versus.fromString(readString());
		this.gameMode = GameMode.fromString(readString());
	}
	
	/**
	 * @return the versus
	 */
	public Versus getVersus() {
		return versus;
	}
	
	/**
	 * @return the gameMode
	 */
	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public void process(GatewayClient client) {

	}
}
