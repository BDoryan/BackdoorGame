package isotopestudio.backdoor.network.packet.packets;

import isotopestudio.backdoor.core.gamescript.GameScript;
import isotopestudio.backdoor.core.gamescript.GameScript.GameScripts;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class PacketPlayerBuyGameScript extends Packet {

	public PacketPlayerBuyGameScript() {
		super(PLAYER_BUY_GAME_SCRIPT);
	}

	public PacketPlayerBuyGameScript(GameScript gameScript, int amount) {
		super(PLAYER_BUY_GAME_SCRIPT, gameScript.getName(), amount);
	}

	public PacketPlayerBuyGameScript(GameScript gameScript) {
		super(PLAYER_BUY_GAME_SCRIPT, gameScript.getName(), 1);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerBuyGameScript();
	}

	private GameScripts gameScript;
	private int amount;

	@Override
	public void read() {
		this.gameScript = GameScripts.fromName(readString());
		this.amount = readInt();
	}

	/**
	 * @return the gameScript
	 */
	public GameScripts getGameScript() {
		return gameScript;
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	@Override
	public void process(GameClient client) {
		
	}
}
