package isotopestudio.backdoor.network.packet.packets;

import doryanbessiere.isotopestudio.api.user.User;
import isotopestudio.backdoor.network.client.GameClient;
import isotopestudio.backdoor.network.packet.Packet;

public class PacketPlayerLogin extends Packet {

	public static final int USERNAME_ALREADY_USED = 1;
	public static final int SERVER_FULL = 2;
	public static final int INVALID_AUTHENTICATION_SESSION = 3;
	public static final int USERNAME_CHANGED = 4;
	public static final int INVALID_VERSION = 5;
	public static final int NOT_ALLOWED_TO_LOGIN = 6;
	public static final int WRONG_PASSWORD = 7;

	public PacketPlayerLogin() {
		super(LOGIN);
	}

	public PacketPlayerLogin(User user, String gameVersion, String password) {
		super(LOGIN, user.toJson(), gameVersion, password);
	}

	@Override
	public Packet clone() {
		return new PacketPlayerLogin();
	}

	private User user;
	private String gameVersion;
	private String password;
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getGameVersion() {
		return gameVersion;
	}

	@Override
	public void read() {
		this.user = User.fromJson(readString());
		this.gameVersion = readString();
		this.password = readString();
	}

	@Override
	public void process(GameClient client) {
	}
}
