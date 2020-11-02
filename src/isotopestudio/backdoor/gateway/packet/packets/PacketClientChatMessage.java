package isotopestudio.backdoor.gateway.packet.packets;

import doryanbessiere.isotopestudio.api.profile.Profile;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class PacketClientChatMessage extends Packet {

	private Profile profile;
	private long time;
	private String message;
	
	public PacketClientChatMessage(String message) {
		super(CLIENT_CHAT_MESSAGE, message);
	}

	/**
	 * @param id
	 * @param datas
	 */
	public PacketClientChatMessage() {
		super(CLIENT_CHAT_MESSAGE);
	}

	@Override
	public Packet clone() {
		return new PacketClientChatMessage();
	}
	
	@Override
	public void read() {
		profile = Profile.fromJson(readString());
		time = readLong();
		message = readString(); 
	}
	
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}
	
	/**
	 * @return the reason
	 */
	public String getMessage() {
		return message;
	}
	
	@Override
	public void process(GatewayClient client) {
		client.getChatListeners().forEach((chatListener) -> chatListener.message(profile, time, message));
	}
}
