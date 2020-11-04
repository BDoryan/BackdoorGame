package isotopestudio.backdoor.gateway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import doryanbessiere.isotopestudio.api.user.User;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.listeners.ChatListener;
import isotopestudio.backdoor.gateway.packet.Packet;
import isotopestudio.backdoor.gateway.packet.packets.PacketClientDisconnected;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class GatewayClient extends Thread {

	public static GatewayClient INSTANCE;

	private Socket socket;

	private DataOutputStream output;
	private DataInputStream input;

	private User user;
	private boolean connected = false;

	public GatewayClient(User user) {
		this.user = user;
		INSTANCE = this;
	}

	private String address;
	private Integer port;
	
	private Timer timer;

	public void connect(String address, int port, boolean autoconnect) throws UnknownHostException, IOException {
		this.address = address;
		this.port = port;
		if(!autoconnect && timer != null) {
			timer.cancel();
		}
		if(autoconnect) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(isConnected())return;
					try {
						reconnect();
						if(isAlive())
							stop();
						
						start();
					} catch (Exception e) {
						System.err.println("The server managing the gateway is currently unavailable...");
						System.err.println("Attempt to reconnect in 5 seconds.");
					}
				}
			}, 5000L, 5000L);
		}
		connect();
	}

	public void reconnect() throws UnknownHostException, IOException {
		if (!isConnected()) 
			connect();
	}

	public void connect() throws UnknownHostException, IOException {
		if (this.socket != null) {
			close();
		}
		if(address == null || port == null) {
			throw new IllegalStateException("address == null || port == null");
		}
		
		this.socket = new Socket(address, port);	
		this.output = new DataOutputStream(this.socket.getOutputStream());
		this.input = new DataInputStream(this.socket.getInputStream());
		this.connected = true;
		start();
	}

	@Override
	public void run() {

		System.out.println("You are connected to the gateway");
		sendData(BackdoorGame.GAME_VERSION + ";" + user.getEmail() + ";" + user.getToken());
		if(dataInWaiting.size() > 0) {
			for(String data : dataInWaiting) {
				sendData(data);
			}
		}
		while (connected) {
			try {
				Packet packet = readPacket();
				try {
					processPacket(packet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				connected = false;
			}
		}
		disconnected();
	}
	
	public ArrayList<String> dataInWaiting = new ArrayList<>();

	public void sendData(String data) {
		if(!isConnected()) {
			dataInWaiting.add(data);
			return;
		}
		try {
			write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPacket(Packet packet) {
		String data = packet.toData();
		sendData(data);
		BackdoorGame.getLogger().debug("sendPacket("+data+")");
	}
	
	public void processPacket(Packet packet) {
		packet.process(this);
	}

	/**
	 * 
	 * This method is called when you decide to read the next packet
	 * 
	 * @return
	 * @throws IOException
	 */
	public Packet readPacket() throws IOException {
		String data = read();
		BackdoorGame.getLogger().debug("readPacket("+data+")");
		Packet packet = Packet.parsePacket(data);
		packet.read();
		return packet;
	}

	/***
	 * 
	 * This method is called when you decide to write a message to the server
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	public void write(String data) throws IOException {
		// output.writeInt(bytes.length);
		output.writeUTF(data);
		output.flush();
	}

	/**
	 *
	 * This method is called when you want to read the last message (puts the thread
	 * on hold if it is still not received).
	 *
	 * @return
	 * @throws IOException
	 */
	public String read() throws IOException {
		return input.readUTF();
	}

	/**
	 * 
	 * This method is called when you decide to close the connection.
	 * 
	 * @throws IOException
	 */
	public void close() {
		try {
			connected = false;
			if (socket != null && !socket.isClosed())
				socket.close();
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnected() {
		System.out.println("The connection to the gateway has been interrupted.");
		try {
			socket.close();
		} catch (Exception e) {
		}
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * 
	 */
	public void disconnect(String reason) {
		sendPacket(new PacketClientDisconnected(PacketClientDisconnected.DISCONNECTED, reason));
		close();
	}

	private ArrayList<ChatListener> chatListeners = new ArrayList<>();

	/**
	 * @return the chat listeners
	 */
	public ArrayList<ChatListener> getChatListeners() {
		return chatListeners;
	}

	/**
	 * @param b
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
