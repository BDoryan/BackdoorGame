package isotopestudio.backdoor.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import doryanbessiere.isotopestudio.api.user.User;
import isotopestudio.backdoor.core.map.MapData;
import isotopestudio.backdoor.core.player.Player;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.game.GameParty;
import isotopestudio.backdoor.network.packet.Packet;
import isotopestudio.backdoor.network.packet.PacketListener;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerDisconnect;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerLogin;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerLoginFailed;
import isotopestudio.backdoor.network.player.NetworkedPlayer;

public class GameClient extends NetworkedPlayer {

	private String address;
	private int port;

	private MapData mapData;
	
	public MapData getMap() {
		return mapData;
	}
	
	public void setMap(MapData mapData) {
		this.mapData = mapData;
	}

	public GameClient(String address, int port) {
		this.address = address;
		this.port = port;
	}

	private HashMap<String, Player> players = new HashMap<String, Player>();

	public HashMap<String, Player> getPlayers() {
		return players;
	}

	private Socket socket;

	/**
	 * 
	 * This method is called when you decide to connect to the server
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public int connect(User user, String password) throws UnknownHostException, IOException {
		this.socket = new Socket(address, port);
		DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());
		DataInputStream input = new DataInputStream(this.socket.getInputStream());

		initialize(socket, input, output);
		setUsername(user.getUsername());
		setUUID(user.getUUID());

		sendPacket(new PacketPlayerLogin(user, BackdoorGame.GAME_VERSION, password));

		Packet packet = readPacket();
		packet.read();
		packet.process(this);

		int disconnect_id = -1;

		switch (packet.getId()) {
		case Packet.LOGIN_FAILED:
			System.out.println("Connection failed");
			disconnect_id = ((PacketPlayerLoginFailed) packet).getDisconnectID();
			break;
		default:
			disconnect_id = 0;
			setConnected(true);
			receiver().start();
			break;
		}

		return disconnect_id;
	}

	private boolean identified;

	public void setIdentified(boolean identified) {
		this.identified = identified;
	}

	public boolean isIdentified() {
		return identified;
	}

	public Thread receiver() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					try {
						Packet packet = readPacket();
						try {
							if (packet != null) {
								packet.read();
								packet.process(GameClient.this);
								if (packet != null) {
									if (!getPacketListeners().isEmpty()) {
										for (PacketListener listener : getPacketListeners()) {
											listener.received(packet);
										}
									}
								}
							}
						} catch (Exception e) {
							if(!isConnected())
								return;
							// packet error and not connection lost
							e.printStackTrace();
						}
					} catch (IOException e) {
						if(!isConnected())
							return;
						PacketPlayerDisconnect.disconnect(GameClient.this, getUUID(), "connection_lost");
						break;
					}
				}
			}
		});
		return thread;
	}

	@Override
	public boolean waitPacket(Packet packet_target, long timeout) {
		AtomicBoolean received = new AtomicBoolean(false);
		PacketListener listener = new PacketListener() {
			@Override
			public void sended(Packet packet) {
			}

			@Override
			public void received(Packet packet) {
				if (packet.getId() == packet_target.getId()) {
					received.set(true);
				}
			}
		};
		getPacketListeners().add(listener);
		long timeleft = System.currentTimeMillis() + timeout;
		while (!received.get()) {
			if (timeleft - System.currentTimeMillis() < 0) {
				// Timeout
				break;
			}
		}
		getPacketListeners().remove(listener);
		return received.get();
	}
	
	public void disconnect(String reason) {
		setConnected(false);
		if(BackdoorGame.getGameParty() != null) {
			BackdoorGame.getGameParty().disconnect();
		}
		close();
	}

	public Socket getSocket() {
		return socket;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
	
	private GameParty gameParty;

	public void startParty() {
		if(gameParty != null) {
			return;
		}
		gameParty = new GameParty();
		gameParty.start();
	}
	
	public void stopParty() {
		if(gameParty != null) {
			gameParty.stop();
		}
	}
}
