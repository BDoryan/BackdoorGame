package isotopestudio.backdoor.network.packet.packets;

import java.util.ArrayList;
import java.util.LinkedList;

import doryanbessiere.isotopestudio.commons.GsonInstance;
import isotopestudio.backdoor.core.gamescript.GameScript;
import isotopestudio.backdoor.core.gamescript.GameScript.GameScripts;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerScriptsUpdate extends Packet {

	public PacketPlayerScriptsUpdate() {
		super(PLAYER_SCRIPTS_UPDATE);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerScriptsUpdate();
	}

	private LinkedList<GameScript> scripts = new LinkedList<>();

	@SuppressWarnings("unchecked")
	@Override
	public void read() {
		ArrayList<String> scripts = GsonInstance.instance().fromJson(readString(), ArrayList.class);
		
		for(String script : scripts) {
			this.scripts.add(GameScripts.fromName(script).getGameScript());	
		}
	}
	
	/**
	 * @return the scripts
	 */
	public LinkedList<GameScript> getScripts() {
		return scripts;
	}

	@Override
	public void process(GameClient client) {
		client.getScripts().clear();
		client.getScripts().addAll(getScripts());
	}
}
