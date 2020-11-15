package isotopestudio.backdoor.gateway.packet.packets.group;

import isotopestudio.backdoor.core.gamemode.GameMode;
import isotopestudio.backdoor.core.versus.Versus;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketGroupSet extends Packet {

	public PacketGroupSet() {
		super(GROUP_SET);
	}
	
	private boolean isPrivate = false;
	private GameMode gamemode;
	private Versus versus;

	public PacketGroupSet(GameMode gamemode, Versus versus, boolean isPrivate) {
		super(GROUP_SET, gamemode.toString(), versus.getText(), isPrivate);
	}

	@Override
	public Packet clone() {
		return new PacketGroupSet();
	}

	@Override
	public void read() {
		this.gamemode = GameMode.fromString(readString());
		this.versus = Versus.fromString(readString());
		this.isPrivate = readBoolean();
	}

	/**
	 * @return the versus
	 */
	public Versus getVersus() {
		return versus;
	}
	
	/**
	 * @return the gamemode
	 */
	public GameMode getGameMode() {
		return gamemode;
	}
	
	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return isPrivate;
	}
	
	@Override
	public void process(GatewayClient client) {
	}
}
