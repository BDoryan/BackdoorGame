package isotopestudio.backdoor.network.packet.packets;

import java.util.UUID;

import isotopestudio.backdoor.core.player.Player;
import isotopestudio.backdoor.core.team.Team;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerConnected extends Packet {

	public PacketPlayerConnected() {
		super(PLAYER_CONNECTED);
	}

	public PacketPlayerConnected(String username, String uuid, Team team) {
		super(PLAYER_CONNECTED, username, uuid, team);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerConnected();
	}

	private String username;
	private UUID uuid;
	private Team team;

	public String getUsername() {
		return username;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Team getTeam() {
		return team;
	}
	
	@Override
	public void read() {
		this.username = readString();
		this.uuid = UUID.fromString(readString());
		this.team = Team.valueOf(readString());
	}

	@Override
	public void process(GameClient client) {
		if (client.getUUID().toString().equalsIgnoreCase(getUUID().toString())) {
			client.setIdentified(true);
			
			client.setUsername(getUsername());
			client.setUUID(getUUID());
			client.setTeam(getTeam());

			client.getPlayers().put(getUUID().toString(), client);
		} else {
			Player player = new Player();
			
			player.setUsername(getUsername());
			player.setUUID(getUUID());
			player.setTeam(getTeam());
			
			client.getPlayers().put(getUUID().toString(), player);
		}
	}
}
