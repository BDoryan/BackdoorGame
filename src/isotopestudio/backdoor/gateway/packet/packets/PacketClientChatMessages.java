package isotopestudio.backdoor.gateway.packet.packets;

import doryanbessiere.isotopestudio.api.profile.Profile;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.GatewayClient;
import isotopestudio.backdoor.gateway.packet.Packet;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PacketClientChatMessages extends Packet {

	private Profile[] profiles;
	private long[] times;
	private String[] messages;
	private int count;
	
	/**
	 * @param id
	 * @param datas
	 */
	public PacketClientChatMessages() {
		super(CLIENT_CHAT_MESSAGES);
	}

	@Override
	public Packet clone() {
		return new PacketClientChatMessages();
	}
	
	@Override
	public void read() {
		profiles = BackdoorGame.getGson().fromJson(readString(), Profile[].class);
		times = BackdoorGame.getGson().fromJson(readString(), long[].class);
		messages = BackdoorGame.getGson().fromJson(readString(), String[].class);
		count = readInt();
	}

	/**
	 * @return the profiles
	 */
	public Profile[] getProfiles() {
		return profiles;
	}
	
	/**
	 * @return the messages
	 */
	public String[] getMessages() {
		return messages;
	}
	
	/**
	 * @return the times
	 */
	public long[] getTimes() {
		return times;
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	
	@Override
	public void process(GatewayClient client) {
		if(profiles.length == count && messages.length == count && times.length == count) {
			for(int index = 0; index < count; index++) {
				Profile profile = getProfiles()[index];
				long time = getTimes()[index];
				String message = getMessages()[index];
				client.getChatListeners().forEach((chatListener) -> chatListener.message(profile, time, message));
			}	
		}
	}
}
