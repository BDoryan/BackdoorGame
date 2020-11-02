package isotopestudio.backdoor.game.server;

import isotopestudio.backdoor.network.server.GameServer;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class ServerMaker extends GameServer {

	/**
	 * @param port
	 */
	public ServerMaker(int port, String password) {
		super(port, password);
	}
}
